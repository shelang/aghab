package io.shelang.aghab.service.redirect.impl;

import io.shelang.aghab.enums.LinkStatus;
import io.shelang.aghab.enums.RedirectType;
import io.shelang.aghab.enums.WebhookStatus;
import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.AnalyticLinkEvent;
import io.shelang.aghab.event.dto.WebhookCallEvent;
import io.shelang.aghab.service.dto.link.RedirectDTO;
import io.shelang.aghab.service.dto.script.ScriptDTO;
import io.shelang.aghab.service.link.LinksService;
import io.shelang.aghab.service.redirect.RedirectService;
import io.shelang.aghab.service.script.ScriptServiceImpl;
import io.shelang.aghab.service.uaa.UserAgentAnalyzer;
import io.shelang.aghab.util.RedirectSanitizer;
import io.smallrye.mutiny.Uni;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.PreparedQuery;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

@ApplicationScoped
public class RedirectServiceImpl implements RedirectService {

  private static final String DEFAULT_REDIRECT_SCRIPT_TITLE = """
      Please be patient, you are being redirected to the desired page...
      \n
      ...لطفا صبور باشید، شما در حال انتقال به صفحه مورد نظر هستید
      """;
  private static final Integer DEFAULT_SCRIPT_TIMEOUT = 10_000;
  private static final String QUESTION_MARK = "&";
  private static final String AMPERSAND_MARK = "&";

  final io.vertx.mutiny.pgclient.PgPool client;
  final LinksService linksService;
  final EventBus bus;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  public RedirectServiceImpl(@Default PgPool client, LinksService linksService, EventBus bus) {
    this.client = client;
    this.linksService = linksService;
    this.bus = bus;
  }

  private static RedirectDTO from(Row row) {
    return new RedirectDTO()
        .setId(row.getLong("id"))
        .setType(RedirectType.from(row.getString("type")))
        .setScriptId(row.getLong("script_id"))
        .setWebhookId(row.getLong("webhook_id"))
        .setUrl(row.getString("url"))
        .setStatusCode(row.getShort("redirect_code"))
        .setForwardParameter(row.getBoolean("forward_parameter"));
  }

  private static RedirectDTO fromByUA(Row row) {
    return from(row).setAltUrl(row.getString("alt_url")).setAltKey(row.getString("key"));
  }

  private static Set<RedirectDTO> fromByUA(RowSet<Row> rows) {
    var result = new HashSet<RedirectDTO>();

    for (Row row : rows) {
      if (result.isEmpty()) {
        result.add(from(row));
      }
      result.add(fromByUA(row));
    }
    return result;
  }

  @Override
  public Uni<RedirectDTO> redirectBy(RoutingContext rc) {
    String hash = rc.request().getParam("hash");
    String query = rc.request().query();
    MultiMap headers = rc.request().headers();
    return redirectByUserAgent(hash, query, headers);
  }

  private Function<Set<RedirectDTO>, RedirectDTO> addAlternativeLink(List<String> linkTypes) {
    return sr -> {
      var byHash = new RedirectDTO().setStatusCode((short) 404);
      if (sr.isEmpty()) {
        return byHash;
      }

      byHash = sr.stream().filter(r -> Objects.isNull(r.getAltKey())).findFirst().orElse(byHash);

      for (RedirectDTO r : sr) {
        if (Objects.nonNull(r.getAltKey()) && linkTypes.contains(r.getAltKey().toUpperCase())) {
          byHash = r;
          break;
        }
      }

      return byHash;
    };
  }

  private Function<RedirectDTO, RedirectDTO> makeRedirectLink(String query) {
    return byHash -> {
      String redirectTo = byHash.getAltUrl() == null ? byHash.getUrl() : byHash.getAltUrl();
      redirectTo = setForwardParameters(query, byHash, redirectTo);
      redirectTo = RedirectSanitizer.sanitizeRedirectUrl(redirectTo);
      byHash.setUrl(redirectTo);
      return byHash;
    };
  }

  private String setForwardParameters(String query, RedirectDTO byHash, String redirectTo) {
    if (Objects.nonNull(query) && byHash.isForwardParameter()) {
      if (redirectTo.contains(QUESTION_MARK)) {
        redirectTo = redirectTo + AMPERSAND_MARK + query;
      } else {
        redirectTo = redirectTo + QUESTION_MARK + query;
      }
    }
    return redirectTo;
  }

  private Function<RedirectDTO, Uni<?>> sendAnalyticEvent(String hash, MultiMap headers) {
    return byHash -> {
      AnalyticLinkEvent event = AnalyticLinkEvent.builder().id(byHash.getId()).headers(headers).hash(hash).build();
      bus.send(EventType.ANALYTIC_LINK, event);
      return Uni.createFrom().item(byHash);
    };
  }

  private Uni<RedirectDTO> scriptRedirection(RedirectDTO byHash) {
    return queryScriptById()
        .execute(Tuple.of(byHash.getScriptId()))
        .onItem()
        .transform(ScriptServiceImpl::from)
        .onItem()
        .transform(setScriptOnRedirection(byHash));
  }

  private Function<RedirectDTO, Uni<? extends RedirectDTO>> addMetaData() {
    return byHash -> {
      switch (byHash.getType()) {
        case SCRIPT:
          return scriptRedirection(byHash);
        case IFRAME:
        case REDIRECT:
          return Uni.createFrom().item(byHash);
      }
      return null;
    };
  }

  private PreparedQuery<RowSet<Row>> queryScriptById() {
    return this.client
        .preparedQuery("SELECT id, name, timeout, content, title FROM scripts WHERE id = $1");
  }

  private static Function<ScriptDTO, RedirectDTO> setScriptOnRedirection(RedirectDTO byHash) {
    return script -> {
      if (Objects.isNull(script)) {
        return byHash;
      }
      String title = Objects.nonNull(script.getTitle())
          ? script.getTitle()
          : DEFAULT_REDIRECT_SCRIPT_TITLE;
      return byHash
          .setStatusCode((short) 200)
          .setTitle(title)
          .setTimeout(
              Objects.nonNull(script.getTimeout())
                  ? script.getTimeout()
                  : DEFAULT_SCRIPT_TIMEOUT)
          .setContent(script.getContent());
    };
  }

  private Function<RedirectDTO, Uni<?>> sendWebhookEvent(String hash) {
    return byHash -> {
      if (Objects.nonNull(byHash.getWebhookId()) &&
          !WebhookStatus.SENT.equals(byHash.getWebhookStatus())) {
        WebhookCallEvent event = WebhookCallEvent.builder()
            .webhookId(byHash.getWebhookId())
            .linkId(byHash.getId())
            .hash(hash)
            .build();
        bus.send(EventType.WEBHOOK_CALL, event);
      }
      return Uni.createFrom().item(byHash);
    };
  }

  private Uni<RowSet<Row>> queryLinkByHash(String hash) {
    return client
        .preparedQuery(
            "SELECT l.id, "
                + "     l.url, "
                + "     l.redirect_code, "
                + "     l.forward_parameter, "
                + "     la.key, "
                + "     la.url alt_url, "
                + "     l.type, "
                + "     l.script_id, "
                + "     l.webhook_id, "
                + "     l.webhook_status "
                + "FROM links l "
                + "LEFT JOIN link_alternatives la on l.id = la.link_id "
                + "WHERE l.hash = $1 and l.status = "
                + LinkStatus.ACTIVE.ordinal())
        .execute(Tuple.of(hash));
  }

  private Uni<RedirectDTO> redirectByUserAgent(String hash, String query, MultiMap headers) {
    var ua = headers.get(HttpHeaders.USER_AGENT);
    var linkTypes = UserAgentAnalyzer.detectType(ua);
    return queryLinkByHash(hash)
        .onItem()
        .transform(RedirectServiceImpl::fromByUA)
        .onItem()
        .transform(addAlternativeLink(linkTypes))
        .onItem()
        .transform(makeRedirectLink(query))
        .onItem()
        .transformToUni(addMetaData())
        .onItem()
        .call(sendAnalyticEvent(hash, headers))
        .onItem()
        .call(sendWebhookEvent(hash));
  }
}

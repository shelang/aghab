package io.shelang.aghab.service.redirect.impl;

import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.AnalyticLinkEvent;
import io.shelang.aghab.service.dto.RedirectDTO;
import io.shelang.aghab.service.link.LinksService;
import io.shelang.aghab.service.redirect.RedirectService;
import io.shelang.aghab.service.uaa.UserAgentAnalyzer;
import io.smallrye.mutiny.Uni;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@ApplicationScoped
public class RedirectServiceImpl implements RedirectService {

  final io.vertx.mutiny.pgclient.PgPool client;
  final LinksService linksService;
  final EventBus bus;

  @Inject
  public RedirectServiceImpl(@Default PgPool client, LinksService linksService, EventBus bus) {
    this.client = client;
    this.linksService = linksService;
    this.bus = bus;
  }

  private static RedirectDTO from(Row row) {
    return new RedirectDTO()
        .setId(row.getLong("id"))
        .setUrl(row.getString("url"))
        .setStatusCode(row.getShort("redirect_code"))
        .setForwardParameter(row.getBoolean("forward_parameter"));
  }

  private static RedirectDTO fromByUA(Row row) {
    return new RedirectDTO()
        .setId(row.getLong("id"))
        .setUrl(row.getString("url"))
        .setAltUrl(row.getString("alt_url"))
        .setAltKey(row.getString("key"))
        .setStatusCode(row.getShort("redirect_code"))
        .setForwardParameter(row.getBoolean("forward_parameter"));
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
      if (sr.isEmpty()) return byHash;

      byHash = sr.stream().filter(r -> Objects.isNull(r.getAltKey())).findFirst().orElse(byHash);

      for (RedirectDTO r : sr)
        if (Objects.nonNull(r.getAltKey()) && linkTypes.contains(r.getAltKey().toUpperCase()))
          byHash = r;

      return byHash;
    };
  }

  private Function<RedirectDTO, RedirectDTO> makeRedirectLink(String query) {
    return byHash -> {
      String redirectTo = byHash.getAltUrl() == null ? byHash.getUrl() : byHash.getAltUrl();
      if (Objects.nonNull(query) && byHash.isForwardParameter()) {
        if (byHash.getUrl().contains("?")) {
          redirectTo = redirectTo + "&" + query;
        } else {
          redirectTo = redirectTo + "?" + query;
        }
      }

      byHash.setUrl(redirectTo);

      return byHash;
    };
  }

  private Function<RedirectDTO, Uni<?>> sendAnalyticEvent(String hash, MultiMap headers) {
    return byHash -> {
      if (byHash.getStatusCode() >= 300 && byHash.getStatusCode() < 400) {
        AnalyticLinkEvent event =
            AnalyticLinkEvent.builder().id(byHash.getId()).headers(headers).hash(hash).build();
        bus.send(EventType.ANALYTIC_LINK, event);
      }

      return Uni.createFrom().item(byHash);
    };
  }

  private Uni<RedirectDTO> redirectByUserAgent(String hash, String query, MultiMap headers) {
    var ua = headers.get(HttpHeaders.USER_AGENT);
    var linkTypes = UserAgentAnalyzer.detectType(ua);
    return client
        .preparedQuery(
            "SELECT l.id, "
                + "   l.url, "
                + "   l.redirect_code, "
                + "   l.forward_parameter, "
                + "   la.key, "
                + "   la.url alt_url "
                + "FROM links l "
                + "LEFT JOIN link_alternatives la on l.id = la.link_id "
                + "WHERE l.hash = $1 and l.status = 0")
        .execute(Tuple.of(hash))
        .onItem()
        .transform(RedirectServiceImpl::fromByUA)
        .onItem()
        .transform(addAlternativeLink(linkTypes))
        .onItem()
        .transform(makeRedirectLink(query))
        .onItem()
        .call(sendAnalyticEvent(hash, headers));
  }
}

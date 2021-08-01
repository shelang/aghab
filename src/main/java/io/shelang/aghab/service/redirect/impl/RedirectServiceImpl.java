package io.shelang.aghab.service.redirect.impl;

import io.shelang.aghab.event.EventType;
import io.shelang.aghab.event.dto.AnalyticLinkEvent;
import io.shelang.aghab.service.dto.RedirectDTO;
import io.shelang.aghab.service.link.LinksService;
import io.shelang.aghab.service.redirect.RedirectService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
public class RedirectServiceImpl implements RedirectService {

  final io.vertx.mutiny.pgclient.PgPool client;
  final LinksService linksService;
  final EventBus bus;
  final UserAgentAnalyzer uaa;

  @Inject
  public RedirectServiceImpl(
      @Default PgPool client, LinksService linksService, EventBus bus, UserAgentAnalyzer uaa) {
    this.client = client;
    this.linksService = linksService;
    this.bus = bus;
    this.uaa = uaa;
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
        .setStatusCode(row.getShort("redirect_code"))
        .setForwardParameter(row.getBoolean("forward_parameter"));
  }

  private static Set<RedirectDTO> fromByUA(RowSet<Row> rows) {
    var result = new HashSet<RedirectDTO>();

    for (Row row : rows) {
      if (result.isEmpty()) {
        result.add(
            new RedirectDTO()
                .setId(row.getLong("id"))
                .setUrl(row.getString("url"))
                .setStatusCode(row.getShort("redirect_code"))
                .setForwardParameter(row.getBoolean("forward_parameter")));
      }
      result.add(
          new RedirectDTO()
              .setId(row.getLong("id"))
              .setUrl(row.getString("url"))
              .setAltUrl(row.getString("alt_url"))
              .setAltKey(row.getString("key"))
              .setStatusCode(row.getShort("redirect_code"))
              .setForwardParameter(row.getBoolean("forward_parameter")));
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

  private Uni<RedirectDTO> redirectByUserAgent(String hash, String query, MultiMap headers) {
    var agent = uaa.parse(headers.get(HttpHeaders.USER_AGENT));
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
        .transform(
            sr -> {
              var byHash =
                  sr.stream().findFirst().orElse(new RedirectDTO().setStatusCode((short) 404));
              for (RedirectDTO r : sr) {
                if (agent
                        .getValue(UserAgent.OPERATING_SYSTEM_CLASS)
                        .toLowerCase()
                        .equals(r.getAltKey())
                    || agent
                        .getValue(UserAgent.OPERATING_SYSTEM_NAME)
                        .toLowerCase()
                        .equals(r.getAltKey())
                    || agent.getValue(UserAgent.DEVICE_CLASS).toLowerCase().equals(r.getAltKey())) {
                  byHash = r;
                }
              }
              return byHash;
            })
        .onItem()
        .transform(
            byHash -> {
              String redirectTo =
                  Objects.nonNull(byHash.getAltUrl()) ? byHash.getAltUrl() : byHash.getUrl();
              if (Objects.nonNull(query) && byHash.isForwardParameter()) {
                if (byHash.getUrl().contains("?")) {
                  redirectTo = redirectTo + "&" + query;
                } else {
                  redirectTo = redirectTo + "?" + query;
                }
              }

              byHash.setUrl(redirectTo);

              return byHash;
            })
        .onItem()
        .call(
            byHash -> {
              if (byHash.getStatusCode() >= 300 && byHash.getStatusCode() < 400) {
                AnalyticLinkEvent event =
                    AnalyticLinkEvent.builder()
                        .id(byHash.getId())
                        .headers(headers)
                        .hash(hash)
                        .build();
                bus.send(EventType.ANALYTIC_LINK, event);
              }

              return Uni.createFrom().item(byHash);
            });
  }
}

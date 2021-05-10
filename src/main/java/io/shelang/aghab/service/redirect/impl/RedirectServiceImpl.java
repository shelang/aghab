package io.shelang.aghab.service.redirect.impl;

import io.shelang.aghab.event.EventType;
import io.shelang.aghab.service.dto.RedirectDTO;
import io.shelang.aghab.service.link.LinksService;
import io.shelang.aghab.service.redirect.RedirectService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.eventbus.EventBus;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class RedirectServiceImpl implements RedirectService {

  final io.vertx.mutiny.pgclient.PgPool client;
  final LinksService linksService;
  final EventBus bus;

  public RedirectServiceImpl(PgPool client, LinksService linksService, EventBus bus) {
    this.client = client;
    this.linksService = linksService;
    this.bus = bus;
  }

  private static RedirectDTO from(Row row) {
    return new RedirectDTO()
        .setId(row.getLong("id"))
        .setUrl(row.getString("url"))
        .setStatusCode(row.getShort("redirect_code"));
  }

  @Override
  public Uni<RedirectDTO> redirectBy(String hash, String query) {
    return client
        .preparedQuery("SELECT id, url, redirect_code FROM links WHERE hash = $1 and status = 0")
        .execute(Tuple.of(hash))
        .onItem()
        .transform(RowSet::iterator)
        .onItem()
        .transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null)
        .onFailure()
        .recoverWithItem(() -> new RedirectDTO().setStatusCode((short) 404).setUrl(""))
        .onItem()
        .transform(
            byHash -> {
              String redirectTo = byHash.getUrl();
              if (Objects.nonNull(query)) {
                if (byHash.getUrl().charAt(byHash.getUrl().length() - 1) == '/')
                  redirectTo = redirectTo + query;
                else redirectTo = redirectTo + "?" + query;
              }

              byHash.setUrl(redirectTo);

              return byHash;
            })
        .onItem()
        .call(
            byHash -> {
              if (byHash.getStatusCode() >= 300 && byHash.getStatusCode() < 400)
                bus.send(EventType.ANALYTIC_LINK, byHash.getId());

              return Uni.createFrom().item(byHash);
            });
  }
}

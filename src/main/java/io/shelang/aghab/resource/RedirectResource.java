package io.shelang.aghab.resource;

import io.quarkus.vertx.web.Route;
import io.shelang.aghab.service.redirect.RedirectService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class RedirectResource {

  @Inject RedirectService redirectService;

  @PermitAll
  @Route(path = "/r/:hash", methods = HttpMethod.GET)
  public Uni<String> redirect(RoutingContext rc) {
    return redirectService
        .redirectBy(rc.request().getParam("hash"), rc.request().query())
        .onItem()
        .transform(
            byHash -> {
              rc.response()
                  .putHeader(HttpHeaders.LOCATION, byHash.getUrl())
                  .setStatusCode(byHash.getStatusCode());
              return "";
            });
  }
}

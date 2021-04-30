package io.shelang.aghab.resource;

import io.quarkus.vertx.web.Route;
import io.shelang.aghab.service.dto.LinksDTO;
import io.shelang.aghab.service.link.LinksService;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class RedirectResource {

    @Inject
    LinksService linksService;

    @Blocking
    @PermitAll
    @Route(path = "/r/:hash", methods = HttpMethod.GET)
    public void redirect(RoutingContext rc) {
        LinksDTO byHash = linksService.getByHash(
                rc.request().getParam("hash"));
        rc.response()
                .putHeader("location", byHash.getUrl())
                .setStatusCode(301)
                .end();
    }

}

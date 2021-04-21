package io.shelang.aghab.resource;

import io.quarkus.vertx.web.Route;
import io.shelang.aghab.service.dto.LinksDTO;
import io.shelang.aghab.service.link.LinksService;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Inject;

public class RedirectResource {

    @Inject
    LinksService linksService;

    @Blocking
    @Route(path = "/r/:hash", methods = HttpMethod.GET)
    public void redirect(RoutingContext ctx) {
        LinksDTO byHash = linksService.getByHash(
                ctx.request().getParam("hash"));
        ctx.response()
                .putHeader("location", byHash.getUrl())
                .setStatusCode(301)
                .end();
    }

}

package io.shelang.aghab.resource;

import io.quarkus.qute.Location;
import io.quarkus.qute.RawString;
import io.quarkus.qute.Template;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.shelang.aghab.enums.RedirectType;
import io.shelang.aghab.service.dto.RedirectDTO;
import io.shelang.aghab.service.redirect.RedirectService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import java.util.logging.Level;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import lombok.extern.java.Log;

@PermitAll
@RequestScoped
@Log
@RouteBase(path = "/r")
public class RedirectResource {

  @Inject
  RedirectService redirectService;

  @Inject
  @Location("iframe.html")
  Template iframeTemplate;

  @Inject
  @Location("script.html")
  Template scriptTemplate;

  @Route(path = "/:hash", methods = Route.HttpMethod.GET)
  public Uni<String> redirect(RoutingContext rc,
      @SuppressWarnings("unused") @Param("hash") String hash) {
    return redirectService
        .redirectBy(rc)
        .onFailure()
        .recoverWithItem(
            throwable -> {
              log.log(Level.SEVERE, throwable.getMessage(), throwable);
              return new RedirectDTO().setStatusCode((short) 404).setUrl("");
            })
        .onItem()
        .transformToUni(
            byHash -> {
              log.info(byHash.toString());
              rc.response().setStatusCode(byHash.getStatusCode());
              if (RedirectType.IFRAME.equals(byHash.getType())) {
                return Uni.createFrom()
                    .completionStage(
                        () -> iframeTemplate.data("url", byHash.getUrl()).renderAsync());
              } else if (RedirectType.SCRIPT.equals(byHash.getType())) {
                return Uni.createFrom()
                    .completionStage(
                        () ->
                            scriptTemplate
                                .data("url", byHash.getUrl())
                                .data("title", byHash.getTitle())
                                .data("timeoutInMillis", byHash.getTimeout())
                                .data("timeoutInSeconds", byHash.getTimeout() / 1000)
                                .data("redirectInMillis", byHash.getTimeout() * 0.8)
                                .data("script", new RawString(byHash.getContent()))
                                .renderAsync());
              } else { // Simple redirect
                rc.response().putHeader(HttpHeaders.LOCATION, byHash.getUrl());
                return Uni.createFrom().item("");
              }
            });
  }
}

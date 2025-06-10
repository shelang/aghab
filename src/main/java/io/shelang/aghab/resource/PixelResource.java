package io.shelang.aghab.resource;

import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.shelang.aghab.service.dto.link.RedirectDTO;
import io.shelang.aghab.service.redirect.RedirectService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.Base64;

@PermitAll
@RequestScoped
@RouteBase(path = "/i")
public class PixelResource {

  private static final byte[] TRANSPARENT_PNG =
      Base64.getDecoder()
          .decode(
              "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAJUbQhoAAAAASUVORK5CYII=");

  @Inject RedirectService redirectService;

  @Route(path = "/:hash", methods = Route.HttpMethod.GET)
  public Uni<byte[]> pixel(RoutingContext rc, @Param("hash") String hash) {
    return redirectService
        .redirectBy(rc)
        .onFailure()
        .recoverWithItem(t -> new RedirectDTO().setStatusCode((short) 404))
        .onItem()
        .transform(
            byHash -> {
              short status = byHash.getStatusCode() >= 400 ? byHash.getStatusCode() : 200;
              rc.response()
                  .setStatusCode(status)
                  .putHeader(HttpHeaders.CONTENT_TYPE, "image/png")
                  .putHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                  .putHeader("Pragma", "no-cache")
                  .putHeader(HttpHeaders.EXPIRES, "0");
              return TRANSPARENT_PNG;
            });
  }
}

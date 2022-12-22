package io.shelang.aghab.service.redirect;

import io.shelang.aghab.service.dto.link.RedirectDTO;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

public interface RedirectService {

  Uni<RedirectDTO> redirectBy(RoutingContext rc);

}

package io.shelang.aghab.service.redirect;

import io.shelang.aghab.service.dto.RedirectDTO;
import io.smallrye.mutiny.Uni;

public interface RedirectService {

    Uni<RedirectDTO> redirectBy(String hash, String query);

}

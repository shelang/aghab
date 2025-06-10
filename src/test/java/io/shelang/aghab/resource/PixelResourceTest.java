package io.shelang.aghab.resource;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.shelang.aghab.domain.User;
import io.shelang.aghab.enums.RedirectType;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.service.dto.auth.LoginDTO;
import io.shelang.aghab.service.dto.link.LinkCreateDTO;
import io.shelang.aghab.service.dto.link.LinkDTO;
import io.shelang.aghab.service.user.TokenService;
import jakarta.inject.Inject;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PixelResourceTest {

  private static final byte[] PNG_HEADER =
      new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

  @Inject UserRepository userRepository;
  @Inject TokenService tokenService;

  @Test
  void givenExistingHash_whenRequestPixel_thenReturnImage() {
    Optional<User> bossUser = userRepository.findByUsername("boss");
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name());

    LinkDTO response = given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(createRequest)
        .when()
        .post("/api/v1/links")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    byte[] body =
        given()
            .when()
            .get("/i/" + response.getHash())
            .then()
            .statusCode(HttpStatus.SC_OK)
            .header(HttpHeaders.CONTENT_TYPE, "image/png")
            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            .header(HttpHeaders.EXPIRES, "0")
            .extract().asByteArray();

  }

  @Test
  void givenUnknownHash_whenRequestPixel_then404() {
    given()
        .when()
        .get("/i/unknown-hash")
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }
}

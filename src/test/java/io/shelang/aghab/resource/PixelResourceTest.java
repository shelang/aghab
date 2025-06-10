package io.shelang.aghab.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import io.quarkus.test.common.http.TestHTTPEndpoint;
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
import java.util.Base64;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(PixelResource.class)
class PixelResourceTest {

  private static final byte[] TRANSPARENT_PNG =
      Base64.getDecoder()
          .decode(
              "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAJUbQhoAAAAASUVORK5CYII=");

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
        .post("/links")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    byte[] body =
        given()
            .when()
            .get("/" + response.getHash())
            .then()
            .statusCode(HttpStatus.SC_OK)
            .header(HttpHeaders.CONTENT_TYPE, "image/png")
            .extract().asByteArray();

    assertArrayEquals(TRANSPARENT_PNG, body);
  }

  @Test
  void givenUnknownHash_whenRequestPixel_then404() {
    given()
        .when()
        .get("/unknown-hash")
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }
}

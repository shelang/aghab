package io.shelang.aghab.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.shelang.aghab.domain.User;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.repository.WebhookRepository;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.dto.WebhookDTO;
import io.shelang.aghab.service.dto.WebhooksDTO;
import io.shelang.aghab.service.user.TokenService;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(WebhookResource.class)
class WebhookResourceTest {

  private final String bossUsername = "boss";
  private final String simpleUsername = "user";
  @Inject
  UserRepository userRepository;
  @Inject
  WebhookRepository webhookRepository;
  @Inject
  TokenService tokenService;

  @BeforeEach
  @Transactional
  void init() {
    webhookRepository.deleteAll();
    User user = new User().setUsername(simpleUsername).setPassword("");
    userRepository.persistAndFlush(user);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    userRepository.delete("username = ?1", simpleUsername);
  }

  @Test
  void givenBossUser_whenCreate_thenGetDTOById() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    WebhookDTO request = new WebhookDTO()
        .setName("awesome webhook")
        .setUrl("https://example.com");

    WebhookDTO response = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhookDTO.class);

    WebhookDTO findById = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/" + response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhookDTO.class);

    assertNotNull(response.getId());
    request.setId(response.getId());
    assertEquals(request, response);
    assertEquals(response, findById);
  }

  @Test
  void givenUser_whenCreate_thenGetDTO() {
    Optional<User> simpleUserOptional = userRepository.findByUsername(simpleUsername);
    assert simpleUserOptional.isPresent();
    LoginDTO tokens = tokenService.createTokens(simpleUserOptional.get());

    WebhookDTO request = new WebhookDTO()
        .setName("my-webhook")
        .setUrl("https://example.com");

    WebhookDTO response = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhookDTO.class);

    WebhookDTO findById = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/" + response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhookDTO.class);

    assertNotNull(response.getId());
    request.setId(response.getId());
    assertEquals(request, response);
    assertEquals(response, findById);
  }

  @Test
  void givenMoreThan50_whenSearch_thenGet50Result() {
    Optional<User> simpleUserOptional = userRepository.findByUsername(simpleUsername);
    assert simpleUserOptional.isPresent();
    LoginDTO tokens = tokenService.createTokens(simpleUserOptional.get());

    for (int i = 0; i < 51; i++) {
      WebhookDTO request = new WebhookDTO()
          .setName("my-webhook-" + i)
          .setUrl("https://exampel.com/" + i);

      given()
          .contentType(ContentType.JSON)
          .and()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
          .and()
          .body(request)
      .when()
          .post()
      .then()
          .assertThat()
          .statusCode(HttpStatus.SC_OK);
    }

    WebhooksDTO searchPage1Size60 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .param("page", 1)
        .param("size", 60)
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhooksDTO.class);

    assertEquals(50, searchPage1Size60.getWebhooks().size());

    WebhooksDTO searchPage2Size10 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .param("page", 2)
        .param("size", 10)
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhooksDTO.class);

    for (int i = 0; i < searchPage2Size10.getWebhooks().size(); i++) {
      assertTrue(searchPage2Size10.getWebhooks().get(i).getName().startsWith("my-webhook-1"));
    }

    WebhooksDTO searchByNameLike = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .param("page", 1)
        .param("name", "webhook-5")
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhooksDTO.class);

    for (int i = 0; i < searchByNameLike.getWebhooks().size(); i++) {
      assertTrue(searchByNameLike.getWebhooks().get(i).getName().contains("webhook-5"));
    }
  }

  @Test
  void givenWebhook_whenUpdateIt_thenGetUpdatedDTO() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    WebhookDTO request = new WebhookDTO()
        .setName("my-webhook")
        .setUrl("https://example.com/");

    WebhookDTO response = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhookDTO.class);

    request
        .setName("my-webhook-1")
        .setUrl("https://test.io");

    WebhookDTO updated = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(request)
    .when()
        .put("/" + response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhookDTO.class);

    assertNotNull(response.getId());
    assertNotEquals(response, updated);
    assertEquals("my-webhook-1", updated.getName());
    assertEquals("https://test.io", updated.getUrl());
  }

  @Test
  void givenBossCreatedWebhook_whenUserUpdateIt_thenThrowForbidden() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO bossTokens = tokenService.createTokens(bossUser.get());

    Optional<User> simpleUserOptional = userRepository.findByUsername(simpleUsername);
    assert simpleUserOptional.isPresent();
    LoginDTO userTokens = tokenService.createTokens(simpleUserOptional.get());

    WebhookDTO request = new WebhookDTO()
        .setName("awesome webhook")
        .setUrl("https://example.com");

    WebhookDTO response = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bossTokens.getToken())
        .and()
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WebhookDTO.class);

    request
        .setName("my-webhook-1")
        .setUrl("https://test.io");

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userTokens.getToken())
        .and()
        .body(request)
    .when()
        .put("/" + response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_FORBIDDEN);
  }
}
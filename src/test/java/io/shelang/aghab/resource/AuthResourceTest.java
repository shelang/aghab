package io.shelang.aghab.resource;

import static io.restassured.RestAssured.given;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.dto.LoginRequestDTO;
import io.shelang.aghab.service.ratelimiter.RateLimiter;
import io.shelang.aghab.util.StringUtil;
import javax.inject.Inject;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(AuthResource.class)
class AuthResourceTest {

  private final String username = "boss";
  private final String password = "!QAZ2wsx#EDC4rfv";

  @Inject
  RateLimiter rateLimiter;

  @BeforeEach
  void cleanUp() {
    rateLimiter.removeAttemptLog(username);
  }


  @Test
  void givenRegisteredUser_WhenLoginWithUsernamePassword_ThenGetAccessAndRefreshToken() {
    LoginDTO loginResponse = given()
        .contentType(ContentType.JSON)
        .and()
        .body(new LoginRequestDTO().setUsername(username).setPassword(password))
    .when()
        .post()
     .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LoginDTO.class);

    Assertions.assertTrue(StringUtil.nonNullOrEmpty(loginResponse.getToken()));
    Assertions.assertTrue(StringUtil.nonNullOrEmpty(loginResponse.getRefresh()));
  }

  @Test
  void givenRegisteredUser_WhenLoginWithUsernameAndWrongPassword_ThenGetBadRequestResponse() {
    given()
        .contentType(ContentType.JSON)
        .and()
        .body(new LoginRequestDTO().setUsername(username).setPassword("wrong-password"))
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
  }

  @Test
  void givenRegisteredUser_WhenLoginMoreThan6Time_ThenGetForbiddenResponse() {
    for (int i = 0; i < 6; i++) {
      given()
          .contentType(ContentType.JSON)
          .and()
          .body(new LoginRequestDTO().setUsername(username).setPassword(password))
      .when()
          .post()
      .then()
          .assertThat()
          .statusCode(HttpStatus.SC_OK);
    }

    given()
        .contentType(ContentType.JSON)
        .and()
        .body(new LoginRequestDTO().setUsername(username).setPassword(password))
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_TOO_MANY_REQUESTS);
  }

  @Test
  void givenUnRegisteredUser_WhenLogin_ThenGetNotFoundResponse() {
    var unregisteredUser = "unregistered.user";
    given()
        .contentType(ContentType.JSON)
        .and()
        .body(new LoginRequestDTO().setUsername(unregisteredUser).setPassword("any-password"))
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  void givenLoginUser_WhenRefreshToken_ThenGetNewTokens() {
    LoginDTO loginResponse = given()
        .contentType(ContentType.JSON)
        .and()
        .body(new LoginRequestDTO().setUsername(username).setPassword(password))
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LoginDTO.class);

    LoginDTO refreshResponse = given()
        .contentType(ContentType.JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getRefresh())
    .when()
        .post("/refresh")
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LoginDTO.class);

    Assertions.assertTrue(StringUtil.nonNullOrEmpty(refreshResponse.getToken()));
    Assertions.assertTrue(StringUtil.nonNullOrEmpty(refreshResponse.getRefresh()));
  }
}
package io.shelang.aghab.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.shelang.aghab.domain.User;
import io.shelang.aghab.enums.RedirectType;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.service.dto.link.LinkAlternativeDTO;
import io.shelang.aghab.service.dto.link.LinkCreateDTO;
import io.shelang.aghab.service.dto.link.LinkDTO;
import io.shelang.aghab.service.dto.auth.LoginDTO;
import io.shelang.aghab.service.user.TokenService;
import java.util.Arrays;
import java.util.Optional;
import jakarta.inject.Inject;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(LinksResource.class)
class LinksResourceTest {

  private final String bossUsername = "boss";

  @Inject
  UserRepository userRepository;
  @Inject
  TokenService tokenService;

  @Test
  void givenAuthUser_whenCreateRedirectLinkWithoutRedirectionCode_thenUseDefaultRedirectCodeAndCreateLink() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name());

    LinkDTO response = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    assertNotNull(response.getId());
    assertNotNull(response.getHash());
    assertEquals(createRequest.getUrl(), response.getUrl());
    assertEquals(RedirectType.REDIRECT, response.getType());
    assertEquals(301, response.getRedirectCode());
    assertEquals("http://localhost:8080/r/" + response.getHash(), response.getRedirectTo());
    assertEquals(0, response.getOs().size());
    assertEquals(0, response.getDevices().size());
    assertNull(response.getExpireAt());
    assertNull(response.getTitle());
    assertNull(response.getDescription());
  }

  @Test
  void givenAuthUser_whenCreateRedirectWithoutHttpProtocol_thenAddHttpProtocol() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("example.com")
        .setType(RedirectType.REDIRECT.name());

    LinkDTO response = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    assertNotEquals(createRequest.getUrl(), response.getUrl());
    assertTrue(response.getUrl().startsWith("http://"));
  }

  @Test
  void givenAuthUser_whenCreateRedirectLinkWithoutUndefinedURL_thenThrowBadRequest() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setType(RedirectType.REDIRECT.name());

    given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
  }

  @Test
  void givenAuthUser_whenCreateRedirectLinkAndFindItById_thenSuccessfullyGetLinkData() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name())
        .setTitle("title")
        .setDescription("desc");

    LinkDTO createResponse = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    LinkDTO getResponse = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/" + createResponse.getId())
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    createResponse.setRedirectTo(null);
    assertEquals(createResponse, getResponse);
  }

  @Test
  void givenAuthUser_whenCreateRedirectLinkAndFindItByHash_thenSuccessfullyGetLinkData() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name())
        .setTitle("title")
        .setDescription("desc");

    LinkDTO createResponse = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    LinkDTO getHashResponse = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .when()
        .get("/hash/" + createResponse.getHash())
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    createResponse.setRedirectTo(null);
    assertEquals(createResponse, getHashResponse);
  }

  @Test
  void givenAuthUser_whenCreateRedirectLinkAndFindItByIdAndHash_thenDataAreSame() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name())
        .setTitle("title")
        .setDescription("desc");

    LinkDTO createResponse = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    LinkDTO getHashResponse = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/hash/" + createResponse.getHash())
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    createResponse.setRedirectTo(null);
    assertEquals(createResponse, getHashResponse);
  }

  @Test
  void givenAuthUser_whenCreateRedirectLinkWithMoreThan150CharOfTitle_thenGetBadRequest() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name())
        .setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

    given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
  }

  @Test
  void givenAuthUser_whenCreateRedirectLinkWithNonAsciiCodeTitle_thenGetBadRequest() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name())
        .setTitle("این یک تایتل است.");

    given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK);
  }

  @Test
  void givenAuthUser_whenCreateRedirectLinkWithOsAlternative_thenGetOkay() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name())
        .setOsAlternatives(Arrays.asList(
            new LinkAlternativeDTO().setKey("android").setUrl("https://google.com"),
            new LinkAlternativeDTO().setKey("ios").setUrl("https://apple.com"))
        );

    LinkDTO response = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    assertNotNull(response.getId());
    assertNotNull(response.getHash());
    assertEquals(createRequest.getUrl(), response.getUrl());
    assertEquals(RedirectType.REDIRECT, response.getType());
    assertEquals(301, response.getRedirectCode());
    assertEquals("http://localhost:8080/r/" + response.getHash(), response.getRedirectTo());
    assertEquals(2, response.getOs().size());
    assertEquals(0, response.getDevices().size());
    assertNull(response.getExpireAt());
    assertNull(response.getTitle());
    assertNull(response.getDescription());
  }

  @Test
  void givenAuthUser_whenCreateLinkWithPreDefinedHash_thenLinkHasCreatedWithThatHash() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    String hash = "randomhashfortest";
    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setHash(hash)
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name());

    given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
    .when()
        .post()
    .then()
        .statusCode(HttpStatus.SC_OK)
        .body("hash", Matchers.equalTo(hash));
  }

  @Test
  void givenAuthUser_whenCreateLinkWithDeviceAlternative_thenLink() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    LinkCreateDTO createRequest = new LinkCreateDTO()
        .setUrl("https://example.com")
        .setType(RedirectType.REDIRECT.name())
        .setDeviceAlternatives(Arrays.asList(
            new LinkAlternativeDTO().setKey("mobile").setUrl("https://google.com"),
            new LinkAlternativeDTO().setKey("desktop").setUrl("https://apple.com"))
        );

    LinkDTO response = given()
        .contentType(ContentType.JSON).and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken()).and()
        .body(createRequest)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(LinkDTO.class);

    assertNotNull(response.getId());
    assertNotNull(response.getHash());
    assertEquals(createRequest.getUrl(), response.getUrl());
    assertEquals(RedirectType.REDIRECT, response.getType());
    assertEquals(301, response.getRedirectCode());
    assertEquals("http://localhost:8080/r/" + response.getHash(), response.getRedirectTo());
    assertEquals(0, response.getOs().size());
    assertEquals(2, response.getDevices().size());
    assertNull(response.getExpireAt());
    assertNull(response.getTitle());
    assertNull(response.getDescription());
  }
}
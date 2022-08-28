package io.shelang.aghab.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.shelang.aghab.domain.User;
import io.shelang.aghab.repository.ScriptRepository;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.dto.ScriptDTO;
import io.shelang.aghab.service.dto.ScriptsDTO;
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
@TestHTTPEndpoint(ScriptResource.class)
class ScriptResourceTest {

  private final String bossUsername = "boss";
  private final String simpleUsername = "user";
  @Inject
  UserRepository userRepository;
  @Inject
  ScriptRepository scriptRepository;
  @Inject
  TokenService tokenService;

  @BeforeEach
  @Transactional
  void init() {
    scriptRepository.deleteAll();
    User user = new User().setUsername(simpleUsername).setPassword("");
    userRepository.persistAndFlush(user);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    userRepository.delete("username = ?1", simpleUsername);
  }

  @Test
  void givenBossUser_whenCreateScript_thenGetScriptDTOById() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    ScriptDTO request = new ScriptDTO()
        .setName("my-script")
        .setContent("console.log('Hi this is a JS script!!')")
        .setTimeout(10_000);

    ScriptDTO response = given()
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
        .extract().body().as(ScriptDTO.class);

    ScriptDTO findById = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/" + response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(ScriptDTO.class);

    assertNotNull(response.getId());
    request.setId(response.getId());
    assertEquals(request, response);
    assertEquals(response, findById);
  }

  @Test
  void givenUser_whenCreateScript_thenGetScriptDTO() {
    Optional<User> simpleUserOptional = userRepository.findByUsername(simpleUsername);
    assert simpleUserOptional.isPresent();
    LoginDTO tokens = tokenService.createTokens(simpleUserOptional.get());

    ScriptDTO request = new ScriptDTO()
        .setName("my-script")
        .setContent("console.log('Hi this is a JS script!!')")
        .setTimeout(10_000);

    ScriptDTO response = given()
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
        .extract().body().as(ScriptDTO.class);

    ScriptDTO findById = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/" + response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(ScriptDTO.class);

    assertNotNull(response.getId());
    request.setId(response.getId());
    assertEquals(request, response);
    assertEquals(response, findById);
  }

  @Test
  void givenMoreThan50Scripts_whenSearch_thenGetScriptResult() {
    Optional<User> simpleUserOptional = userRepository.findByUsername(simpleUsername);
    assert simpleUserOptional.isPresent();
    LoginDTO tokens = tokenService.createTokens(simpleUserOptional.get());

    for (int i = 0; i < 51; i++) {
      ScriptDTO request = new ScriptDTO()
          .setName("my-script-" + i)
          .setContent("console.log('Hi this is a JS script # " + i +" !!')")
          .setTimeout(10_000);

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

    ScriptsDTO searchPage1Size60 = given()
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
        .extract().body().as(ScriptsDTO.class);

    assertEquals(50, searchPage1Size60.getScripts().size());

    ScriptsDTO searchPage2Size10 = given()
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
        .extract().body().as(ScriptsDTO.class);

    for (int i = 0; i < searchPage2Size10.getScripts().size(); i++) {
      assertTrue(searchPage2Size10.getScripts().get(i).getName().startsWith("my-script-1"));
    }

    ScriptsDTO searchByNameLike = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .param("page", 1)
        .param("name", "script-5")
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(ScriptsDTO.class);

    for (int i = 0; i < searchByNameLike.getScripts().size(); i++) {
      assertTrue(searchByNameLike.getScripts().get(i).getName().contains("script-5"));
    }
  }

  @Test
  void givenScript_whenUpdateIt_thenGetUpdatedScriptDTO() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    ScriptDTO request = new ScriptDTO()
        .setName("my-script")
        .setContent("console.log('Hi this is a JS script!!')")
        .setTimeout(10_000);

    ScriptDTO response = given()
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
        .extract().body().as(ScriptDTO.class);

    request
        .setTitle("My JS Script")
        .setName("my-script-1")
        .setContent("console.log('Hi this is a updated JS script!!')")
        .setTimeout(5_000);

    ScriptDTO updatedScript = given()
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
        .extract().body().as(ScriptDTO.class);

    assertNotNull(response.getId());
    assertNotEquals(response, updatedScript);
    assertEquals("My JS Script", updatedScript.getTitle());
    assertEquals("my-script-1", updatedScript.getName());
    assertEquals("console.log('Hi this is a updated JS script!!')", updatedScript.getContent());
    assertEquals(5_000, updatedScript.getTimeout());
  }

}
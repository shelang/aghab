package io.shelang.aghab.resource;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.shelang.aghab.domain.User;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.service.dto.LoginDTO;
import io.shelang.aghab.service.dto.UserCredentialDTO;
import io.shelang.aghab.service.dto.UserDTO;
import io.shelang.aghab.service.dto.UserMeDTO;
import io.shelang.aghab.service.dto.UsersDTO;
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
@TestHTTPEndpoint(UserResource.class)
class UserResourceTest {

  private final String bossUsername = "boss";
  @Inject
  UserRepository userRepository;
  @Inject
  TokenService tokenService;

  @BeforeEach
  @AfterEach
  @Transactional
  void init() {
    userRepository.delete("username != 'boss'");
  }

  @Test
  void givenBossUser_whenCreateUser_thenGetUserDTO() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UserCredentialDTO request = new UserCredentialDTO()
        .setUsername("new-user-1")
        .setPassword("very-Strong-p@sz");

    UserDTO response = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    assertNotNull(response.getId());
    assertEquals(request.getUsername(), response.getUsername());
  }

  @Test
  void givenNonBossUser_whenCreateUser_thenGetForbidden() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UserCredentialDTO request = new UserCredentialDTO()
        .setUsername("new-user-1")
        .setPassword("very-Strong-p@sz");

    UserDTO createUser1Response = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    LoginDTO user1Tokens = tokenService
        .createTokens(new User().setId(createUser1Response.getId()).setUsername(
            createUser1Response.getUsername()));

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + user1Tokens.getToken())
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_FORBIDDEN);
  }

  @Test
  void givenAUser_whenRequestToGenerateAPIToken_thenGetUserDetailWithAPIToken() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UserMeDTO generateAPITokenResponse = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .post("/api-token")
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserMeDTO.class);

    UserMeDTO getMeResponse = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/me")
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserMeDTO.class);

    assertNotNull(generateAPITokenResponse.getToken());
    assertNotNull(getMeResponse.getToken());
    assertEquals(generateAPITokenResponse.getToken(), getMeResponse.getToken());
  }

  @Test
  void givenBossUser_whenGetExistedUserById_thenGetUserDTO() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UserCredentialDTO request = new UserCredentialDTO()
        .setUsername("new-user-1")
        .setPassword("very-Strong-p@sz");

    UserDTO user1 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    UserDTO user1ById = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/"+user1.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    assertEquals(user1, user1ById);
  }

  @Test
  void givenNonBossUser_whenUpdateProfile_thenGetUpdatedUserDTO() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UserCredentialDTO request = new UserCredentialDTO()
        .setUsername("new-user-1")
        .setPassword("very-Strong-p@sz");

    UserDTO user1 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    LoginDTO user1Tokens = tokenService
        .createTokens(new User().setId(user1.getId()).setUsername(user1.getUsername()));

    UserCredentialDTO user1UpdateRequest = new UserCredentialDTO()
        .setId(user1.getId())
        .setUsername("user1")
        .setPassword("new-password");

    UserDTO updatedUser1response = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + user1Tokens.getToken())
        .body(user1UpdateRequest)
    .when()
        .put("/" + user1.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);
    assertEquals(user1UpdateRequest.getUsername(), updatedUser1response.getUsername());
  }

  @Test
  void givenNonBossUser_whenUpdateSomeoneElseProfile_thenGetForbidden() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UserCredentialDTO request = new UserCredentialDTO()
        .setUsername("new-user-1")
        .setPassword("very-Strong-p@sz");

    UserDTO user1 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(request)
        .when()
        .post()
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    LoginDTO user1Tokens = tokenService
        .createTokens(new User().setId(user1.getId()).setUsername(user1.getUsername()));

    UserCredentialDTO user1UpdateRequest = new UserCredentialDTO()
        .setId(1L)
        .setUsername("me")
        .setPassword("hack-your-account :)");

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + user1Tokens.getToken())
        .body(user1UpdateRequest)
    .when()
        .put("/" + 1)
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_FORBIDDEN);
  }

  @Test
  void givenBossUser_whenUpdateSomeoneElseProfile_thenGetUserDTO() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UserCredentialDTO request = new UserCredentialDTO()
        .setUsername("new-user-1")
        .setPassword("very-Strong-p@sz");

    UserDTO user1 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(request)
    .when()
        .post()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    UserCredentialDTO user1UpdateRequest = new UserCredentialDTO()
        .setId(user1.getId())
        .setUsername("user-1")
        .setPassword("reset-your-password");

    UserDTO updatedUser = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .body(user1UpdateRequest)
        .when()
        .put("/" + user1.getId())
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UserDTO.class);

    assertEquals(user1.getId(), updatedUser.getId());
    assertNotEquals(user1.getUsername(), updatedUser.getUsername());
    assertEquals("user-1", updatedUser.getUsername());
  }

  @Test
  void givenRegisteredUsers_whenBossUserSearchWithAndWithoutPagination_thenGetListOfUsers() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    for (int i = 0; i < 20; i++) {
      UserCredentialDTO request = new UserCredentialDTO()
          .setUsername("user-" + i)
          .setPassword("very-Strong-p@sz" + i);

      given()
          .contentType(ContentType.JSON)
          .and()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
          .body(request)
      .when()
          .post()
      .then()
          .assertThat()
          .statusCode(HttpStatus.SC_OK);
    }

    UsersDTO searchResponseDefaultPage = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(10, searchResponseDefaultPage.getUsers().size());
    assertEquals("boss", searchResponseDefaultPage.getUsers().get(0).getUsername());
    for (int i = 1; i < 10; i++) {
      assertEquals("user-" + (i-1), searchResponseDefaultPage.getUsers().get(i).getUsername());
    }

    UsersDTO searchResponsePage1Size5 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .param("page", "1")
        .param("size", "5")
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(5, searchResponsePage1Size5.getUsers().size());
    assertEquals("boss", searchResponsePage1Size5.getUsers().get(0).getUsername());
    for (int i = 1; i < 5; i++) {
      assertEquals("user-" + (i-1), searchResponsePage1Size5.getUsers().get(i).getUsername());
    }

    UsersDTO searchResponsePage2Size5 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .param("page", "2")
        .param("size", "5")
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(5, searchResponsePage2Size5.getUsers().size());
    for (int i = 5; i < 10; i++) {
      assertEquals("user-" + (i-1), searchResponsePage2Size5.getUsers().get(i-5).getUsername());
    }

    UsersDTO searchResponseQueryByUsernameSingle = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .param("username", "user-15")
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(1, searchResponseQueryByUsernameSingle.getUsers().size());
    assertEquals("user-15", searchResponseQueryByUsernameSingle.getUsers().get(0).getUsername());
  }

  @Test
  void givenBossUser_whenSearchANonExistUsername_thenGetEmptyList() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    UsersDTO searchResponseQueryByNonExistUsername = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .param("username", "user-")
        .param("page", "1")
        .when()
        .get()
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(0, searchResponseQueryByNonExistUsername.getUsers().size());
  }

  @Test
  void givenTableWithMoreThan50Users_whenSearchWithSizeBiggerThan50_thenInResponseJustReturn50Users() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    for (int i = 0; i < 51; i++) {
      UserCredentialDTO request = new UserCredentialDTO()
          .setUsername("user-" + i)
          .setPassword("very-Strong-p@sz" + i);

      given()
          .contentType(ContentType.JSON)
          .and()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
          .body(request)
          .when()
          .post()
          .then()
          .assertThat()
          .statusCode(HttpStatus.SC_OK);
    }

    UsersDTO searchResponsePage1Size60 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .param("size", "60")
    .when()
        .get()
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(50, searchResponsePage1Size60.getUsers().size());
  }
}
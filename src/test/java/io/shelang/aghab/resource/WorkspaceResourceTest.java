package io.shelang.aghab.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.shelang.aghab.domain.User;
import io.shelang.aghab.repository.UserRepository;
import io.shelang.aghab.repository.WorkspaceRepository;
import io.shelang.aghab.repository.WorkspaceUserRepository;
import io.shelang.aghab.service.dto.auth.LoginDTO;
import io.shelang.aghab.service.dto.auth.UsersDTO;
import io.shelang.aghab.service.dto.workspace.MembersRequest;
import io.shelang.aghab.service.dto.workspace.WorkspaceDTO;
import io.shelang.aghab.service.dto.workspace.WorkspacesDTO;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.util.PageUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(WorkspaceResource.class)
class WorkspaceResourceTest {

  private final String bossUsername = "boss";
  private final String simpleUsername = "user";

  @Inject
  UserRepository userRepository;
  @Inject
  WorkspaceRepository workspaceRepository;
  @Inject
  TokenService tokenService;
  @Inject
  WorkspaceUserRepository workspaceUserRepository;

  @BeforeEach
  @Transactional
  void init() {
    workspaceUserRepository.deleteAll();
    workspaceRepository.deleteAll();
    List<User> simpleUser = userRepository.search(simpleUsername, PageUtil.of(1, 1));
    if (simpleUser.isEmpty()) {
      User user = new User().setUsername(simpleUsername).setPassword("");
      userRepository.persistAndFlush(user);
    }
  }

  @Test
  void givenBossUser_whenCreateWorkspace_thenGetDTOById() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    WorkspaceDTO request = new WorkspaceDTO().setName("My workspace");

    WorkspaceDTO response = given()
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
        .extract().body().as(WorkspaceDTO.class);

    WorkspaceDTO findById = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/{id}", response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WorkspaceDTO.class);

    assertNotNull(response.getId());
    request.setId(response.getId());
    assertEquals(request, response);
    assertEquals(response, findById);
  }

  @Test
  void givenWorkspace_whenAddMember_thenListOfMemberShowTheChange() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    WorkspaceDTO request = new WorkspaceDTO().setName("My workspace");

    WorkspaceDTO response = given()
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
        .extract().body().as(WorkspaceDTO.class);

    List<User> users = userRepository.search(simpleUsername, PageUtil.of(1, 1));

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(new MembersRequest().setMemberIds(Collections.singletonList(users.get(0).getId())))
    .when()
        .post("/{id}/members", response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);

    UsersDTO members = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/{id}/members", response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(2, members.getUsers().size());
  }

  @Test
  void givenWorkspaceWith2Members_whenDeleteMember_thenGroupHas1Member() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    WorkspaceDTO request = new WorkspaceDTO().setName("My workspace");

    WorkspaceDTO response = given()
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
        .extract().body().as(WorkspaceDTO.class);

    List<User> users = userRepository.search(simpleUsername, PageUtil.of(1, 1));

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(new MembersRequest().setMemberIds(Collections.singletonList(users.get(0).getId())))
    .when()
        .post("/{id}/members", response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);

    UsersDTO addedMembersResponse = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/{id}/members", response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(2, addedMembersResponse.getUsers().size());

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(new MembersRequest().setMemberIds(Collections.singletonList(users.get(0).getId())))
    .when()
        .delete("/{id}/members", response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);

    UsersDTO getMembersResponse = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/{id}/members", response.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(UsersDTO.class);

    assertEquals(1, getMembersResponse.getUsers().size());
  }

  @Test
  void givenWorkspaceWith2Members_whenDeleteWorkspace_thenGetWorkspaceReturn403Forbidden() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    WorkspaceDTO request = new WorkspaceDTO().setName("My workspace");

    WorkspaceDTO workspace = given()
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
        .extract().body().as(WorkspaceDTO.class);

    List<User> users = userRepository.search(simpleUsername, PageUtil.of(1, 1));

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(new MembersRequest().setMemberIds(Collections.singletonList(users.get(0).getId())))
    .when()
        .post("/{id}/members", workspace.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .delete("/{id}", workspace.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);

    given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
    .when()
        .get("/{id}", workspace.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_FORBIDDEN);
  }

  @Test
  void givenWorkspace_whenUpdateItsName_thenGetOkay() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    WorkspaceDTO request = new WorkspaceDTO().setName("My workspace");

    WorkspaceDTO workspace = given()
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
        .extract().body().as(WorkspaceDTO.class);

    WorkspaceDTO update = new WorkspaceDTO().setName("My workspace updated");

    WorkspaceDTO updated = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .body(update)
    .when()
        .put("/{id}", workspace.getId())
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WorkspaceDTO.class);

    assertEquals(workspace.getId(), updated.getId());
    assertNotEquals(workspace.getName(), updated.getName());
    assertEquals("My workspace updated", updated.getName());
  }

  @Test
  void givenWorkspaces_whenSearchThemByName_thenGetListOfWorkspaceByName() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    for (int i = 0; i < 5; i++) {
      WorkspaceDTO request = new WorkspaceDTO().setName("My workspace #" + i);

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

    WorkspacesDTO searched = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .param("q", "My workspace #3")
    .when()
        .get("/")
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WorkspacesDTO.class);

    assertEquals(1, searched.getWorkspaces().size());
    assertEquals("My workspace #3", searched.getWorkspaces().get(0).getName());
  }

  @Test
  void givenWorkspaces_whenGetDifferentPage_thenSeeScrolledListData() {
    Optional<User> bossUser = userRepository.findByUsername(bossUsername);
    assert bossUser.isPresent();
    LoginDTO tokens = tokenService.createTokens(bossUser.get());

    for (int i = 0; i < 25; i++) {
      WorkspaceDTO request = new WorkspaceDTO().setName("My workspace #" + i);

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

    WorkspacesDTO page3WithSize5 = given()
        .contentType(ContentType.JSON)
        .and()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getToken())
        .and()
        .param("page", 3)
        .param("size", 5)
    .when()
        .get("/")
    .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .extract().body().as(WorkspacesDTO.class);

    assertEquals(5, page3WithSize5.getWorkspaces().size());
    for (int i = 0; i < page3WithSize5.getWorkspaces().size(); i++) {
      assertTrue(page3WithSize5.getWorkspaces().get(i).getName().startsWith("My workspace #1"));
    }
  }
}
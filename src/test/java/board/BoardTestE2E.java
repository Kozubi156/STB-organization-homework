package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Board;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Names;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class BoardTestE2E extends BaseTest {

    @BeforeEach
    public void beforeEach() {
        board = new Board();
        board.setName(faker.lorem().word());
    }

    @Test
    public void createNewBoard() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", board.getName())
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();


        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo(board.getName());

        String boardId = json.get("id");

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + BOARDS + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createBoardWithEmptyBoardName() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();
    }

    @Test
    public void createBoardWithoutDefaultLists() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", board.getName())
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo(board.getName());

        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + BOARDS + boardId + "/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath jsonGet = responseGet.jsonPath();
        List<String> idList = jsonGet.getList("id");
        assertThat(idList).hasSize(0);

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + BOARDS + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createBoardWithDefaultLists() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", board.getName())
                .queryParam("defaultLists", true)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo(board.getName());

        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + BOARDS + boardId + "/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath jsonGet = responseGet.jsonPath();
        List<String> nameList = jsonGet.getList("name");

        assertThat(nameList).hasSize(3).contains(Names.DEFAULT_LIST_NAME_1, Names.DEFAULT_LIST_NAME_2,
                Names.DEFAULT_LIST_NAME_3);

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + BOARDS + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}

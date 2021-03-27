package organization;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Utils;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateNewOrganizationTest extends BaseTest {

    @BeforeEach
    public void beforeEach() {
        organization.setDisplayName(faker.lorem().word());
        organization.setDescription(faker.lorem().paragraph());
        organization.setName(faker.lorem().characters(4, 10, false, false));
        organization.setWebsite(faker.internet().url());
    }

    @Test
    public void createNewOrganizationWithAllAvailableFileds() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", organization.getDisplayName())
                .queryParam("desc", organization.getDescription())
                .queryParam("name", organization.getName())
                .queryParam("website", Utils.HTTPS_PREFIX + organization.getWebsite())
                .when()
                .post(BASE_URL + ORGANIZATION)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(organization.getDisplayName());
        assertThat(json.getString("desc")).isEqualTo(organization.getDescription());
        assertThat(json.getString("name")).isEqualTo(organization.getName());
        assertThat(json.getString("website")).isEqualTo(Utils.HTTPS_PREFIX + organization.getWebsite());
    }

    @Test
    public void createNewOrganizationWithoutDisplayName() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("desc", organization.getDescription())
                .queryParam("name", organization.getName())
                .queryParam("website", Utils.HTTPS_PREFIX + organization.getWebsite())
                .when()
                .post(BASE_URL + ORGANIZATION)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("error")).isEqualTo(Utils.ERROR);
        assertThat(json.getString("message")).isEqualTo(Utils.ERROR_MESSAGE_DISPLAY_NAME);
//        assertThat(json).doesNotHaveJsonPathValue("")
    }

    @Test
    public void createNewOrganizationOnlyWithDisplayNameFiled() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", organization.getDisplayName())
                .when()
                .post(BASE_URL + ORGANIZATION)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(organization.getDisplayName());
        assertThat(json.getString("desc")).isEmpty();
        assertThat(json.getString("name")).isNotNull();
        assertThat(json.getString("website")).isNull();
    }

}

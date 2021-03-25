package organization;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateNewOrganizationTest extends BaseTest {

    @BeforeEach
    public void beforeEach() {
        organization.setDisplayName(faker.lorem().sentence());
        organization.setDescription(faker.lorem().paragraph());
        organization.setName(faker.lorem().sentence());
        organization.setWebsite(faker.internet().url());
    }

    @Test
    public void createNewOrganizationWithAllAvailableFileds(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", organization.getDisplayName())
                .queryParam("desc", organization.getDescription())
                .queryParam("name", organization.getName())
                .queryParam("website", organization.getWebsite())
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
        assertThat(json.getString("website")).isEqualTo(organization.getWebsite());
    }
}

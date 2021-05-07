package organization;

import base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Organization;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.DeleteOrganization;
import utils.Names;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateOrganizationTest extends BaseTest {

    @BeforeEach
    public void beforeEach() {

        organization = new Organization();
        organization.setDisplayName(faker.lorem().word());
        organization.setDescription(faker.lorem().paragraph());
        organization.setName(faker.lorem().characters(4, 10, false, false));
        organization.setWebsite(faker.internet().url());

    }


    @Test
    public void createNewOrganizationWithAllAvailableFileds() {

        Response response = given()
                .contentType(ContentType.JSON)
                .spec(reqSpec)
                .queryParam("displayName", organization.getDisplayName())
                .queryParam("desc", organization.getDescription())
                .queryParam("name", organization.getName())
                .queryParam("website", Names.HTTPS_PREFIX + organization.getWebsite())
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
        assertThat(json.getString("website")).isEqualTo(Names.HTTPS_PREFIX + organization.getWebsite());

        final String organizationId = json.getString("id");

        DeleteOrganization.deleteOrganization(reqSpec, BASE_URL, ORGANIZATION, organizationId);
    }


    @Test
    public void createNewOrganizationWithoutDisplayName() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("desc", organization.getDescription())
                .queryParam("name", organization.getName())
                .queryParam("website", Names.HTTPS_PREFIX + organization.getWebsite())
                .when()
                .post(BASE_URL + ORGANIZATION)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("error")).isEqualTo(Names.ERROR);
        assertThat(json.getString("message")).isEqualTo(Names.ERROR_MESSAGE_DISPLAY_NAME);
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

        final String organizationId = json.getString("id");

        DeleteOrganization.deleteOrganization(reqSpec, BASE_URL, ORGANIZATION, organizationId);
    }


    @DisplayName("Create orgnization with valid data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("utils.OrganizationData#createOrganizationValidData")
    public void createNewOrganizationWithValidParams(String displayName, String desc, String name, String website) {

        Response response = given()
                .contentType(ContentType.JSON)
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATION)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayName);
        assertThat(json.getString("desc")).isEqualTo(desc);
        assertThat(json.getString("name")).isEqualTo(name);
        assertThat(json.getString("website")).isEqualTo(website);

        final String organizationId = json.getString("id");
        DeleteOrganization.deleteOrganization(reqSpec, BASE_URL, ORGANIZATION, organizationId);
    }


    @DisplayName("Create orgnization with invalid data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("utils.OrganizationArguments#createOrganizationInvalidData")
    public void createNewOrganizationWithInvalidParams(String displayName, String desc, String name, String website) {

        Response response = given()
                .contentType(ContentType.JSON)
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATION)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();


        JsonPath json = response.jsonPath();
        System.out.println(json.prettyPrint());
        assertThat(json.getString("displayName")).isEqualTo(displayName);
        assertThat(json.getString("desc")).isEqualTo(desc);
        assertThat(json.getString("name").length()).isGreaterThan(3);
        assertThat(json.getString("website")).contains(Names.HTTP_PREFIX);

        final String organizationId = json.getString("id");
        DeleteOrganization.deleteOrganization(reqSpec, BASE_URL, ORGANIZATION, organizationId);
    }

}

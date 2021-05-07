package utils;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class DeleteOrganization {

    public static void deleteOrganization(RequestSpecification reqSpec, String BASE_URL, String ORGANIZATION,
                                    String organizationId){

        given()
                .contentType(ContentType.JSON)
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATION + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}

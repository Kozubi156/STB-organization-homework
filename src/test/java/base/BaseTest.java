package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.Organization;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String ORGANIZATION = "organizations/";

    protected static final String KEY = "69f4863785d85042c966803ae8c46d3e";
    protected static final String TOKEN = "bdefc2ba052f1061b37c83473873039c9615d6f2c69c014d9ec62a76a302a498";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    public static Faker faker;
    public static Organization organization;


    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);
        reqSpec = reqBuilder.build();

        organization = new Organization();
        faker = new Faker();
    }
}

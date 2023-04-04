package ru.eshoprzd.api;

import com.codeborne.selenide.Selenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import ru.eshoprzd.config.AuthConfig;

import java.util.HashMap;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.http.ContentType.JSON;


public class TestAuth {


    @Test
    public void authTestEshop() {
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        HashMap<String, Object> body = new HashMap<>();
        body.put("login", config.login());
        body.put("password", config.password());

        var response = RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(JSON)
                .header("type-shop", "rzd")
                .body(body)
                .log().uri().log().body()
                .post("https://eshoprzd.ru/rest/auth/api/user/login")
                .then()
                .log().body()
                .extract()
                .response();

        var token = response.jsonPath().get("result.tokenId").toString();

        open("https://eshoprzd.ru/home");
        Selenide.sessionStorage().setItem("key", token);
        Selenide.refresh();
        $("[title='Общая учетка УТП']").should(appear);

    }
}

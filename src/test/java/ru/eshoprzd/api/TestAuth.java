package ru.eshoprzd.api;

import com.codeborne.selenide.Selenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.StringTokenizer;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.http.ContentType.JSON;


public class TestAuth {

//    {"login":"10012206","password":"cgjrjggbc8"}


    @Test
    public void authTestEshop() {
        HashMap<String,Object> body=new HashMap<>();
        body.put("login","10012206");
        body.put("password","cgjrjggbc8");

        var response = RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(JSON)
                .header("type-shop","rzd")
                .body(body)
                .log().uri().log().body()
                .post("https://eshoprzd.ru/rest/auth/api/user/login")
                .then()
                .log().body()
                .extract()
                .response();

        var token = response.jsonPath().get("result.tokenId").toString();

        open("https://eshoprzd.ru/home");
        Selenide.sessionStorage().setItem("key",token);
        Selenide.refresh();

        $("[title='Общая учетка УТП']").should(appear);




    }


}

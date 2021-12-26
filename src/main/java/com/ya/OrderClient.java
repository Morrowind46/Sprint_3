package com.ya;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "/api/v1/orders";

    @Step ("Создание заказа")
    public ValidatableResponse create(Order order){
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step ("Получениме списка заказов`")
    public ValidatableResponse orderList (){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
//
//    @Step
//    public ValidatableResponse cancel (String trackId){
//        return given()
//                .spec(getBaseSpec())
//                .when()
//                .delete(ORDER_PATH + "cancel/")
//                .then();
//    }
}
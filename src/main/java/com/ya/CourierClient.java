package com.ya;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class CourierClient extends RestAssuredClient{

    private static final String COURIER_PATH = "/api/v1/courier/";

    @Step ("Создать курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec ())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step ("Логин курьера в системе")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec (getBaseSpec())
                .body (credentials)
                .when ()
                .post (COURIER_PATH + "login")
                .then ();
    }

    @Step ("Удалить курьера")
    public boolean delete(int courierID) {
        return given ()
                .spec (getBaseSpec ())
                .when ()
                .delete (COURIER_PATH+courierID)
                .then ()
                .assertThat ()
                .statusCode (SC_OK) // тоже самое, что 200
                .extract ()
                .path ("ok");
    }
}
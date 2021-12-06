import com.ya.Courier;
import com.ya.CourierClient;
import com.ya.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CourierCreateTest {

    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandom ();
        courierClient = new CourierClient ();
    }

    @After
    public void  tearDown() {
        courierClient.delete (courierId);
    }

    @Test
    @DisplayName("Курьера можно создать")
    @Description("успешный запрос возвращает ok: true")
    public void checkCourierCanBeCreated () {
    //arrange, act, assert

    //Act
    ValidatableResponse response = courierClient.create(courier);
    int statusCode = response.extract().statusCode();
    boolean isCourierCreated = response.extract().path("ok");
    courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
    //без паттерна "фабичный метод": courierId = courierClient.login(new CourierCredentials(courier.login, courier.password)).extract().path("id");

    //Assert
    assertThat(statusCode, equalTo(201));
    assertTrue(isCourierCreated);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("усли создать пользователя с логином, который уже есть, возвращается ошибка")
    public void duplicateCourierCannotBeCreated() {

        courierClient.create(courier);

        ValidatableResponse negativeResponse = courierClient.create(courier);

        int statusCodeNegativeResponse = negativeResponse.extract().statusCode();
        String errorMessage = negativeResponse.extract().path("message");

        assertThat("Error status code", statusCodeNegativeResponse, equalTo(409));
        assertThat("Wrong body - massage", errorMessage, (equalTo("Этот логин уже используется")));
    }
}

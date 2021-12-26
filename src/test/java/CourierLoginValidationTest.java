import com.ya.Courier;
import com.ya.CourierClient;
import com.ya.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierLoginValidationTest {

    private static CourierClient courierClient = new CourierClient ();
    private static Courier courier = Courier.getRandom ();
    private int courierId;

    private final CourierCredentials courierCredentials;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public CourierLoginValidationTest(CourierCredentials courierCredentials, int expectedStatus, String expectedErrorMessage) {
        this.courierCredentials = courierCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

@Parameterized.Parameters
public static Object[][] getTestData() {
        return new Object[][]{
        {CourierCredentials.getCredentialsWithPasswordOnly (courier), 400, "Недостаточно данных для входа"},
        {CourierCredentials.getCredentialsWithLoginOnly (courier), 400, "Недостаточно данных для входа"},
        {CourierCredentials.getCredentialsWithRandomLogin (courier), 404 , "Учетная запись не найдена"},
        {CourierCredentials.getCredentialsWithRandomPassword (courier), 404 , "Учетная запись не найдена"},
        };
}

    @After
    public void  tearDown() {
        courierClient.delete (courierId);
    }

    @Test
    @DisplayName ("Для авторизации нужно передать все обязательные поля")
    @Description ("1. если нет поля Логин, запрос возвращает ошибку" +
                  "2. если нет поля Пароль, запрос возвращает ошибку" +
                  "3. система вернёт ошибку, если неправильно указать логин" +
                  "4. система вернёт ошибку, если неправильно указать пароль" )
    public void CourierLoginValidationTest () {

        //Arrange
        courierClient.create(courier);
        ValidatableResponse login = new CourierClient().login(courierCredentials);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
        //Act
        int ActualStatusCode = login.extract ().statusCode();
        //Assert
        assertEquals ("Status code is incorrect",expectedStatus, ActualStatusCode);
        String actualMessage = login.extract ().path ("message");
        assertEquals ("Courier ID is incorrect", expectedErrorMessage, actualMessage);
    }
}

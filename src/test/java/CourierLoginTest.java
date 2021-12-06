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

public class CourierLoginTest {

    private Courier courier;
    private CourierClient courierClient;
    private int courierId;


    @Before
    public void setUp() {
        courierClient = new CourierClient ();
        courier = Courier.getRandom ();
    }

    @After
    public void  tearDown() {
        courierClient.delete (courierId);
    }

    @Test
    @DisplayName ("Курьер может авторизоваться")
    @Description ("успешный запрос возвращает id")
    public void courierCanLogIn () {

        //Act
        courierClient.create(courier);
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        //без паттерна "фабичный метод":  ValidatableResponse response = courierClient.login(new CourierCredentials(courier.login, courier.password));
        courierId = response.extract ().path ("id");
        int statusCodeSuccessfulLogin = response.extract ().statusCode();
        //Assert
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
        assertThat(statusCodeSuccessfulLogin, equalTo(200));
    }
}

import com.ya.Order;
import com.ya.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(Parameterized.class)

public class CreateOrderTest {

    private final List<String> color;

    public CreateOrderTest (List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColorType () {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK","GREY")},
                {List.of()}

        };
    }

    @Test
    @DisplayName ("Проверка, что заказ может быть создан")
    @Description ("1. со значением ключа color: BLACK " +
                  "2. со значение ключа color: GREY " +
                  "3. со значение ключа color: BLACK,GREY " +
                  "4. с пустым значение ключа color ")
    public void orderCanBeCreatedTest () {

        Order order = Order.setColor(color);
        OrderClient orderClient = new OrderClient();

        //Создание заказа
        ValidatableResponse response = orderClient.create(order);

        int statusCode = response.extract().statusCode();
        int trackId = response.extract().path("track");

        assertThat ("Status code is incorrect", statusCode, equalTo(201));
        assertThat("Track number not created", trackId, is(not(0)));
    }
}
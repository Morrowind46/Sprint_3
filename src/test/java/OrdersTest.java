import com.ya.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;

public class OrdersTest {

    @Test
    @DisplayName ("Проверить, что в тело ответа возвращается список заказов")
    @Description ("список заказов не пустой")
    public void getOrdersReturnsTest() {
        OrderClient ordersClient = new OrderClient();

        // Задача проверить, что этот метод возвращает список ордеров
        ValidatableResponse response = ordersClient.orderList();

        response.assertThat().body ("orders.size()", is(not (0)));

//        //Другой метод:
//        //Получаем список
//        List<Object> orders = response.extract ().jsonPath ().getList ("orders");
//        //Делаем asserts
//        assertFalse(orders.isEmpty());
    }
}

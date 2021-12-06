import com.ya.Courier;
import com.ya.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith (Parameterized.class)
public class CourierCreateRequestValidationTest {

    private final Courier courier;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public CourierCreateRequestValidationTest(Courier courier, int expectedStatus, String expectedErrorMessage) {
        this.courier = courier;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }


    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {Courier.getWithLoginOnly (), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getWithPasswordOnly(), 400, "Недостаточно данных для создания учетной записи"},
                // Если не хотим делать такие методы (напр: getWithPasswordOnly()). Можно делать конструктором:
                // new Courier().setPassword(какой-то password), 400, "Error"}
                // Но лучше генерировать тестовые данные в специальных классах отдельных от тестов. Это позволяет проще изменять подход к генерации данных

                // Можно добавить еще один тест:
                // {Courier.getWithPasswordAndLogin(), 201, null} // после него нужно будет почистить
        };
    }

    @Test
    @DisplayName ("Чтобы создать курьера, нужно передать в ручку все обязательные поля")
    @Description ("если одного из полей нет, запрос возвращает ошибку")
    public void invalidRequestIsNotAllowed() {

        ValidatableResponse response = new CourierClient ().create(courier);
        // Цель: провреить, что нельзя создать курьера с невалидным реквестом
        // Создаем курьера только с логином, делаем запрос, проверяем, что создание не состоялось

        String actualMessage = response.extract ().path ("message");
        int code = response.extract ().path ("code");

        // Asserts
        // Можно так:
        // assert actualMessage == expectedErrorMessage;
        // Или так:
        assertEquals (expectedErrorMessage, actualMessage);
        assertEquals (expectedStatus, code);

    }
}

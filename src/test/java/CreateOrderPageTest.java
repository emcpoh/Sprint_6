import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.stream.Stream;

import pages.CreateOrderPage;

public class CreateOrderPageTest {
    private WebDriver driver;
    private CreateOrderPage createOrderPage;


    @BeforeEach
    public void setUp() {
        //FirefoxOptions options = new FirefoxOptions();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");

        //this.driver = new FirefoxDriver(options);
        this.driver = new ChromeDriver(options);

        this.createOrderPage = new CreateOrderPage(this.driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest
    @MethodSource("createOrderProvider")
    public void createOrderTest(
            String name, String surname, String address, String metroStation,String phoneNumber,
            String rentDate, String rentDuration, String color, String comment
    ) {
        createOrderPage.fillCreateOrderForm(
                name, surname, address, metroStation, phoneNumber,
                rentDate, rentDuration, color, comment
        );

        createOrderPage.assertOrderCreationInfo();
    }

    private static Stream<Arguments> createOrderProvider() {
        return Stream.of(
                Arguments.of(
                        "Иван", "Иванов", "ул. Пушкина, д.10", "Комсомольская", "+79260000001",
                        "10.06.2025", "сутки", "чёрный жемчуг", "Позвоните за 10 минут"
                ),
                Arguments.of(
                        "Петр", "Петров", "пр. Ленина, д.5", "Авиамоторная", "+79260000002",
                        "15.06.2025", "семеро суток", "серая безысходность", ""
                )
        );
    }
}
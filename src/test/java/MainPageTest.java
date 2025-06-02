import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import pages.MainPage;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        FirefoxOptions options = new FirefoxOptions();
        //ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");

        this.driver = new FirefoxDriver(options);
        //this.driver = new ChromeDriver(options);

        this.mainPage = new MainPage(this.driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest
    @MethodSource("importantQuestionsDataGenerator")
    public void importantQuestionsTest(int questionIndex, String expectedAnswer) {

        WebElement questionButton = mainPage.findQuestionButton(questionIndex);
        mainPage.scrollToElement(questionButton);
        questionButton.click();
        WebElement answerPanel = mainPage.findAnswerPanel(questionIndex);
        String actualAnswerText = answerPanel.getText().trim();

        assertEquals(expectedAnswer, actualAnswerText);
    }

    @Test
    public void createOrderTopButtonOpenForm() {
        mainPage.clickTopOrderButton();
        mainPage.assertOrderFormIsOpened();
    }

    @Test
    public void createOrderMiddleButtonOpenForm() {
        mainPage.clickMiddleOrderButton();
        mainPage.assertOrderFormIsOpened();
    }

    public static Stream<Arguments> importantQuestionsDataGenerator() {
        return Stream.of(
                Arguments.of(
                        0,
                        "Сутки — 400 рублей. Оплата курьеру — наличными или картой."
                ),
                Arguments.of(
                        1,
                        "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, " +
                                "можете просто сделать несколько заказов — один за другим."
                ),
                Arguments.of(
                        2,
                        "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. " +
                                "Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. " +
                                "Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."
                ),
                Arguments.of(
                        3,
                        "Только начиная с завтрашнего дня. Но скоро станем расторопнее."
                ),
                Arguments.of(
                        4,
                        "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."
                ),
                Arguments.of(
                        5,
                        "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже " +
                                "если будете кататься без передышек и во сне. Зарядка не понадобится."
                ),
                Arguments.of(
                        6,
                        "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."
                ),
                Arguments.of(
                        7,
                        "Да, обязательно. Всем самокатов! И Москве, и Московской области."
                )
        );
    }
}
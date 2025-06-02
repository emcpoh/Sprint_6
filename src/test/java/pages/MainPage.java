package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage {
    private final WebDriver driver;

    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    public static final By CONFIRM_COOKIE_BUTTON = By.xpath(".//button[@id='rcc-confirm-button']");

    public final By accordionButton = By.className("accordion__button");
    public final By accordionPanel = By.className("accordion__panel");

    public static final By createOrderTopButton = By.xpath("//button[text()='Заказать' and @class='Button_Button__ra12g']");
    public static final By createOrderMiddleButton = By.xpath("//button[text()='Заказать' and @class='Button_Button__ra12g Button_Middle__1CSJM']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.driver.get(BASE_URL);

        WebElement cookieButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(CONFIRM_COOKIE_BUTTON));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cookieButton);
    }

    public void clickTopOrderButton() {
        driver.findElement(createOrderTopButton).click();
    }

    public void clickMiddleOrderButton() {
        scrollToElement(driver.findElement(createOrderMiddleButton));
        driver.findElement(createOrderMiddleButton).click();
    }

    public WebElement findQuestionButton(int questionIndex) {
        return driver.findElements(accordionButton).get(questionIndex);
    }

    public WebElement findAnswerPanel(int answerIndex) {
        return driver.findElements(accordionPanel).get(answerIndex);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void assertOrderFormIsOpened() {
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(CreateOrderPage.createOrderForm));
        assertEquals(CreateOrderPage.BASE_URL , driver.getCurrentUrl());
    }
}

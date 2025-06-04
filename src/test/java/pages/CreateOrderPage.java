package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateOrderPage {
    public static final String BASE_URL = MainPage.BASE_URL + "/order";
    public static final By CONFIRM_COOKIE_BUTTON = By.xpath(".//button[@id='rcc-confirm-button']");

    private final WebDriver driver;

    public CreateOrderPage(WebDriver driver) {
        this.driver = driver;
        this.driver.get(CreateOrderPage.BASE_URL);

        WebElement cookieButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(CreateOrderPage.CONFIRM_COOKIE_BUTTON));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cookieButton);
    }

    public static final By createOrderForm = By.xpath("//div[@class='Order_Content__bmtHS']");
    public static final By createOrderHeader = By.xpath("//div[@class='Order_Header__BZXOb']");

    public static final By nameField = By.xpath("//input[@placeholder='* Имя']");
    public static final By surnameField = By.xpath("//input[@placeholder='* Фамилия']");
    public static final By addressField = By.xpath("//input[contains(@placeholder, 'куда')]");

    public static final By metroStationField = By.xpath("//div[@class='select-search__value']");
    public static final By metroStationInput = By.xpath("//input[@tabindex='0']");
    public static final By metroStationButton = By.xpath("//button[contains(@class, 'select-search__option')]");

    public static final By phoneNumberField = By.xpath("//input[contains(@placeholder, 'позвонит')]");
    public static final By goNextButton = By.xpath("//div[@class='Order_NextButton__1_rCA']/button[text()='Далее']");

    public static final By rentDateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    public static final By rentDateDropDownArrow = By.xpath("//span[@class='Dropdown-arrow']");
    public static final By rentDateDropDownMenu = By.xpath("//div[@class='Dropdown-menu']");
    public static final By rentDateDropDownOptions = By.xpath("//div[@class='Dropdown-option']");

    public static final By chooseColorField = By.xpath("//div[@class='Order_Checkboxes__3lWSI']");

    public static final By commentField = By.xpath("//input[contains(@placeholder, 'Комментарий')]");
    public static final By finishCreateOrderButton = By.xpath("//div[@class='Order_Buttons__1xGrp']/button[text()='Заказать']");
    public static final By confirmOrderButton = By.xpath("//div[@class='Order_Modal__YZ-d3']//button[text()='Да']");

    public static final By orderCreatedModal = By.xpath("//div[@class='Order_Modal__YZ-d3']");
    public static final By orderCreatedModalHeader = By.xpath("//div[@class='Order_ModalHeader__3FDaJ']");
    public static final By orderCreatedModalText = By.xpath("//div[@class='Order_Text__2broi']");

    public void fillMetroStation(String stationName) {
        driver.findElement(metroStationField).click();
        driver.findElement(metroStationInput).sendKeys(stationName);

        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(metroStationButton));

        driver.findElement(metroStationButton).click();
    }

    public void fillName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void fillSurname(String surname) {
        driver.findElement(surnameField).sendKeys(surname);
    }

    public void fillAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    public void fillPhoneNumber(String phoneNumber) {
        driver.findElement(phoneNumberField).sendKeys(phoneNumber);
    }

    public void clickGoNextButton() {
        driver.findElement(goNextButton).click();
    }

    public void fillRentDate(String rentDate) {
        WebElement rendDateElement = driver.findElement(rentDateField);
        rendDateElement.sendKeys(rentDate);
        rendDateElement.sendKeys(Keys.ENTER);
    }

    public void fillRentDuration(String rentDuration) {
        driver.findElement(rentDateDropDownArrow).click();

        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(rentDateDropDownMenu));

        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(rentDateDropDownOptions));

        List<WebElement> options = driver.findElements(rentDateDropDownOptions);
        int selectedOptionIndex = rentDateStringToIndex(rentDuration);
        options.get(selectedOptionIndex).click();
    }

    public void fillColor(String color) {
        WebElement chooseField = driver.findElement(chooseColorField);
        String xpathExpr = String.format(".//label[text()='%s']", color);
        chooseField.findElement(By.xpath(xpathExpr))
                .findElement(By.xpath("input[@type='checkbox']"))
                .click();
    }

    public void fillComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
    }

    public void finishOrderCreation() {
        driver.findElement(finishCreateOrderButton).click();
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(confirmOrderButton))
                .click();
    }

    public void fillCreateOrderForm(
            String name,
            String surname,
            String address,
            String metroStationName,
            String phoneNumber,
            String rentDate,
            String rentDuration,
            String color,
            String comment
    ) {
        assertOrderFormIsOpened();
        fillName(name);
        fillSurname(surname);
        fillAddress(address);
        fillMetroStation(metroStationName);
        fillPhoneNumber(phoneNumber);

        clickGoNextButton();
        assertOrderFormSecondFrameIsOpened();

        fillRentDate(rentDate);
        fillRentDuration(rentDuration);
        fillColor(color);
        fillComment(comment);
        finishOrderCreation();
    }

    public int rentDateStringToIndex(String rentDate) {
        switch (rentDate.trim()) {
            case "сутки":
                return 0;
            case "двое суток":
                return 1;
            case "трое суток":
                return 2;
            case "четверо суток":
                return 3;
            case "пятеро суток":
                return 4;
            case "шестеро суток":
                return 5;
            case "семеро суток":
                return 6;
            default:
                throw new IllegalArgumentException("В rentDateStringToIndex не описан вариант: " + rentDate);
        }
    }

    public void assertOrderCreationInfo() {
        String orderInfoRegex = "^Номер заказа:\\s*(\\d+)\\.\\s+Запишите\\sего:\\s*\\r?\\nпригодится,\\sчтобы\\sотслеживать\\sстатус$";

        WebElement orderCreatedInfo = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(CreateOrderPage.orderCreatedModal));

        String actualOrderCreatedHeader = orderCreatedInfo.findElement(orderCreatedModalHeader).getText().trim();
        String actualOrderCreatedText = orderCreatedInfo.findElement(orderCreatedModalText).getText().trim();

        Assertions.assertTrue(actualOrderCreatedHeader.startsWith("Заказ оформлен"));
        Assertions.assertTrue(actualOrderCreatedText.matches(orderInfoRegex));
    }

    public void assertOrderFormIsOpened() {
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(createOrderForm));
        assertEquals(BASE_URL , driver.getCurrentUrl());
        assertEquals("Для кого самокат" , driver.findElement(createOrderHeader).getText().trim());
    }

    public void assertOrderFormSecondFrameIsOpened() {
        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(createOrderForm));
        assertEquals("Про аренду" , driver.findElement(createOrderHeader).getText().trim());
    }
}

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Сценарий №2
 * 1. Перейти по ссылке http://www.rgs.ru
 * 2. Выбрать пункт меню – Страхование
 * 3. Путешествия – Страхование выезжающих за рубеж
 * 4. Нажать рассчитать – Онлайн
 * 5. Проверить наличие заголовка – Страхование выезжающих за руюеж
 * 6. Заполнить форму:
 * Несколько поездок в течении года
 * Я согласен на обработку данных  - выбрать чекбокс
 * 7. Нажать рассчитать
 * 8. Заполнить поля:
 * Куда едем – Шенген
 * Страна въезда – Испания
 * Дата первой поездки – 1 ноября
 * Сколько дней планируете пробыть за рубежом за год – не более 90
 * ФИО
 * Дата рождения
 * Планируется активный отдых
 * 9. Нажать рассчитать
 * 10. Проверить значения:
 * Условия страхования – Многократные поездки в течении года
 * Территория – Шенген
 * Застрахованный
 * Дата рождения
 * Активный отдых - включен
 */
public class InsuranceTravelTest {
    private WebDriver driver;

    @Before
    public void beforeTest() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        driver = new ChromeDriver();
        String baseURL = "https://www.rgs.ru/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(baseURL);

    }

    @After
    public void afterTest() throws InterruptedException {
//        Thread.sleep(1000);
//        driver.quit();
    }

    @Test
    public void insuranceTravelTest() {
        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);

        //2. Выбрать пункт меню – Страхование
        findByXPath("//*[@data-toggle = 'dropdown'][contains(text(), 'Страхование')]").click();

        //3. Путешествия – Страхование выезжающих за рубеж
        findByXPath("//a[contains(text(),'Выезжающим')]").click();

        //4. Нажать рассчитать – Онлайн
        WebElement calculateOnlineButton = findByXPath("//a[contains(text(),'Рассчитать')]");
        scrollIntoView(calculateOnlineButton,"down");
        calculateOnlineButton.click();

        //5. Проверить наличие заголовка – Страхование выезжающих за рубеж
        assertEquals("Страхование выезжающих за рубеж", findByXPath("//span[contains(text(),'выезжающих')]").getText());

        //6. Выбрать пункты на форме:
        //- Несколько поездок в течении года
        WebElement severalTripsButton = findByXPath("//button[contains(@data-bind,'Несколько')]");
        wait.until(ExpectedConditions.visibilityOf(severalTripsButton));
        severalTripsButton.click();

        //- Я согласен на обработку данных (выбрать чекбокс)
        WebElement iAgreeCheckbox = findByXPath("//input[contains(@data-test-name,'IsProcessingPersonalDataTo')]");
        scrollIntoView(iAgreeCheckbox,"down");
        iAgreeCheckbox.click();

        //7. Нажать "Рассчитать"
        WebElement calculateButton = findByXPath("//button[@data-test-name ='NextButton'][contains(text(),'Рассчитать')]");
        calculateButton.click();

        //8. Заполнить поля:
        // - Куда едем: Шенген
        WebElement countriesTextBox = driver.findElement(By.id("Countries"));
        fillTextField(By.id("Countries"), "Шенген");
        countriesTextBox.sendKeys(Keys.ARROW_DOWN);
        countriesTextBox.sendKeys(Keys.ENTER);

        // - Страна въезда: Испания

        WebElement arrivalCountryList = driver.findElement(By.name("ArrivalCountryList"));
        new Select(arrivalCountryList).selectByVisibleText("Испания");

        // - Дата первой поездки: 1 ноября
        findByXPath("//input[@data-test-name='FirstDepartureDate']").click();
        findByXPath("//input[@data-test-name='FirstDepartureDate']").sendKeys("01112018");

        // - Сколько дней планируете пробыть за рубежом за год: не более 90
        findByXPath("//label[contains(text(), 'Не более 90 дней')]").click();

        // - ФИО
        String LastNameLatin = "IVANOV";
        String FirstNameLatin = "IVAN";
        findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']").click();
        findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']").sendKeys(LastNameLatin);
        findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']").sendKeys(Keys.SPACE);
        findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']").sendKeys(FirstNameLatin);

        // - Дата рождения
        String birthDay = "31.12.1988";
        findByXPath("//input[@data-test-name = 'BirthDate']").click();
        findByXPath("//input[@data-test-name = 'BirthDate']").sendKeys(birthDay);

        // - Планируется активный отдых
        scrollIntoView(findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']"),"down");
        findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']").click();

        // 9. Нажать "Рассчитать"
        scrollIntoView(calculateButton,"down");
        calculateButton.click();

        // 10. Проверить значения:
        // - Условия страхования: Многократные поездки в течении года
        WebElement tripsConditionMainLabel = findByXPath("//span[@data-bind='with: Trips']//span");
        wait.until(ExpectedConditions.visibilityOf(tripsConditionMainLabel));
        assertEquals("Многократные поездки в течение года", tripsConditionMainLabel.getText());
        System.out.println(tripsConditionMainLabel.getText());

        // - Территория: Шенген
        WebElement tripsConditionTerritoryLabel = findByXPath("*//div//span//span[@data-bind='foreach: countries']//strong");
        wait.until(ExpectedConditions.visibilityOf(tripsConditionTerritoryLabel));
        assertEquals("Шенген", tripsConditionTerritoryLabel.getText());
        System.out.println(tripsConditionTerritoryLabel.getText());

        /*
        // - Застрахованный
        WebElement tripsConditionNameLabel = findByXPath("//strong[contains(@data-bind,'FirstName')]");
        wait.until(ExpectedConditions.visibilityOf(tripsConditionNameLabel));
        assertEquals(LastNameLatin + " " + FirstNameLatin, tripsConditionNameLabel.getText());

        // - Дата рождения
        WebElement tripsConditionBirthdayLabel = findByXPath("//strong[contains(@data-bind,'BirthDay')]");
        wait.until(ExpectedConditions.visibilityOf(tripsConditionBirthdayLabel));
        assertEquals(birthDay, tripsConditionBirthdayLabel.getText());

        // - Активный отдых: включен
        WebElement tripsConditionActivityLabel =
                findByXPath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span");
        wait.until(ExpectedConditions.visibilityOf(tripsConditionActivityLabel));
        assertEquals("Включен", tripsConditionActivityLabel.getText());
        System.out.println(tripsConditionActivityLabel.getText());
        */
    }

    private void fillTextField(By locator, String text) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }

    private WebElement findByXPath(String xPath) {
        return driver.findElement(By.xpath(xPath));
    }

    private void scrollIntoView(WebElement element, String direction) {
        switch (direction) {
            case "up":
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
                break;
            case "down":
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                break;
            default:
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }
}

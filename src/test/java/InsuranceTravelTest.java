import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
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
    public void afterTest() {
        driver.quit();
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
        scrollIntoView(calculateOnlineButton, "down");
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
        scrollIntoView(iAgreeCheckbox, "down");
        iAgreeCheckbox.click();

        //7. Нажать "Рассчитать"
        WebElement calculateButton = findByXPath("//button[@data-test-name ='NextButton'][contains(text(),'Рассчитать')]");
        calculateButton.click();

        //8. Заполнить поля:
        // - Куда едем: Шенген
        WebElement countriesTextBox = driver.findElement(By.id("Countries"));
        new Actions(driver).sendKeys(countriesTextBox, "Шенген")
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ENTER)
                .perform();

        // - Страна въезда: Испания

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ArrivalCountryList")));
        WebElement arrivalCountryList = driver.findElement(By.name("ArrivalCountryList"));
        new Select(arrivalCountryList).selectByVisibleText("Испания");

        // - Дата первой поездки: 1 ноября
        findByXPath("//input[@data-test-name='FirstDepartureDate']").click();
        findByXPath("//input[@data-test-name='FirstDepartureDate']").sendKeys("01.11.2018");

        // - Сколько дней планируете пробыть за рубежом за год: не более 90
        findByXPath("//label[contains(text(), 'Не более 90 дней')]").click();

        // - ФИО
        String LastNameLatin = "IVANOV";
        String FirstNameLatin = "IVAN";

        WebElement fullNameLatinTextField =
                findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']");
        fullNameLatinTextField.click();
        new Actions(driver).sendKeys(fullNameLatinTextField, LastNameLatin)
                .sendKeys(Keys.SPACE)
                .sendKeys(FirstNameLatin)
                .perform();

        // - Дата рождения
        String birthDay = "31.12.1988";
        findByXPath("//input[@data-test-name = 'BirthDate']").click();
        findByXPath("//input[@data-test-name = 'BirthDate']").sendKeys(birthDay);

        // - Планируется активный отдых
        scrollIntoView(findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']"), "down");
        findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']").click();

        // 9. Нажать "Рассчитать"
        scrollIntoView(calculateButton, "down");
        calculateButton.click();

        // 10. Проверить значения:
        // - Условия страхования: Многократные поездки в течении года
        List<String> report = new ArrayList<>(); //создаем массив для отчётности

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@data-bind='with: Trips']//span")));
        WebElement tripsConditionMainLabel = findByXPath("//span[@data-bind='with: Trips']//span");
        assertEquals("Многократные поездки в течение года", tripsConditionMainLabel.getText());
        report.add("Условия страхования: " + tripsConditionMainLabel.getText()); //собираем данные для отчёта

        // - Территория: Шенген
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("*//div//span//span[@data-bind='foreach: countries']//strong")));
        WebElement tripsConditionTerritoryLabel = findByXPath("*//div//span//span[@data-bind='foreach: countries']//strong");
        assertEquals("Шенген", tripsConditionTerritoryLabel.getText());
        report.add("Территория действия: " + tripsConditionTerritoryLabel.getText());

        // - Застрахованный
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(@data-bind,'FirstName')]")));
        WebElement tripsConditionNameLabel = findByXPath("//strong[contains(@data-bind,'FirstName')]");
        assertEquals(LastNameLatin + " " + FirstNameLatin, tripsConditionNameLabel.getText());
        report.add("Застрахованный: " + tripsConditionNameLabel.getText());

        // - Дата рождения
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[contains(@data-bind,'BirthDay')]")));
        WebElement tripsConditionBirthdayLabel = findByXPath("//strong[contains(@data-bind,'BirthDay')]");
        assertEquals(birthDay, tripsConditionBirthdayLabel.getText());
        report.add("Д.Р.: " + tripsConditionBirthdayLabel.getText());

        // - Активный отдых: включен
        wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span")));
        WebElement tripsConditionActivityLabel =
                findByXPath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span");
        assertEquals("Включен", tripsConditionActivityLabel.getText());
        report.add("Активный отдых или спорт: " + tripsConditionActivityLabel.getText());

        //Распечатываем отчет по финальной проверке данных
        System.out.println("\nИтоговый отчёт:\n ");
        report.forEach(System.out::println);
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

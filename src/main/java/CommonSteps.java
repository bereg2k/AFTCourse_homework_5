import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Класс для описания базовых шагов работы автотеста с веб-страницей.
 */
class CommonSteps {

    WebDriver driver; //драйвер выполнения теста
    String BASE_URL; //основной веб-адрес, откуда начинается тестирование
    List<String> testReport = new ArrayList<>(); //массив для хранения данных по отчёту по тестированию

    /**
     * Инициализация теста. Выполнение некоторых действий перед запуском отдельного кейса.
     * Для помещения под нотацией @Before в тестовом классе.
     */
    void startUp() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    /**
     * Завершение теста.
     * Для выполнения под нотацией @After в тестовом классе.
     */
    void endTest() {
        driver.quit();
    }

    /**
     * Ожидание доступности или появления на экране элемента.
     *
     * @param locator локатор ожидаемого элемента
     */
    void waitForVisible(By locator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Выбор данных из выпадающих списков.
     *
     * @param element элемент выпадающего списка
     * @param text    название пункта в списке, который надо выбрать
     */
    void selectByText(WebElement element, String text) {
        new Select(element).selectByVisibleText(text);
    }

    /**
     * Поиск элемента по локатору.
     *
     * @param locator локатор элемента
     * @return экземпляр класса найденного элемента
     */
    WebElement findByLocator(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Поиск элемента по xpath.
     *
     * @param xPath xpath элемента
     * @return экземпляр класса найденного элемента
     */
    WebElement findByXPath(String xPath) {
        return findByLocator(By.xpath(xPath));
    }

    /**
     * Нажатие по элементу левой кнопкой мыши.
     *
     * @param locator локатор элемента для кликания.
     */
    void click(By locator) {
        findByLocator(locator).click();
    }

    /**
     * Нажатие по элементу левой кнопкой мыши.
     *
     * @param xPath xpath элемента для кликания.
     */
    void click(String xPath) {
        findByXPath(xPath).click();
    }

    /**
     * Прокрутка страницы до попадания элемента в область видимости.
     *
     * @param element искомый элемент
     */
    void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Заполнение текстового поля.
     *
     * @param locator локатор текстового поля для заполнения
     * @param text    текст, который вставляем в поле
     */
    void populateTextBox(By locator, String text) {
        click(locator);
        findByLocator(locator).sendKeys(text);
    }

    /**
     * Печать итогового отчёта по тестированию.
     */
    void printTestReport() {
        System.out.println("\nИтоговый отчёт:\n ");
        testReport.forEach(System.out::println);
    }
}

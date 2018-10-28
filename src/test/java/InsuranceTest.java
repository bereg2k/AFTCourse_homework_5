import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Сценарий №1
 * 1. Перейти по ссылке http://www.rgs.ru
 * 2. Выбрать пункт меню - Страхование
 * 3. Выбрать категорию - ДМС
 * 4. Проверить наличие заголовка - Добровольное медицинское страхование
 * 5. Нажать на кнопку - Отправить заявку
 * 6. Проверить, что открылась страница , на которой присутствует текст - Заявка на добровольное
 * медицинское страхование
 * 7.Заполнить поля
 * - Имя
 * - Фамилия
 * - Отчество
 * - Регион
 * - Телефон
 * - Эл. почта - qwertyqwerty
 * - Комментарии
 * - Я согласен на обработку (чек-бокс)
 * 8. Проверить, что все поля заполнены введенными значениями
 * 9. Нажать Отправить
 * 10. Проверить, что у Поля - Эл. почта присутствует сообщение об ошибке - "Введите адрес электронной почты".
 */
public class InsuranceTest {

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
    public void testInsurance() {
        findByXPath("//*[@data-toggle = 'dropdown'][contains(text(), 'Страхование')]").click();
        findByXPath("//li[@class ='adv-analytics-navigation-line3-link']/a[contains(text(), 'ДМС')]").click();

        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        WebElement sendButton = findByXPath("//a[contains(text(), 'Отправить заявку')]");
        wait.until(ExpectedConditions.visibilityOf(sendButton)).click();

        WebElement requestTitle = findByXPath("//*[@class = 'modal-title']");
        wait.until(ExpectedConditions.visibilityOf(requestTitle));

        assertEquals("Заявка на добровольное медицинское страхование", requestTitle.getText());

        fillTextField(By.name("LastName"), "Иванов");
        fillTextField(By.name("FirstName"), "Иван");
        fillTextField(By.name("MiddleName"), "Иванович");

        new Select(driver.findElement(By.name("Region"))).selectByVisibleText("Москва");

        fillTextField(By.name("Comment"), "Autotest!");
        fillTextField(By.xpath("//input[contains(@data-bind, 'Phone')]"), "4951234567");
        fillTextField(By.name("Email"), "qwerty");

        findByXPath("//input[contains(@class, 'checkbox')]").click();
        findByXPath("//button[contains(text(), 'Отправить')]").click();

        WebElement emailValidationError = findByXPath("//*[contains(text(), 'Эл. почта')]/..//span[contains(@class, 'validation-error')]");
        WebElement dateValidationError = findByXPath(" //*[contains(text(), 'дата')]/..//span[contains(@class, 'validation-error')]");

        wait.until(ExpectedConditions.visibilityOf(emailValidationError));

        assertEquals("Введите адрес электронной почты",
                emailValidationError.getText());
        assertEquals("Введите дату",
                dateValidationError.getText());

        assertEquals("Иванов", driver.findElement(By.name("LastName")).getAttribute("value"));
        assertEquals("Иван", driver.findElement(By.name("FirstName")).getAttribute("value"));
        assertEquals("Иванович", driver.findElement(By.name("MiddleName")).getAttribute("value"));

        List<WebElement> errorList =  driver.findElements(By.xpath("//*[@class='validation-error-text']"));
        Assert.assertEquals("Количество ошибок при заполнении заявки не совпадает", 2,  errorList.size());

        System.out.println("Следующие виды валидационных сообщений были отображены: ");
        errorList.stream().map(WebElement::getText).forEach(System.out::println);
    }

    private void fillTextField(By locator, String text) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }

    private WebElement findByXPath (String xPath){
        return driver.findElement(By.xpath(xPath));
    }
}

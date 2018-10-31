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

class CommonSteps {

    WebDriver driver;
    String BASE_URL;
    List<String> testReport = new ArrayList<>();

    public WebDriver getDriver() {
        return driver;
    }

    void startUp() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    void endTest() {
        driver.quit();
    }

    void waitForVisible(By locator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    void selectByText(WebElement element, String text) {
        new Select(element).selectByVisibleText(text);
    }

    WebElement findByLocator(By locator) {
        return driver.findElement(locator);
    }

    WebElement findByXPath(String xPath) {
        return findByLocator(By.xpath(xPath));
    }

    void click(By locator) {
        findByLocator(locator).click();
    }

    void click(String xPath) {
        findByXPath(xPath).click();
    }

    void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    void populateTextBox(By locator, String text){
        click(locator);
        findByLocator(locator).sendKeys(text);
    }

    void printTestReport(){
        System.out.println("\nИтоговый отчёт:\n ");
        testReport.forEach(System.out::println);
    }

}

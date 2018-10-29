import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

class CommonSteps {

    WebDriver driver;
    String BASE_URL;

    public WebDriver getDriver() {
        return driver;
    }

    public void startUp() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    public void endTest() {
        driver.quit();
    }

    void waitForVisible(By locator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    void selectByText(WebElement element, String text){
        new Select(element).selectByVisibleText(text);
    }

    WebElement findByXPath(String xPath) {
        return driver.findElement(By.xpath(xPath));
    }

}

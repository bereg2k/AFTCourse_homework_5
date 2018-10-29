import org.openqa.selenium.By;

public class RgsSteps extends CommonSteps {
    private final static String BASE_RGS_URL = "https://www.rgs.ru/";
    private final static By openInsuranceNavBarLocator = By.xpath("//*[@data-toggle = 'dropdown'][contains(text(), 'Страхование')]");
    private final static By insuranceNavBarLocator = By.className("container-rgs-main-menu-links");

    public RgsSteps() {
        BASE_URL = BASE_RGS_URL;
    }

    public void openInsuranceNavBar() {
        click(openInsuranceNavBarLocator);
        waitForVisible(insuranceNavBarLocator);
    }

    public void chooseCategory(String categoryName) {
        click(By.xpath("//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'" + categoryName + "')]"));
    }

    public void chooseParentCategory(String categoryName) {
        click(By.xpath("//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'" + categoryName + "')][@class = 'hidden-xs']"));
    }




}

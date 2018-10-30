import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;


public class RgsSteps extends CommonSteps {
    private final static String BASE_RGS_URL = "https://www.rgs.ru/";
    private final static By openInsuranceNavBarLocator = By.xpath("//*[@data-toggle = 'dropdown'][contains(text(), 'Страхование')]");
    private final static By insuranceNavBarLocator = By.className("container-rgs-main-menu-links");
    private final static String categoryFormat = "//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'%s')]";
    private final static String parentCategoryFormat = "//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'%s')][@class = 'hidden-xs']";
    private final static String calculateOptionFormat = "//div[@class ='thumbnail-footer']//a[contains(text(),'%s')]";

    public RgsSteps() {
        BASE_URL = BASE_RGS_URL;
    }

    public void openInsuranceNavBar() {
        click(openInsuranceNavBarLocator);
        waitForVisible(insuranceNavBarLocator);
    }

    public void chooseCategory(String categoryName) {
        click(By.xpath(String.format(categoryFormat, categoryName)));
    }

    public void chooseParentCategory(String categoryName) {
        click(By.xpath(String.format(parentCategoryFormat, categoryName)));
    }

    public void chooseCalculateOption(String calculateOption) {
        if (calculateOption.equals("онлайн") || calculateOption.equals("Онлайн") || calculateOption.equals("Рассчитать онлайн")) {
            scrollIntoView(findByXPath(String.format(calculateOptionFormat, "Рассчитать")));
            click(String.format(calculateOptionFormat, "Рассчитать"));
        } else {
            scrollIntoView(findByXPath(String.format(calculateOptionFormat, calculateOption)));
            click(String.format(calculateOptionFormat, calculateOption));
        }
    }

    public void checkHeaderText(String headerType, String expectedResult){
        assertEquals(expectedResult, findByXPath("//span[contains(text(),'выезжающих')]").getText());
    }

}

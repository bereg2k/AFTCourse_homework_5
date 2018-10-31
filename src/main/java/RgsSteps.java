import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class RgsSteps extends CommonSteps {
    private final static String BASE_RGS_URL = "https://www.rgs.ru/";
    private final static By openInsuranceNavBarLocator = By.xpath("//*[@data-toggle = 'dropdown'][contains(text(), 'Страхование')]");
    private final static By insuranceNavBarLocator = By.className("container-rgs-main-menu-links");
    private final static String categoryFormat = "//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'%s')]";
    private final static String parentCategoryFormat = "//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'%s')][@class = 'hidden-xs']";
    private final static String calculateOptionFormat = "//div[@class ='thumbnail-footer']//a[contains(text(),'%s')]";
    private final static String numberOfTripsButtonFormat = "//button[contains(@data-bind,'%s')]";

    private String FullNameLatin;
    private String birthday;
    private boolean isMultipleTrips;
    private String destinationTerritory;
    private boolean isActiveRestIncluded;
    private boolean isEducationIncluded;

    RgsSteps() {
        BASE_URL = BASE_RGS_URL;
    }

    void openInsuranceNavBar() {
        click(openInsuranceNavBarLocator);
        waitForVisible(insuranceNavBarLocator);
    }

    void chooseCategory(String categoryName) {
        click(By.xpath(String.format(categoryFormat, categoryName)));
    }

    public void chooseParentCategory(String categoryName) {
        click(By.xpath(String.format(parentCategoryFormat, categoryName)));
    }

    void chooseCalculateOption(String calculateOption) {
        if (calculateOption.equals("онлайн") || calculateOption.equals("Онлайн") || calculateOption.equals("Рассчитать онлайн")) {
            scrollIntoView(findByXPath(String.format(calculateOptionFormat, "Рассчитать")));
            click(String.format(calculateOptionFormat, "Рассчитать"));
        } else {
            scrollIntoView(findByXPath(String.format(calculateOptionFormat, calculateOption)));
            click(String.format(calculateOptionFormat, calculateOption));
        }
    }

    void checkHeaderText(String expectedResult) {
        assertEquals(expectedResult, findByXPath("//div[@class='page-header']//span[@class='h1']").getText());
        testReport.add("Заголовок формы = " + expectedResult + " - ОК!");
    }

    void chooseNumberOfTrips(String numberOfTripsText) {
        waitForVisible(By.xpath(String.format(numberOfTripsButtonFormat, numberOfTripsText)));
        click(String.format(numberOfTripsButtonFormat, numberOfTripsText));

        isMultipleTrips = numberOfTripsText.toUpperCase().contains("НЕСКОЛЬКО");
    }

    void agreementCheckBoxClick() {
        scrollIntoView(findByXPath("//input[contains(@data-test-name,'IsProcessingPersonalDataTo')]"));
        click("//input[contains(@data-test-name,'IsProcessingPersonalDataTo')]");
    }

    void calculateButtonClick() {
        scrollIntoView(findByXPath("//button[@data-test-name ='NextButton'][contains(text(),'Рассчитать')]"));
        click("//button[@data-test-name ='NextButton'][contains(text(),'Рассчитать')]");
    }

    void selectDestination(String destination) {
        this.destinationTerritory = destination;
        click(By.id("Countries"));
        new Actions(driver).sendKeys(findByLocator(By.id("Countries")), destination)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ENTER)
                .perform();
    }

    void selectShengenCountry(String shengenCountryName) {
        waitForVisible(By.name("ArrivalCountryList"));
        new Select(findByLocator(By.name("ArrivalCountryList"))).selectByVisibleText(shengenCountryName);
    }

    void inputFirstDepartureDate(String date) {
        populateTextBox(By.xpath("//input[@data-test-name='FirstDepartureDate']"), date);
    }

    void inputBirthdate(String birthday) {
        this.birthday = birthday;
        populateTextBox(By.xpath("//input[@data-test-name = 'BirthDate']"), birthday);
    }

    void chooseTripDuration(String durationLimit) {
        click(By.xpath("//label[contains(text(), '" + durationLimit + "')]"));
    }

    void inputFullNameLatin(String FullNameLatin) {
        String[] nameSplit = FullNameLatin.split("\\s+");
        String LastNameLatin = nameSplit[0].toUpperCase();
        String FirstNameLatin = nameSplit[1].toUpperCase();
        this.FullNameLatin = LastNameLatin + " " + FirstNameLatin;

        click(By.xpath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']"));
        new Actions(driver).sendKeys(findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']"), LastNameLatin)
                .sendKeys(Keys.SPACE)
                .sendKeys(FirstNameLatin)
                .perform();
    }

    void chooseAdditionalInsurancePlansOptions(String option) {
        switch (option.toUpperCase()) {
            case "АКТИВНЫЙ ОТДЫХ":
                isActiveRestIncluded = true;
                isEducationIncluded = false;
                scrollIntoView(findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']"));
                click("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']");
                break;
            case "ОБУЧЕНИЕ":
                isEducationIncluded = true;
                isActiveRestIncluded = false;
                scrollIntoView(findByXPath("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']"));
                click("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']");
                break;
            case "АКТИВНЫЙ ОТДЫХ И ОБУЧЕНИЕ":
                isActiveRestIncluded = true;
                isEducationIncluded = true;
                scrollIntoView(findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']"));
                click("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']");
                scrollIntoView(findByXPath("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']"));
                click("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']");
                break;
            default:
                isActiveRestIncluded = false;
                isEducationIncluded = false;
                break;
        }
    }

    void checkTravelInsuranceCalculationResults() {
        // - Условия страхования: Многократные поездки в течении года
        waitForVisible(By.xpath("//span[@data-bind='with: Trips']//span"));
        if (isMultipleTrips) {
            assertEquals("Многократные поездки в течение года", findByXPath("//span[@data-bind='with: Trips']//span").getText());
            testReport.add("Условия страхования: " + findByXPath("//span[@data-bind='with: Trips']//span").getText() + " - OK!");
        } else {
            assertTrue(findByXPath("//span[@data-bind='with: Trips']//strong").getText().contains("дней"));
            testReport.add("Условия страхования: Поездка длинной " + findByXPath("//span[@data-bind='with: Trips']//strong").getText() + " - OK!");
        }

        // - Территория: Шенген
        waitForVisible(By.xpath("*//div//span//span[@data-bind='foreach: countries']//strong"));
        assertEquals(destinationTerritory, findByXPath("*//div//span//span[@data-bind='foreach: countries']//strong").getText());
        testReport.add("Территория действия: " + findByXPath("*//div//span//span[@data-bind='foreach: countries']//strong").getText() + " - OK!");

        // - Застрахованный
        waitForVisible(By.xpath("//strong[contains(@data-bind,'FirstName')]"));
        assertEquals(FullNameLatin, findByXPath("//strong[contains(@data-bind,'FirstName')]").getText());
        testReport.add("Застрахованный: " + findByXPath("//strong[contains(@data-bind,'FirstName')]").getText() + " - OK!");

        // - Дата рождения
        waitForVisible(By.xpath("//strong[contains(@data-bind,'BirthDay')]"));
        assertEquals(birthday, findByXPath("//strong[contains(@data-bind,'BirthDay')]").getText());
        testReport.add("Д.Р.: " + findByXPath("//strong[contains(@data-bind,'BirthDay')]").getText() + " - OK!");

        // - Активный отдых: включен
        if (isActiveRestIncluded) {
            waitForVisible((By.xpath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span")));
            assertEquals("Включен", findByXPath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span").getText());
            testReport.add("Активный отдых или спорт: " + findByXPath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span").getText() + " - OK!");
        }
        if (isEducationIncluded) {
            waitForVisible((By.xpath("//div[contains(@data-bind, 'Обучение')]//div[contains(@style, 'opa')]//..//span[@class = 'summary-value']//span")));
            assertEquals("Включен", findByXPath("//div[contains(@data-bind, 'Обучение')]//div[contains(@style, 'opa')]//..//span[@class = 'summary-value']//span").getText());
            testReport.add("Обучение: включено - ОК!");
        }
        if (!isEducationIncluded & !isActiveRestIncluded) {
            assertEquals(0, findByXPath("//div[contains(@data-bind, 'Активный')]").getSize().getHeight());
            assertEquals(0, findByXPath("//div[contains(@data-bind, 'Обучение')]").getSize().getHeight());
            testReport.add("Обучение и активный отдых: не включены - ОК!");
        }
    }
}

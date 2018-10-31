import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Описание переменных и методов для работы с элементами сайта Росгосстраха.
 */
class RgsSteps extends CommonSteps {
    //основная ссылка на главную страницу Росгосстраха
    private final static String BASE_RGS_URL = "https://www.rgs.ru/";

    //константое значение локатора кнопки "СТРАХОВАНИЕ" на главной странице
    private final static By openInsuranceNavBarLocator = By.xpath("//*[@data-toggle = 'dropdown'][contains(text(), 'Страхование')]");

    //константое значение локатора панели главного меню, вызываемого по кнопке "СТРАХОВАНИЕ"
    private final static By insuranceNavBarLocator = By.className("container-rgs-main-menu-links");

    //общий формат локатора при выборе категории из меню "Страхование" (например, %s = Выезжающим за рубеж)
    private final static String categoryFormat = "//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'%s')]";

    //общий формат локатора при выборе родительской категории (содержит в себе несколько обычных) из меню "Страхование"
    private final static String parentCategoryFormat = "//div[@class = 'grid rgs-main-menu-links']//a[contains(text(),'%s')][@class = 'hidden-xs']";

    //общий формат локатора для кнопки выбора способов расчёта для полиса - %s = Онлайн, Позвонить или Найти офис
    private final static String calculateOptionFormat = "//div[@class ='thumbnail-footer']//a[contains(text(),'%s')]";

    //общий формат локатора для опции выбора количества поездок - %s = Одна или Несколько
    private final static String numberOfTripsButtonFormat = "//button[contains(@data-bind,'%s')]";

    private String FullNameLatin;           // полное имя на латинице
    private String birthday;                // дата рождения в текстовом формате типа "DD.MM.YYYY"
    private boolean isMultipleTrips;        // флаг выбора нескольких поездок для полиса
    private String destinationTerritory;    // место назначения в общем виде (страна или зона, типа "Шенген", "Весь мир")
    private boolean isActiveRestIncluded;   // флаг включения опции Активного отдыха
    private boolean isEducationIncluded;    // флаг включения опции Обучения

    RgsSteps() {
        BASE_URL = BASE_RGS_URL;
    }

    /**
     * Открытие главного меню по кнопке "Страхование" вверху страницы.
     */
    void openInsuranceNavBar() {
        click(openInsuranceNavBarLocator);
        waitForVisible(insuranceNavBarLocator);
    }

    /**
     * Выбор категории из главного меню.
     *
     * @param categoryName наименование категории (передавать как есть на странице)
     */
    void chooseCategory(String categoryName) {
        click(By.xpath(String.format(categoryFormat, categoryName)));
    }

    /**
     * Выбор родительской категории из главного меню.
     * Дополнительный метод для работы с пунктами главного меню, имеющие обычные пункты в качестве дочерних.
     *
     * @param categoryName наименование родительской категории (передавать как есть на странице)
     */
    void chooseParentCategory(String categoryName) {
        click(By.xpath(String.format(parentCategoryFormat, categoryName)));
    }

    /**
     * Выбор варианта расчёта полиса выезжающих за рубеж (ВЗР) со страницы "Страхование для выезжающих за рубеж".
     *
     * @param calculateOption выбор для расчёта. Возможные варианты - "онлайн"("рассчитать" или "рассчитать онлайн"),
     *                        "позвонить", "найти офис".
     */
    void chooseCalculateOption(String calculateOption) {
        if (calculateOption.toUpperCase().equals("ОНЛАЙН") || calculateOption.toUpperCase().equals("РАССЧИТАТЬ")
                || calculateOption.toUpperCase().equals("РАССЧИТАТЬ ОНЛАЙН")) {
            scrollIntoView(findByXPath(String.format(calculateOptionFormat, "Рассчитать")));
            click(String.format(calculateOptionFormat, "Рассчитать"));
        } else {
            scrollIntoView(findByXPath(String.format(calculateOptionFormat, calculateOption)));
            click(String.format(calculateOptionFormat, calculateOption));
        }
    }

    /**
     * Проверка основных заголовков у разделов на сайте.
     *
     * @param expectedResult текст заголовка для сверки
     */
    void checkHeaderText(String expectedResult) {
        assertEquals(expectedResult, findByXPath("//div[@class='page-header']//span[@class='h1']").getText());
        testReport.add("Заголовок формы = " + expectedResult + " - ОК!"); //
    }

    /**
     * Выбор количества поездок со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     *
     * @param numberOfTripsText выбор количества поездок. Варианты - "Одна" ("Одна поездка")
     *                          или "Несколько" ("Несколько в течение года"). Соблюдать регистр!.
     */
    void chooseNumberOfTrips(String numberOfTripsText) {
        waitForVisible(By.xpath(String.format(numberOfTripsButtonFormat, numberOfTripsText)));
        click(String.format(numberOfTripsButtonFormat, numberOfTripsText));

        isMultipleTrips = numberOfTripsText.toUpperCase().contains("НЕСКОЛЬКО");
    }

    /**
     * Нажатие на чек-бокс "Я согласен на обработку моих персональных данных в целях расчета страховой премии."
     * на странице онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     */
    void agreementCheckBoxClick() {
        scrollIntoView(findByXPath("//input[contains(@data-test-name,'IsProcessingPersonalDataTo')]"));
        click("//input[contains(@data-test-name,'IsProcessingPersonalDataTo')]");
    }

    /**
     * Нажатие на кнопку "РАССЧИТАТЬ" на странице онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     */
    void calculateButtonClick() {
        scrollIntoView(findByXPath("//button[@data-test-name ='NextButton'][contains(text(),'Рассчитать')]"));
        click("//button[@data-test-name ='NextButton'][contains(text(),'Рассчитать')]");
    }

    /**
     * Выбор направления для путешествия в общих словах.
     * Осуществляется со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     *
     * @param destination название страны или зоны, типа "Шенген", "Весь мир".
     */
    void selectDestination(String destination) {
        this.destinationTerritory = destination;
        click(By.id("Countries"));
        new Actions(driver).sendKeys(findByLocator(By.id("Countries")), destination)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ENTER)
                .perform();
    }

    /**
     * Выбор страны в зоне Шенген для путешествия. Выбирается из выпадающего списка.
     * Осуществляется со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     *
     * @param shengenCountryName название страны в Шенген зоне.
     */
    void selectShengenCountry(String shengenCountryName) {
        waitForVisible(By.name("ArrivalCountryList"));
        selectByText(findByLocator(By.name("ArrivalCountryList")), shengenCountryName);
    }

    /**
     * Ввод даты выезда из страны.
     * Осуществляется со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     * !!! Сейчас работает только для случая количества поездок - "НЕСКОЛЬКО В ТЕЧЕНИЕ ГОДА" !!!
     *
     * @param date дата в формате "DD.MM.YYYY"
     */
    void inputFirstDepartureDate(String date) {
        populateTextBox(By.xpath("//input[@data-test-name='FirstDepartureDate']"), date);
    }

    /**
     * Ввод даты рождения застрахованного.
     * Осуществляется со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     *
     * @param birthday дата рождения в формате "DD.MM.YYYY"
     */
    void inputBirthdate(String birthday) {
        this.birthday = birthday;
        populateTextBox(By.xpath("//input[@data-test-name = 'BirthDate']"), birthday);
    }

    /**
     * Ввод продолжительности нахождения за рубежом.
     * Осуществляется со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     *
     * @param durationLimit длительность поездки. Варианты - "Не более 30 дней" или "Не более 90 дней" (соблюдая регистр!).
     *                      Ещё возможно "30" или "90".
     */
    void chooseTripDuration(String durationLimit) {
        click(By.xpath("//label[contains(text(), '" + durationLimit + "')]"));
    }

    /**
     * Ввод фамилии и имени путешественника на латинице.
     * Осуществляется со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     *
     * @param FullNameLatin фамилия и имя на латинице. Сначала фамилия, потом пробел (или несколько) и имя. РЕГИСТР ЛЮБОЙ!
     *                      Например, "Ivanov Ivan" или "ivanov  ivan" или "IVANOV ivan".
     */
    void inputFullNameLatin(String FullNameLatin) {
        String[] nameSplit = FullNameLatin.split("\\s+");
        String LastNameLatin = nameSplit[0].toUpperCase();
        String FirstNameLatin = nameSplit[1].toUpperCase();
        this.FullNameLatin = LastNameLatin + " " + FirstNameLatin;

        scrollIntoView(findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']"));
        click(By.xpath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']"));
        new Actions(driver).sendKeys(findByXPath("//div[@class='validation-group-error-wrap']/../input[@data-test-name = 'FullName']"), LastNameLatin)
                .sendKeys(Keys.SPACE)
                .sendKeys(FirstNameLatin)
                .perform();
    }

    /**
     * Выбор дополнительных опций для путешествия.
     * Осуществляется со страницы онлайн-расчёта "Калькулятор страхования путешественников онлайн".
     *
     * @param option доп.опции для путешествия. Варианты - "Активный отдых" или "Обучение" или "Активный отдых и обучение".
     *               Регистр ЛЮБОЙ, но пробелы и точность слов именно как здесь!
     */
    void chooseAdditionalInsurancePlansOptions(String option) {
        switch (option.toUpperCase()) {
            case "АКТИВНЫЙ ОТДЫХ": //выбрать только опцию "Активный отдых".
                isActiveRestIncluded = true;
                isEducationIncluded = false;
                scrollIntoView(findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']"));
                click("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']");
                break;
            case "ОБУЧЕНИЕ": //выбрать только опцию "Обучение".
                isEducationIncluded = true;
                isActiveRestIncluded = false;
                scrollIntoView(findByXPath("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']"));
                click("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']");
                break;
            case "АКТИВНЫЙ ОТДЫХ И ОБУЧЕНИЕ": //выбрать обе опции "Активный отдых" и "Обучение".
                isActiveRestIncluded = true;
                isEducationIncluded = true;
                scrollIntoView(findByXPath("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']"));
                click("//div[contains(@data-bind, 'activeRest')]/div[@class='toggle off toggle-rgs']");
                scrollIntoView(findByXPath("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']"));
                click("//div[@class='sections']//div[not(@data-bind)]/div[@class='toggle off toggle-rgs']");
                break;
            default: //если текст параметра другой, то обе опции игнорируются
                isActiveRestIncluded = false;
                isEducationIncluded = false;
                break;
        }
    }

    /**
     * Общий метод проверки результатов расчёта стоимости полиса ВЗР.
     * Метод работает с общими переменными, так что является условно-универсальным (не требует передачи доп. параметров).
     */
    void checkTravelInsuranceCalculationResults() {
        // - Условия страхования
        waitForVisible(By.xpath("//span[@data-bind='with: Trips']//span"));
        if (isMultipleTrips) { //был выбран вариант нескольких поездок
            assertEquals("Многократные поездки в течение года", findByXPath("//span[@data-bind='with: Trips']//span").getText());
            testReport.add("Условия страхования: " + findByXPath("//span[@data-bind='with: Trips']//span").getText() + " - OK!");
        } else { //был выбран вариант с одной поездкой
            assertTrue(findByXPath("//span[@data-bind='with: Trips']//strong").getText().contains("дней"));
            testReport.add("Условия страхования: Поездка длинной " + findByXPath("//span[@data-bind='with: Trips']//strong").getText() + " - OK!");
        }

        // - Территория
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

        // - Активный отдых или обучение
        if (isActiveRestIncluded) { //был выбран только Активный отдых
            waitForVisible((By.xpath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span")));
            assertEquals("Включен", findByXPath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span").getText());
            testReport.add("Активный отдых или спорт: " + findByXPath("//div[contains(@data-bind, 'risk')]//div[contains(@style, 'opa')]//span[@class = 'summary-value']//span").getText() + " - OK!");
        }
        if (isEducationIncluded) { //было выбрано только Обучение
            waitForVisible((By.xpath("//div[contains(@data-bind, 'Обучение')]//div[contains(@style, 'opa')]//..//span[@class = 'summary-value']//span")));
            assertEquals("Включен", findByXPath("//div[contains(@data-bind, 'Обучение')]//div[contains(@style, 'opa')]//..//span[@class = 'summary-value']//span").getText());
            testReport.add("Обучение: включено - ОК!");
        }
        if (!isEducationIncluded & !isActiveRestIncluded) { //доп. опции не включены
            assertEquals(0, findByXPath("//div[contains(@data-bind, 'Активный')]").getSize().getHeight());
            assertEquals(0, findByXPath("//div[contains(@data-bind, 'Обучение')]").getSize().getHeight());
            testReport.add("Обучение и активный отдых: не включены - ОК!");
        }
    }
}

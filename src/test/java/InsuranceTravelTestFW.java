import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InsuranceTravelTestFW {
    private RgsSteps userTest = new RgsSteps();

    @Before
    public void beforeTest() {
        userTest.startUp();
    }

    @After
    public void afterTest() {
        userTest.endTest();
    }

    @Test
    public void insuranceTravelTestLite() {
        //2. Выбрать пункт меню – Страхование
        userTest.openInsuranceNavBar();

        //3. Путешествия – Страхование выезжающих за рубеж
        userTest.chooseCategory("Выезжающим за рубеж");

        //4. Нажать рассчитать – Онлайн
        userTest.chooseCalculateOption("Рассчитать");

        //5. Проверить наличие заголовка – Страхование выезжающих за рубеж
        userTest.checkHeaderText("Страхование выезжающих за рубеж");

        //6. Выбрать пункты на форме:
        //- Несколько поездок в течении года
        userTest.chooseNumberOfTrips("Несколько в течение года");

        //- Я согласен на обработку данных (выбрать чекбокс)
        userTest.agreementCheckBoxClick();

        //7. Нажать "Рассчитать"
        userTest.calculateButtonClick();

        //8. Заполнить поля:
        // - Куда едем: Шенген
        userTest.selectDestination("Шенген");

        // - Страна въезда: Испания
        userTest.selectShengenCountry("Испания");

        // - Дата первой поездки: 1 ноября
        userTest.inputFirstDepartureDate("01.11.2018");

        // - Сколько дней планируете пробыть за рубежом за год: не более 90
        userTest.chooseTripDuration("Не более 90 дней");

        // - ФИО
        userTest.inputFullNameLatin("Berezhnoy  Oleg");

        // - Дата рождения
        userTest.inputBirthdate("31.12.1988");

        // - Планируется активный отдых
        userTest.chooseAdditionalInsurancePlansOptions("активный отдых");

        // 9. Нажать "Рассчитать"
        userTest.calculateButtonClick();

        // 10. Проверить значения:
        // - Условия страхования: Многократные поездки в течении года
        // - Территория: Шенген
        // - Застрахованный
        // - Дата рождения
        // - Активный отдых: включен
        userTest.checkTravelInsuranceCalculationResults();

        //Распечатываем отчет по финальной проверке данных
        userTest.printTestReport();
    }

}


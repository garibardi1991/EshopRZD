package ru.eshoprzd.tests;

import cloud.autotests.helpers.DriverUtils;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.eshoprzd.config.Property;
import ru.eshoprzd.helpers.Attach;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@Tag("testEshop")
public class TestsEshopRzd {

    @BeforeAll
    static void configure() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        SelenideLogger.addListener("allure", new AllureSelenide());

        Configuration.browserCapabilities = capabilities;
        Configuration.browser = Property.browser();
        Configuration.browserVersion = Property.browserVersion();
        Configuration.browserSize = Property.browserSize();
        if (!Property.remoteUrl().equals("")) {
            Configuration.remote = Property.remoteUrl();
        }
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }

    @Test
    @Feature("Проверка сайта eshoprzd.ru")
    @Story("Проверяем раздел Вход")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("Проверка наличия формы ввода логина")
    void openEshop() {
        step("Открываем сайт eshoprzd.ru", () ->
        open("https://eshoprzd.ru/home"));

        step("Нажимаем на кнопку 'Вход'", () ->
        $("#login-btn").click());

        step("Проверяем наличие формы 'Входа'", () ->
        $("[ng-click*='showLoginForm']").should(appear));
        sleep(3000);
    }

    @Test
    @Feature("Проверка сайта eshoprzd.ru")
    @Story("Проверяем консоль разработчика")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("Журнал консоли страницы не должен содержать ошибок")
    void consoleShouldNotHaveErrorsTestEshop() {
        step("Открываем сайт eshoprzd.ru", () ->
            open("https://eshoprzd.ru/home"));

        step("Журналы консоли не должны содержать текст 'SEVERE'", () -> {
            String consoleLogs = Attach.getConsoleLogs();
            String errorText = "SEVERE";

            assertThat(consoleLogs).doesNotContain(errorText);
        });
    }

    @Test
    @Feature("Проверка сайта eshoprzd.ru")
    @Story("Проверяем наличие раздела 'Об Электронном магазине'")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("На главной странице должен отображаться раздел 'Об Электронном магазине'")
    void titleTest() {
        step("Открываем сайт eshoprzd.ru", () ->
                open("https://eshoprzd.ru/home"));

        step("На главной странице должен отображаться раздел с наименование 'Об Электронном магазине'", () -> {
            String expectedTitle = "Об Электронном магазине";
            String actualTitle = title();

            assertThat(actualTitle).isEqualTo(expectedTitle);
        });
    }

}


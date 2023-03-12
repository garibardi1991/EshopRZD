package ru.eshoprzd.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.eshoprzd.config.Property;
import ru.eshoprzd.helpers.Attach;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("testEshop")
public class TestsEshopRzd {

    @BeforeAll
    static void configure() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        SelenideLogger.addListener("allure", new AllureSelenide());

//        Configuration.baseUrl = "https://eshoprzd.ru/";
//        Configuration.browserSize = "1920x1080";
//        Configuration.headless = true;
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
    @Story("Проверяем наличие плашки с информацией 'На нашем сайте мы используем...'")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("На главной странице должна отображаться информация 'На нашем сайте мы используем файлы cookie. Продо...'")
    void titleTest() {
        step("Открываем сайт eshoprzd.ru", () ->
                open("https://eshoprzd.ru/home"));

        step("Проверка наличия отображения информации 'На нашем сайте мы используем файлы cookie. Продолжая работу на сайте, Вы даете согласие на использование файлов cookie.'", () ->
            $(".ng-scope").shouldHave(text("На нашем сайте мы используем файлы cookie. Продолжая работу на сайте, Вы даете согласие на использование файлов cookie.")));
    }


    static Stream<Arguments> openEshopRzdCheckForm(){
        File file = new File("src/test/resources/files/haski.jpg");
        return Stream.of(
                Arguments.of("Прочие вопросы", "Игорь", "garibardi@list.ru", "Hello", file, "Заполните все обязательные поля." ),
                Arguments.of("Техническая поддержка, изменение реквизитов и пр.", "Вася", "garibardi@mail.ru", "Good job", file, "Заполните все обязательные поля.")
        );
    }

    @MethodSource
    @ParameterizedTest
    @Feature("Проверка сайта eshoprzd.ru")
    @Story("Проверяем раздел 'Контакты'")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("Проверка заполнения обязательных полей и подгрузка файлов")
    void openEshopRzdCheckForm(String subject, String name, String email, String text, File file, String message){
        step("Открываем сайт eshoprzd.ru, раздел 'Контакты'", () ->
        open("https://eshoprzd.ru/contacts"));

        step("Заполянем 'Выберите тему'", () ->{
        $("#theme").click();
        $(byText(subject)).click();
                });
        step("Заполянем 'Пожалуйста, представьтесь'", () ->
        $("#name").setValue(name));

        step("Заполянем 'Электронная почта'", () ->
        $("#email").setValue(email));

        step("Заполянем поле 'Текст обращения'", () ->
        $("#text").setValue(text));

        step("Загружаем файл в раздел 'Документы'", () ->{
        $("[ng-click='it.showAttachBlock()']").click();
        $(".attachments__file input[type='file']").uploadFile(file);
        });

        step("Проверяем выходит ли сообщение 'Заполните все обязательные поля.'", () ->{
        $("[ng-click='sendFeedback(content)']").click();
        $(".alert-danger").shouldHave(Condition.text(message));
        });
    }
    @Test
    @Feature("Проверка сайта eshoprzd.ru")
    @Story("Проверяем скачивание файлов")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("Должен быть скачен документ 'Регламент работ'")
    public void downloadFile() throws IOException {
        step("Открываем сайт eshoprzd.ru, раздел 'Документы'", () ->
        open("https://eshoprzd.ru/documents"));

        step("Скачиваем файл 'Регламент работы' и сравниваем с эталонным файлом", () -> {
            var downloadedFile = $("[href='/downloads/reglament_em.pdf']").download();
            var etalon = new File("src/test/resources/files/reglament_em.pdf");
            var isEqual = FileUtils.contentEquals(downloadedFile, etalon);
            assertTrue(isEqual);
        });
    }
}


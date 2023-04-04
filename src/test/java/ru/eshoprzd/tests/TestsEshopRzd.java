package ru.eshoprzd.tests;

import com.codeborne.selenide.Condition;
import io.qameta.allure.*;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.eshoprzd.config.AuthConfig;
import ru.eshoprzd.config.TestBase;
import ru.eshoprzd.pages.PageObjectsEshopRzd;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("testEshop")
public class TestsEshopRzd extends TestBase {
    PageObjectsEshopRzd pageObjectsEshopRzd= new PageObjectsEshopRzd();


    @Test
    @Feature("Проверка сайта eshoprzd.ru")
    @Story("Проверяем раздел Вход")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("Проверка наличия формы ввода логина")
    void openEshop() {
        step("Открываем сайт eshoprzd.ru", () ->
                pageObjectsEshopRzd.openPage());

        step("Нажимаем на кнопку 'Вход'", () ->
                pageObjectsEshopRzd.loginClick());

        step("Проверяем наличие формы 'Входа'", () ->
                pageObjectsEshopRzd.shouldEntrance());
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
                pageObjectsEshopRzd.openPage());

        step("Журналы консоли не должны содержать текст 'SEVERE'", () -> {
            pageObjectsEshopRzd.checkingСonsole();
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
                pageObjectsEshopRzd.openPage());

        step("Проверка наличия отображения информации 'На нашем сайте мы используем файлы cookie. Продолжая работу на сайте, Вы даете согласие на использование файлов cookie.'", () ->
                pageObjectsEshopRzd.checkingForm());
    }


    static Stream<Arguments> openEshopRzdCheckForm(){
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());
        File file = new File("src/test/resources/files/haski.jpg");
        return Stream.of(
                Arguments.of("Прочие вопросы", config.name(), config.email(), "Hello", file, "Заполните все обязательные поля." ),
                Arguments.of("Техническая поддержка, изменение реквизитов и пр.", config.name2(), config.email2(), "Good job", file, "Заполните все обязательные поля.")

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
        pageObjectsEshopRzd.openContacts());

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
        pageObjectsEshopRzd.openDocuments());

        step("Скачиваем файл 'Регламент работы' и сравниваем с эталонным файлом", () -> {
            var downloadedFile = $("[href='/downloads/reglament_em.pdf']").download();
            var etalon = new File("src/test/resources/files/reglament_em.pdf");
            var isEqual = FileUtils.contentEquals(downloadedFile, etalon);
            assertTrue(isEqual);
        });
    }

    @Test
    @Feature("Проверка сайта eshoprzd.ru")
    @Story("Проверяем раздел 'Контакты'")
    @Owner("trubikhoviv")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Testing", url = "https://eshoprzd.ru/home")
    @DisplayName("Проверка заполнения обязательных полей")
    void openCheckForm(){
        AuthConfig config = ConfigFactory.create(AuthConfig.class, System.getProperties());

        step("Открываем сайт eshoprzd.ru, раздел 'Контакты'", () ->
                pageObjectsEshopRzd.openContacts());


        step("Заполянем 'Пожалуйста, представьтесь'", () ->
                $("#name").setValue(config.name()));

        step("Заполянем 'Электронная почта'", () ->
                $("#email").setValue(config.email()));


        step("Проверяем выходит ли сообщение 'Заполните все обязательные поля.'", () ->{
            pageObjectsEshopRzd.checkingMessage();
        });
    }

}


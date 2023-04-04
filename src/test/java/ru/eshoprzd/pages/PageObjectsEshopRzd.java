package ru.eshoprzd.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.eshoprzd.helpers.Attach;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PageObjectsEshopRzd {

    private final SelenideElement
            clickEntrance = $("#login-btn"),
            checkingform = $("[ng-click*='showLoginForm']"),
            datapage = $(".ng-scope"),
            click = $("[ng-click='sendFeedback(content)']"),
            chekingAlert= $(".alert-danger");


    public PageObjectsEshopRzd openPage() {
        open("https://eshoprzd.ru/home");
        return this;
    }
    public PageObjectsEshopRzd loginClick() {
        clickEntrance.click();

        return this;
    }

    public PageObjectsEshopRzd shouldEntrance() {
        checkingform.should(appear);
        sleep(3000);
        return this;
    }

    public PageObjectsEshopRzd checkingСonsole() {
        String consoleLogs = Attach.getConsoleLogs();
        String errorText = "SEVERE";
        assertThat(consoleLogs).doesNotContain(errorText);
        return this;
    }
    public PageObjectsEshopRzd checkingForm() {
        datapage.shouldHave(text("На нашем сайте мы используем файлы cookie. Продолжая работу на сайте, Вы даете согласие на использование файлов cookie."));

        return this;
    }
    public PageObjectsEshopRzd openContacts() {
        open("https://eshoprzd.ru/contacts");

        return this;
    }

    public PageObjectsEshopRzd openDocuments() {
        open("https://eshoprzd.ru/documents");

        return this;
    }

    public PageObjectsEshopRzd checkingMessage() {
        click.click();
        chekingAlert.shouldHave(Condition.text("Заполните все обязательные поля."));

        return this;
    }
}

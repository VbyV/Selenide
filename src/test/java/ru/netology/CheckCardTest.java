package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class CheckCardTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldCheckCardValid() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов Сергей");
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__content").shouldBe(Condition.visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно забронирована на " + correctData));

    }

    @Test
    void shouldCheckCardToFamily() {
        $("[placeholder=\"Город\"]").setValue("Казань");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов-Толмачев Сергей");
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__content").shouldBe(Condition.visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно забронирована на " + correctData));
    }

    @Test
    void testWhitWrongCity() {
        $("[placeholder=\"Город\"]").setValue("Нью-Йорк");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов-Толмачев Сергей");
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"city\"]").getText();
        Assertions.assertEquals("Доставка в выбранный город недоступна", text.trim());
    }

    @Test
    void testWhitWrongDate() {
        $("[placeholder=\"Город\"]").setValue("Казань");
        String correctData = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов-Толмачев Сергей");
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"date\"]").getText();
        Assertions.assertEquals("Заказ на выбранную дату невозможен", text.trim());
    }

    @Test
    void testWhitWrongName() {
        $("[placeholder=\"Город\"]").setValue("Казань");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Bumba Feel");
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"name\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void textWrongNumber() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов Сергей");
        $("[name=\"phone\"]").setValue("89098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"phone\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void testWithoutCheckBox() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов Сергей");
        $("[name=\"phone\"]").setValue("+79098888988");
        $(".button").click();
        String text = $("[data-test-id=\"agreement\"]").getText();
        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", text.trim());
    }

    @Test
    void testWithoutCity() {
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов-Толмачев Сергей");
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"city\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void testWithoutName() {
        $("[placeholder=\"Город\"]").setValue("Казань");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"name\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void testWithoutNumber() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String correctData = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов Сергей");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"phone\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void testWithoutData() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String correctData = LocalDate.now().format(DateTimeFormatter.ofPattern(""));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + correctData);
        $("[name=\"name\"]").setValue("Иванов Сергей");
        $("[name=\"phone\"]").setValue("+79098888988");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"date\"]").getText();
        Assertions.assertEquals("Неверно введена дата", text.trim());
    }

}

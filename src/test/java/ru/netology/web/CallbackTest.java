package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSuccessfullSendValidForm() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231212123");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldNotSendFormEmptyName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231212123");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSendFormEmptyPhone() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSendEmptyFlag() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231212123");
        form.findElement(By.cssSelector("[role='button']")).click();
        assertTrue(form.findElement(By.className("input_invalid")).isDisplayed());
    }

    @Test
    void shouldNotSendFormInvalidName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan Petrov");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231212123");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSendFormInvalidPhone() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+712312121234");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldGiveErrorOnlyName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231212123");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }
}


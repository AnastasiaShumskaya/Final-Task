package pageobjectsTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private static final By USERNAME = By.id("Email");
    private static final By PASSWORD = By.id("Passwd");
    private static final By SUBMIT_BUTTON = By.id("signIn");
    private static final By NEXT_BUTTON = By.id("next");
    private static final By SIGNEDIN_CHECKBOX = By.id("PersistentCookie");

    private WebDriver DRIVER;

    public LoginPage(WebDriver driver) {
        this.DRIVER = driver;
    }

    public HomePage login(String name, String pass) {

        DRIVER.findElement(USERNAME).sendKeys(name);
        DRIVER.findElement(NEXT_BUTTON).click();
        DRIVER.findElement(PASSWORD).sendKeys(pass);
        if (DRIVER.findElement(SIGNEDIN_CHECKBOX).getAttribute("checked") !=null)
            DRIVER.findElement(SIGNEDIN_CHECKBOX).click();
        DRIVER.findElement(SUBMIT_BUTTON).click();

        return new HomePage(DRIVER);
    }
}

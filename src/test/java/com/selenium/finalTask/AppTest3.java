package com.selenium.finalTask;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pageobjectsTask.HomePage;
import pageobjectsTask.LoginPage;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * Created by AnastasiaShumskaya on 1/16/2017.
 */
@Listeners(SomeListener.class)
public class AppTest3 extends Browser {

    private WebDriver driver;

    private static final String URL = "https://gmail.com";

    private static final String USERNAME1 = "seleniumtests10";
    private static final String USERNAME2 = "seleniumtests30";
    private static final String PASSWORD = "060788avavav";
    private static final Integer RUN = 1;//1- run locally; 2 - by Grid; 3 - by SauceLabs

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(String browser) throws MalformedURLException {

        switch (RUN) {
            case 1:
                driver = createDriver(browser);
                break;
            case 2:
                driver = createGridDriver(browser);
                break;
            case 3:
                driver = createSauceLabsDriver(browser);
                break;
            default:
                driver = createDriver(browser);
                break;
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(URL);
    }

    @AfterClass
    private void tearDown() {
        driver.close();
    }

    @Test(groups = "fast")
    @TestCaseId("ID-003")
    @Features("Login")
    @Stories("Verify the ability to send emails")
    public void googleTest3() throws InterruptedException {
        LoginPage googlePage = new LoginPage(driver);
        HomePage homePage = googlePage.login(USERNAME1, PASSWORD);
        homePage.sendEmail(USERNAME2, USERNAME1);
        homePage.signedOut();
        homePage = googlePage.login(USERNAME2, PASSWORD);

        Assert.assertTrue(homePage.emailHasCome(USERNAME1), "No email listed!");
        homePage.signedOut();
    }

    @Test(groups = "fast")
    @TestCaseId("ID-004")
    @Features("Login")
    @Stories("Verify that sent email appears in Sent Mail folder")
    public void googleTest4() throws InterruptedException {
        LoginPage googlePage = new LoginPage(driver);
        HomePage homePage = googlePage.login(USERNAME1, PASSWORD);
        homePage.sendEmail(USERNAME2, USERNAME1);
        homePage.goSentMail();

        Assert.assertTrue(homePage.emailHasCome(USERNAME2), "Letter was not sent!");
        homePage.signedOut();
    }

    @Test(groups = "fast")
    @TestCaseId("ID-005")
    @Features("Login")
    @Stories("Verify that deleted email is listed in Trash")
    public void googleTest5() {
        LoginPage googlePage = new LoginPage(driver);
        HomePage homePage = googlePage.login(USERNAME1, PASSWORD);
        String mail;
        mail = homePage.deleteEmail();

        Assert.assertEquals(homePage.emailPresenceInTrash(), mail, "No deleted email in Trash!");
        homePage.signedOut();
    }
}

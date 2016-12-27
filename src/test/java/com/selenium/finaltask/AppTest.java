package com.selenium.finaltask;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pageobjectsTask.HomePage;
import pageobjectsTask.LoginPage;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Listeners(SomeListener.class)
public class AppTest {

    private WebDriver driver;

    private static final String URL = "https://gmail.com";

    private static final String fileName = "src\\main\\resources\\credentials.txt";
    private static final String USERNAME1 = "seleniumtests10";
    private static final String USERNAME2 = "seleniumtests30";
    private static final String PASSWORD = "060788avavav";
    private static final String SIGN_IN_TEXT = "Sign in to continue to Gmail";

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(URL);
    }

    @AfterMethod
    private void tearDown() {
        //driver.close();
    }

    @DataProvider(name = "credentials")
    public  Object[][] getFromFile() throws IOException {

        File tFile = new File(fileName);
        FileReader fileReader = new FileReader(tFile);
        List<String> lines = new ArrayList<String>();

        try {
            String line;

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int fileSize = lines.size();

        Object[][] data = new Object[fileSize][];

        for (int i = 0; i < fileSize; i++) {
            String[] strArr = lines.get(i).split(" ");
            Object[] d = new Object[strArr.length];
            for (int j = 0; j < strArr.length; j++) {
                d[j] = strArr[j];
            }
            data[i] = d;
        }
        return data;
    }

    @Test(dataProvider = "credentials", groups = "fast")
    @TestCaseId("ID-001")
    @Features("Login")
    @Stories("Verify the ability to login to gmail with valid credentials")
    public void googleTest1(final String username, final String password) {
        LoginPage googlePage = new LoginPage(driver);
        HomePage homePage = googlePage.login(username, password);

        Assert.assertTrue(homePage.getTitle().contains(username+"@gmail.com"), "User is not signed in!");
    }

    @Test(dataProvider = "credentials", groups = "fast")
    @TestCaseId("ID-002")
    @Features("Login")
    @Stories("Verify the ability to logout from gmail")
    public void googleTest2(final String username, final String password) {
        LoginPage googlePage = new LoginPage(driver);
        HomePage homePage = googlePage.login(username, password);
        homePage.signedOut();

        Assert.assertTrue(homePage.getSource().contains(SIGN_IN_TEXT), "User is not signed out!");
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
    }

}

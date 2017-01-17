package com.selenium.finalTask;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pageobjectsTask.HomePage;
import pageobjectsTask.LoginPage;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by anastasiashumskaya on 1/17/2017.
 */
@Listeners(SomeListener.class)
public class AppTest2 extends Browser {

    private WebDriver driver;

    private static final String URL = "https://gmail.com";
    private static final String fileName = "src\\main\\resources\\credentials.txt";
    private static final String SIGN_IN_TEXT = "Sign in to continue to Gmail";
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
    @TestCaseId("ID-002")
    @Features("Login")
    @Stories("Verify the ability to logout from gmail")
    public void googleTest2(final String username, final String password) {
        LoginPage googlePage = new LoginPage(driver);
        HomePage homePage = googlePage.login(username, password);
        homePage.signedOut();

        Assert.assertTrue(homePage.getSource().contains(SIGN_IN_TEXT), "User is not signed out!");
    }
}

package com.selenium.finalTask;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anastasiashumskaya on 1/17/2017.
 */
public class Browser {

    protected ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    private static final String LOGIN = "kanapuska";
    private static final String ACCESS_KEY = "1e5855e2-accd-4e0f-a8e9-e20c3eb36fcd";
    private static final String GRID_URL = "http://localhost:4444/wd/hub";

    protected WebDriver createDriver(String browser) {

        switch (browser) {
            case "firefox":
                webDriver.set(new FirefoxDriver());
                return webDriver.get();
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
                webDriver.set(new ChromeDriver());
                return webDriver.get();
            default:
                System.out.println("browser : " + browser + " is invalid, Launching Firefox as browser of choice..");
                webDriver.set(new FirefoxDriver());
                return webDriver.get();
        }
    }

    protected WebDriver createSauceLabsDriver(String browser) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        capabilities.setPlatform(Platform.WINDOWS);
        webDriver.set(new RemoteWebDriver(
                new URL("https://" + LOGIN + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub"),
                capabilities));
        return webDriver.get();
    }

    protected WebDriver createGridDriver(String browser) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        capabilities.setPlatform(Platform.WINDOWS);
        URL url = new URL(GRID_URL);
        webDriver.set(new RemoteWebDriver(url, capabilities));
        return webDriver.get();
    }

}

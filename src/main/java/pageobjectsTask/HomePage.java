package pageobjectsTask;

import com.google.common.collect.Iterables;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {

    private final WebDriver DRIVER;

    private static final By SENTLIST = By.cssSelector("div[role=\"main\"] table[class=\"F cf zt\"] div[class=\"yW\"] span");
    private static final By MAIL_CHECKBOXES = By.cssSelector("div[role=\"main\"] table[class=\"F cf zt\"] tr td[class=\"oZ-x3 xY\"] div[role=\"checkbox\"]");

    @FindBy(css = ".gb_8a.gbii")
    private WebElement signOutSpan;

    @FindBy(id = "gb_71")
    private WebElement signOutButton;

    @FindBy(xpath = "//div[@class=\"nM\"]//div[@role=\"button\"]")
    private WebElement sendLetter;

    @FindBy(css = "textarea[name=\"to\"]")
    private WebElement toTextbox;

    @FindBy(css = "input[name=\"subjectbox\"]")
    private WebElement subject;

    @FindBy(css = "div[class=\"T-I J-J5-Ji aoO T-I-atl L3\"]")
    private WebElement sendButton;

    @FindBy(css = "a[href*=\"#sent\"]")
    private WebElement sentMailLink;

    @FindBy(css = "div[role=\"main\"] table[class=\"F cf zt\"] div[class=\"yW\"] span")
    private WebElement allLetters;

    @FindBy(css = "div[style=\"\"]>div[class*=\"nX\"]")
    private WebElement deleteButton;

    @FindBy(css = "a[href*=\"#trash\"]")
    private WebElement trashBox;

    @FindBy(css = "span[class=\"CJ\"]")
    private WebElement more;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.DRIVER = driver;
    }

    public String getTitle() {
        WebElement dynamicElement = (new WebDriverWait(DRIVER, 10))
                .until(ExpectedConditions.visibilityOf(signOutSpan));
        return DRIVER.getTitle();
    }

    public String getSource() {
        return DRIVER.getPageSource();
    }

    public void sendEmail(String toAddress, String fromAddress) {
        sendLetter.click();
        toTextbox.sendKeys(toAddress + "@gmail.com");
        subject.sendKeys(fromAddress);
        sendButton.click();
    }

    public boolean emailHasCome(String letterAuthor) {
        List<WebElement> my = DRIVER.findElements(SENTLIST);
        WebElement first = Iterables.getFirst(my,DRIVER.findElement(SENTLIST));

        return  first.getAttribute("email").contains(letterAuthor);
    }

    public void goSentMail() {
        sentMailLink.click();
        List<WebElement> dynamicElements = (new WebDriverWait(DRIVER, 5)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(SENTLIST)));
    }

    public String deleteEmail() {
        List<WebElement> my = DRIVER.findElements(SENTLIST);
        String email = my.get(0).getText();
        my = DRIVER.findElements(MAIL_CHECKBOXES);
        my.get(0).click();
        deleteButton.click();
        return email;
    }

    public String emailPresenceInTrash() {

        Actions builder = new Actions(DRIVER);
        builder.moveToElement(sentMailLink).build().perform();
        more.click();
        trashBox.click();
        List<WebElement> my = DRIVER.findElements(SENTLIST);
        WebElement first = Iterables.getFirst(my,DRIVER.findElement(SENTLIST));
        return  first.getText();
    }

    public LoginPage signedOut() {
        signOutSpan.click();
        signOutButton.click();
        try{
            Alert a = new WebDriverWait(DRIVER, 10).until(ExpectedConditions.alertIsPresent());
            if(a!=null){
                DRIVER.switchTo().alert().accept();
                return new LoginPage(DRIVER);
            }else{
                throw new Throwable();
            }
        }
        catch (Throwable e) {
            return new LoginPage(DRIVER);
        }
    }
}

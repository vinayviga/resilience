package pages.UIAPICookieIntegration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.Base;

public class SimpleFormAuthPage extends Base{
	
	@FindBy(xpath="//h1[text()='Page Not Found']")
	private WebElement errorMessage;
	
	@FindBy(xpath = "//a[text() = 'Sign Out']")
	private WebElement signOut;

	
	public SimpleFormAuthPage(WebDriver driver)
	{
		Base.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	

	public WebElement getErrorMessage() {
		return errorMessage;
	}
	
	public WebElement getSignOut() {
		return signOut;
	}
}

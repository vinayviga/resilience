package pages.APIUICookieIntegration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.Base;

public class OrangeHRMLeavesPage extends Base{
	
	@FindBy(xpath="//span[text()='Cancelled ']/i")
	private WebElement cancelFilter; 



	@FindBy(xpath="//button[text()=' Search ']")
	private WebElement searchButton;
	
	@FindBy(css="span[class='oxd-text oxd-text--span']")
	private WebElement leaveRecordsField;
	
	@FindBy(xpath = "//h6[text()='Overlapping Leave Request(s) Found']")
	private WebElement errorMessageField;
	
	@FindBy(xpath = "//p[text()='Balance not sufficient']")
	private WebElement errorMessageField2;
	
	
	public OrangeHRMLeavesPage(WebDriver driver)
	{
		Base.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public WebElement getCancelFilter() {
		return cancelFilter;
	}

	public WebElement getSearchButton() {
		return searchButton;
	}

	public WebElement getLeaveRecordsField() {
		return leaveRecordsField;
	}

	public WebElement getErrorMessageField() {
		return errorMessageField;
	}

	public WebElement getErrorMessageField2() {
		return errorMessageField2;
	}
	
	

}

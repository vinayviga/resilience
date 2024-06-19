package pages.APIUICookieIntegration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.Base;

public class OrangeHRMLeaveRequestPage extends Base{
	
	@FindBy(xpath="//div[@class='oxd-form-loader']")
	private WebElement loaderField;
	
	@FindBy(xpath="//i[@class='oxd-icon bi-caret-down-fill oxd-select-text--arrow']")
	private WebElement leaveTypeDD;
	
	@FindBy(xpath = "//span[contains(text(),'CAN')]")
	private WebElement leaveTypeOption;
	
	@FindBy(xpath = "//label[text()='From Date']/../..//input")
	private WebElement fromDate;
	
	@FindBy(xpath = "//textArea")
	private WebElement reasontextArea;
	
	@FindBy(xpath = "//button[text()=' Apply ']")
	private WebElement applyButton;
	
	
	public OrangeHRMLeaveRequestPage(WebDriver driver)
	{
		Base.driver = driver;
		PageFactory.initElements(driver, this);
	}


	public WebElement getLoaderField() {
		return loaderField;
	}


	public WebElement getLeaveTypeDD() {
		return leaveTypeDD;
	}


	public WebElement getLeaveTypeOption() {
		return leaveTypeOption;
	}


	public WebElement getFromDate() {
		return fromDate;
	}


	public WebElement getReasontextArea() {
		return reasontextArea;
	}


	public WebElement getApplyButton() {
		return applyButton;
	}

	
}

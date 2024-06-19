package stepdefinitions;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.asserts.SoftAssert;

import base.Base;
import base.Support;
import groovyjarjarantlr4.v4.misc.OrderedHashMap;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pages.APIUICookieIntegration.OrangeHRMLeaveRequestPage;
import pages.APIUICookieIntegration.OrangeHRMLeavesPage;
import pages.APIUICookieIntegration.OrangeHRMLoginPage;

public class APIUIStepDefinition extends Base{
	
	static SoftAssert assertion = new SoftAssert();
	OrangeHRMLeaveRequestPage leaveRequest = new OrangeHRMLeaveRequestPage(driver);
	OrangeHRMLeavesPage leavesPage = new OrangeHRMLeavesPage(driver);
	OrangeHRMLoginPage loginPage = new OrangeHRMLoginPage(driver);
	static String sessionCookie = "";
	List<String> leaveIDs = new ArrayList<String>();
	
	@BeforeAll
	public static void setup()
	{
		Base.browserSetup();
	}
	
	@Given("I login successfully {string}")
	public void login(String LoginPageURL)
	{
		driver.get(LoginPageURL);
		loginPage.getUsername().sendKeys("Admin");
		loginPage.getPassword().sendKeys("admin123");
		loginPage.getSubmitButton().click();
		Support.waitTillPageLoad(wait);
		assertion.assertTrue(driver.getTitle().equals("OrangeHRM"));
	}
	
	@When("I extract session data")
	public void sessionData()
	{
		// extracting session data from cookie to trigger API requests
				
				try {
					Cookie session = driver.manage().getCookieNamed("orangehrm");
					System.out.println(session.getName() + "=" + session.getValue());
					sessionCookie = session.getName() + "=" + session.getValue();
				} catch (Exception e) {
					// In case we fail to extract session data from the cookie
					e.printStackTrace();
				}
				assertion.assertTrue(!sessionCookie.isBlank(),"Asserting that the session cookie variable is not empty");
	}
	
	@Then("I validate the session data by getting the count of balance leaves")
	public void validateSessionData()
	{
		assertion.assertTrue(Support.getLeaveBalance(sessionCookie)>20,"validating the session cookies");
	}
	
	@Given("I goto the leaves page {string}")
	public void gotoLeavesPage(String LeavesPageURL)
	{
		driver.get(LeavesPageURL);
	}
	
	@Given("Apply all the leaves in the background")
	public void applyLeave()
	{
		// basic arraylist to store the ID's of all the leaves
		leaveIDs = new ArrayList<String>();
		int dateCount = 0;
		
		
		Map<String,Object> body = new OrderedHashMap<String, Object>();
		Map<String,Object> subBody = new OrderedHashMap<String, Object>();
		subBody.put("type", "full_day");
		body.put("leaveTypeId", 7);
		body.put("fromDate", "");
		body.put("toDate", "");
		body.put("comment", "test");
		body.put("duration", subBody);

		// creating leave requests
		while (Support.getLeaveBalance(sessionCookie) >1) {
			dateCount++;
			String formattedNumber = "2024-05-"+String.format("%02d", dateCount);
			body.put("fromDate", formattedNumber);
			body.put("toDate", formattedNumber);
			
			Response postResponse = given().contentType(ContentType.JSON).header("cookie", sessionCookie)
					// Please do make changes to the year and month accordingly
					.body(body)
					.when().post("https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/leave/leave-requests");

			JsonPath jsonPathEvaluator = postResponse.jsonPath();

			System.out.println("post response code " + postResponse.getStatusCode());
			if (postResponse.statusCode() == 200) {
				
				leaveIDs.add(jsonPathEvaluator.get("data.id").toString());
			}
		}
		assertion.assertTrue(leaveIDs.size()>0);
	}
	
	@When("I check for the upper limit by creating an extra leave in apply leave page {string}")
	public void upperLimitCheck(String LeaveApplyPageURL)
	{
		try 
		{
			// refreshing the page and checking whether the entries are reflecting on the UI
			driver.navigate().refresh();
			Support.waitTillPageLoad(wait);
			leavesPage.getCancelFilter().click();
			wait.until(ExpectedConditions.elementToBeClickable(leavesPage.getSearchButton())).click();
			Support.waitTillPageLoad(wait);
			assertion.assertTrue(wait.until(ExpectedConditions.visibilityOf(leavesPage.getLeaveRecordsField())).getText().contains("Records Found"));
			
			// create an extra leave to validate the error message
			driver.get(LeaveApplyPageURL);
			wait.until(ExpectedConditions.visibilityOf(leaveRequest.getReasontextArea()));
			
			try {
			wait.until(ExpectedConditions.invisibilityOf(leaveRequest.getLoaderField()));
			}
			catch (Exception e) {
				// TODO: handle exception
			}
				
				
			leaveRequest.getLeaveTypeDD().click();
			leaveRequest.getLeaveTypeOption().click();
			leaveRequest.getFromDate().clear();
			leaveRequest.getFromDate().sendKeys("2024-05-02");
			leaveRequest.getReasontextArea().sendKeys("test");
			leaveRequest.getApplyButton().click();
			boolean assertValue  = false;
			try {
			assertValue =wait.until(ExpectedConditions.visibilityOf(leavesPage.getErrorMessageField())).isDisplayed();
			}catch (Exception e) {
				// TODO: handle exception
				assertValue = wait.until(ExpectedConditions.visibilityOf(leavesPage.getErrorMessageField2())).isDisplayed();
			}
			assertion.assertTrue(assertValue);
			
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Then("I verify the error message and cancel all the leaves created")
	public void cleanup()
	{
		
		for (String leaveID : leaveIDs) {
			// cleanup, canceling the leave requests and closing the browser
			Response putResponse = given().header("Content-Type", "application/json")
					.header("cookie", sessionCookie).body("{\r\n" + "    \"action\": \"CANCEL\"\r\n" + "}").when()
					.put("https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/leave/employees/leave-requests/"+leaveID);

			System.out.println("put response code for leave cancellation " + putResponse.getStatusCode());
			assertion.assertEquals(putResponse.getStatusCode(), 200);
		}
	}
	
	
	@AfterAll
	public static void teardownAPIUI()
	{
		Base.browserQuit();
	}

}

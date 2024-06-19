package stepdefinitions;

import static io.restassured.RestAssured.given;

import org.openqa.selenium.Cookie;
import org.testng.asserts.SoftAssert;

import api.uiapi.UIAPIEndpoints;
import pages.UIAPICookieIntegration.SimpleFormAuthPage;
import base.Base;
import base.Property;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UIAPIStepDefinitions extends Base {
	static SoftAssert assertion = new SoftAssert();
	String accessToken = "";
	SimpleFormAuthPage simpleFormAuthPage = new SimpleFormAuthPage(driver);
	
	
	@BeforeAll
	public static void setup()
	{
		Base.browserSetup();
	}
	@Given("I navigate to authenticationtest.com {string}")
	public void gotoAuthenticationTest(String authenticationtestURL)
	{
		driver.get(authenticationtestURL);
	}
	
	@When("Create session and set cookies in the browser")
	public void setSessionCookies()
	{
		RestAssured.baseURI = Property.propertyReader("authenticationTestURL");
		Response auth_response = RestAssured.given().contentType(ContentType.URLENC)
				.formParam("email", Property.propertyReader("authenticationtestEmail"))
				.formParam("password", Property.propertyReader("authenticationtestPassword"))
				.post(UIAPIEndpoints.simpleFormAuthLoginEP);

		System.out.println(auth_response.statusCode());

		// Retrieve RestAssured cookies as Cookies object
		io.restassured.http.Cookies restAssuredCookies = auth_response.detailedCookies();
		System.out.println(restAssuredCookies);

		// Convert RestAssured cookies to Selenium cookies and add them to WebDriver
		restAssuredCookies.forEach(cookie -> {
			Cookie seleniumCookie = new Cookie(cookie.getName(), cookie.getValue());
			driver.manage().addCookie(seleniumCookie);
			
			assertion.assertTrue(driver.manage().getCookies().size()>0);
			
		});
	}
	
	@Then("Navigate to home page and verify the created cookies {string}")
	public void verifySession(String loginSuccessURL) throws InterruptedException
	{
		Thread.sleep(4000);
		driver.get(loginSuccessURL);
		Thread.sleep(4000);
		assertion.assertTrue(simpleFormAuthPage.getSignOut().isDisplayed());
	}
	
	@Given("Visit course details page {string}")
	public void visitDetailsPage(String getcoursedetailsURL)
	{
		driver.get("https://rahulshettyacademy.com/oauthapi/getcoursedetails");
		assertion.assertTrue(simpleFormAuthPage.getErrorMessage().isDisplayed());
	}
	
	@When("Generate oauth session id")
	public void getSessionID()
	{
		/* creating a session ID and passing it through cookies to selenium */
		String response =

				given().formParams("client_id",
						Property.propertyReader("rahulshettyacademyClientID"))
				.formParams("client_secret", Property.propertyReader("rahulshettyacademyClientSecret"))
				.formParams("grant_type", "client_credentials").formParams("scope", "trust").when().log().all()

				.post(UIAPIEndpoints.oAuthTokenURL).asString();

		System.out.println(response);
		JsonPath jsonPath = new JsonPath(response);
		accessToken = jsonPath.getString("access_token");
		assertion.assertTrue(!accessToken.equals(""));
		System.out.println(accessToken);
	}
	
	@Then("Verify the oauth session ID {string}")
	public void verifyOauthSession(String getCourseDetailsTokenURL) throws InterruptedException
	{
		Thread.sleep(4000);
		driver.get(getCourseDetailsTokenURL+ accessToken);
		Thread.sleep(4000);
		System.out.println("Title is : "+driver.getTitle());
		assertion.assertTrue(!driver.getTitle().equals(""));

		driver.get("file:///C:/Users/Vinay/Downloads/dictator.gif");// remove this URL and you can use your own gif
		Thread.sleep(6000);
	}
	
	@AfterAll
	public static void teardownUIAPI()
	{
		Base.browserQuit();
	}

}

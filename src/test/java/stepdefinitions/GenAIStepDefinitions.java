package stepdefinitions;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import api.genai.GenAIServices;
import base.Base;
import base.GoogleServiceActions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;


public class GenAIStepDefinitions extends Base{
	
	public static Map<String, String> employeeData;
	public static List<List<String>> users;
	
	@BeforeAll
	public static void setupGenAI()
	{
		Base.browserSetup();
	}
	
	@Given("Generating the employee data")
	public void dataGeneration() throws IOException
	{
		// Step 1: Generating the employee data
		employeeData = GoogleServiceActions.aiDataGenerator();
	}
	
	@When("Passing the employee data to reqres.in and creating the users {string}")
	public void userRegistration(String SheetURL) throws ClientProtocolException, IOException
	{
		driver.get(SheetURL);
		// Step 2: Passing the employee data to reqres.in and creating the users
		users = new ArrayList<List<String>>();
		int count = 0;
		for (Map.Entry<String, String> entry : employeeData.entrySet()) {
			JsonPath path = GenAIServices.reqresPost(entry);
			users.add(new ArrayList<>());
			users.get(count).add(path.getString("name"));
			users.get(count).add(path.getString("job"));
			users.get(count).add(path.getString("id"));
			users.get(count).add(path.getString("createdAt"));
			count++;
		}
	}
	
	@When("Updating the employee details sheet")
	public void sheetUpdate() throws IOException, GeneralSecurityException
	{
		// Step 3: updating the employee details sheet
		GoogleServiceActions.sheetAppend(users);
	}
	
	@Then("Deleting all the users and updating the same in the sheet {string}")
	public void userCleanup(String SheetURL) throws IOException, GeneralSecurityException, InterruptedException
	{
		// Step 4: deleting all the users and updating the same in the sheet

				Map<String, String> seamus = new HashMap<String, String>();
				seamus.put("SEAMUS FINNIGAN", "GLAD YOU ASKED!!");
				System.out.println(seamus);
				List<List<String>> finnigan = new ArrayList<List<String>>();
				int count = 0;
				for (Map.Entry<String, String> entry : seamus.entrySet()) {
					JsonPath path = GenAIServices.reqresPost(entry);
					finnigan.add(new ArrayList<>());
					finnigan.get(count).add(path.getString("name"));
					finnigan.get(count).add(path.getString("job"));
					finnigan.get(count).add(path.getString("id"));
					finnigan.get(count).add(path.getString("createdAt"));
					count++;
				}
				GoogleServiceActions.sheetAppend(finnigan);

				for (int i = 0; i < users.size(); i++) {
					GenAIServices.reqresDelete(users.get(i).get(2));

				}
				GenAIServices.reqresDelete(finnigan.get(0).get(2));
				GoogleServiceActions.deleteCells(users.size() + 1);
				driver.get("file:///C:/Users/Vinay/Downloads/finnigan.gif");
				Thread.sleep(3000);
				driver.get(SheetURL);
				Thread.sleep(3000);
	}
	
	
	@AfterAll
	public static void teardownGenAI()
	{
		Base.browserQuit();
	}

}

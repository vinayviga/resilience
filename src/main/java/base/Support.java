package base;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.JsonElement;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Support {

	//wait method to wait until page is completely loaded
	public static ExpectedCondition<Object> waitTillPageLoad(WebDriverWait wait) {
		// Wait for the 'document.readyState' to be 'complete'
		return new ExpectedCondition<Object>() {
			@Override
			public Object apply(WebDriver driver) {
				return ExpectedConditions.jsReturnsValue("return document.readyState").equals("complete");
			}
		};
	}
	
	// method to get the latest count on available leaves
		public static int getLeaveBalance(String sessionCookie) {
			Response postResponse = given()
					.header("Content-Type", "application/json").header("cookie", sessionCookie)
					.when()
					.get("https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/leave/leave-balance/leave-type/7");

			JsonPath jsonPathEvaluator = postResponse.jsonPath();
			int value = 0;
			try {
			value = jsonPathEvaluator.get("data.balance.balance");
			}catch (Exception e) {
				// TODO: handle exception
				float data = jsonPathEvaluator.get("data.balance.balance");
				value = Math.round(data);
			}
			

			System.out.println("remaining number of leaves " + value);

			return value;
		}
		
		 public static String mapToStringPayloadConvert(Map m)
		    {
		    	String s = m.toString().replace("{", "{\"").replace("}", "\"}").replace("=", "\":\"").replace(", ", "\",\"");
		        return s.replace("\"{\"\"}\"", "{}");
		    }
	
}

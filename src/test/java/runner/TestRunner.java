package runner;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "stepdefinitions",
        plugin = {"pretty", "html:target/cucumber-reports"},
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
	
	 private static boolean isLastScenario = false;

	    @After
	    public void afterScenario(Scenario scenario) {
	        if (scenario.getSourceTagNames().contains("@LastScenario")) {
	            isLastScenario = true;
	        }
	        if (isLastScenario) {
	            runAfterAllScenarios();
	        }
	    }

	    private void runAfterAllScenarios() {
	        // Code to run after all scenarios
	        System.out.println("All scenarios finished!");
	        isLastScenario = false; // Reset for the next run
	    }
}

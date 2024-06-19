#Author: Vinay Gandhi Pachigoolla
Feature: Using API tests to boost UI tests - Data Generation


  Scenario Outline: Logging in and storing session data
    Given I login successfully <LoginPageURL>
    When I extract session data
    Then I validate the session data by getting the count of balance leaves
    
    Examples:
    |LoginPageURL																												 |
    |"https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"|
    


  Scenario Outline: Verifying the upper limit and canceling leaves
    Given I goto the leaves page <LeavesPageURL>
    And Apply all the leaves in the background
    When I check for the upper limit by creating an extra leave in apply leave page <LeaveApplyPageURL>
    Then I verify the error message and cancel all the leaves created
    
    Examples:
    | LeavesPageURL																																	| LeaveApplyPageURL																												 |
    |"https://opensource-demo.orangehrmlive.com/web/index.php/leave/viewMyLeaveList"|"https://opensource-demo.orangehrmlive.com/web/index.php/leave/applyLeave"|
    
	#@LastScenario
	#Scenario: cleanup
	#Given Browser cleanup

    
#Author: Vinay Gandhi Pachigoolla

Feature: Using API tests to boost UI tests - Session Generation

  Scenario Outline: Generating basic authentication session
    Given I navigate to authenticationtest.com <authenticationtestURL>
    When Create session and set cookies in the browser
    Then Navigate to home page and verify the created cookies <loginSuccessURL>
    
    Examples:
    |authenticationtestURL													 |loginSuccessURL																|
    |"https://authenticationtest.com/simpleFormAuth/"|"https://authenticationtest.com/loginSuccess/"|

	Scenario Outline: Generating oauth session
	    Given Visit course details page <getCourseDetailsURL>
	    When Generate oauth session id
	    Then Verify the oauth session ID <getCourseDetailsTokenURL>
	    
	    Examples:
	    |getCourseDetailsTokenURL																								 |getCourseDetailsURL																				|
	    |"https://rahulshettyacademy.com/oauthapi/getCourseDetails?access_token="|"https://rahulshettyacademy.com/oauthapi/getCourseDetails"|
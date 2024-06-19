Feature: Generative AI data driven testing, a magical example


  Scenario Outline: Hogwarts berozgar yojna
    Given Generating the employee data
    When Passing the employee data to reqres.in and creating the users <SheetURL>
    And Updating the employee details sheet
    Then Deleting all the users and updating the same in the sheet <SheetURL>
    
    Examples:
    |	SheetURL																																											 |																																							 	 
    |"https://docs.google.com/spreadsheets/d/1SP_qfbkIEYf1P8DIV8d-yiDCXCFlUjzvzk0rjbnrTdQ/edit#gid=0"|

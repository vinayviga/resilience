# resilience
framework built from mistakes

download/clone the project and **import as a maven project in your IDE**

don't forget to use your own **credential.json** and **client.properties** file in **src\main\java\base package**
and make sure to update your details in **src\test\resources\testData.properties** file

Too much heat! Cool it with Cucumber...

A lot of QA folk find themselves under too much *heat** when the developers **behavior** towards them gets more condescending and the product team's **behaviour** gets more skeptical towards them. One of the main reasons is the lack of understanding of testing process, perspective and goals. A foolish way of dealing with that fix is assigning unrelated work to QA team like cleaning the toilets or assign freshly created production issues RCA to QA without atleast giving them read access to codebase, database and the implementation team which cause **burnouts**. A non-foolish way to fix the heat is using **Behaviour Driven Development(BDD)** and the tool I have chosen is **Cucumber**. even I have received some heat because I have not been using a framework to package my code and I have decided to build a framework with Cucumber to cool it down.

The benefit of packaging a project in a framework is to provide readability, reusability and handle complexity atleast in my case. One of the most important aspects of a framework which provides all the above qualities are **Design Patterns**. You can learn more about them here(https://www.javatpoint.com/design-patterns-in-java)
I have used the following design patterns to build this framework:
1. POM(Page Object Model)
2. Singleton
3. Builder

Before we dive into the code, lets get a brief understanding of BDD. It focuses on the collaborative work of developers, testers, and non-technical or business participants in a software project. BDD encourages teams to use conversation and concrete examples to formalize a shared understanding of how the application should behave, Interesting right?
Now let's focus on how to implement BDD in cucumber. There is a language called **Gherkin** which makes it happen. 
->Gherkin is a human-readable language used in Cucumber, to define application behavior in a structured format. 
->Given/When/Then are the core keywords or as I call them, **the holy trinity of Gherkin.**
->The test scenarios and their steps are defined using the above keywords and saved in a .feature file which can be run individually or by using a runner library like testNG or Junit.

You can learn more about cucumber and BDD in the links below:
-> https://school.cucumber.io/collections
-> https://cucumber.io/docs/

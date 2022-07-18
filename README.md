# API Automation

The purpose of the project was to develop automated tests for the API from [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com) 
website. The project includes sample validations: getting, creating, updating, deleting posts, comments, etc. Automated 
tests were developed for single sync and multiple async requests.


## Tech stack

- Main programming language: `Java`
- Java Development Kit: `Java 17 (Oracle OpenJDK version 17.0.2)`
- Developed on IDE: `IntelliJ IDEA 2021.3.2 (Community Edition) Build #IC-213.6777.52`
- Developed on Device: `MacBook Air (M1, 2020), 8GB RAM`
- Used Operating System: `macOS BigSur v11.3.1 (20E241)`
- Testing framework: `JUnit Jupiter v5.6.2`
- Behavior-Driven Development (BDD) support tool: `Cucumber v6.0.0`
- Build automation tool: `Apache Maven v3.8.6`
- Data binding tool: `Jackson Databind v2.13.0`


### Prerequisites

1. Java Development Kit (JDK)

You can use the latest version from the [Oracle](https://www.oracle.com/java/technologies/downloads/) website
and setup _JAVA_HOME_ environmental path.


2. Maven

You can use the following instruction from the [Maven install](https://maven.apache.org/install.html) website
and download Maven from here [Maven download](https://maven.apache.org/download.cgi).

## Execution

To execute tests, please go to project folder and execute the following command in terminal:
```
mvn test -Dmaven.test.failure.ignore=true
```

## Development

### Overview
All test cases are described in feature files placed in test _resources_ folder. Test are executed by _TestRunner_ 
classes in _jsonPlaceholderTypicode.apiTests_ package, separated for each request method. The steps are developed in 
_Steps.class_.

### Test Runner

TestRunner classes execute test scenarios described in feature file. Because of using Cucumber the annotation
_@RunWith(Cucumber.class)_ has been used.

### Steps

Here are developed all the steps described in feature file.  Before each scenario there are set up (annotation _@Before_) 
including elements such as adjusting system properties, initiate global variables, etc. After each scenario there is 
a clean-up (annotation _@After_). There are also annotations _@Before_, _@When_, _@Then_ which correspond the values 
from feature file and here the specific step is developed. 

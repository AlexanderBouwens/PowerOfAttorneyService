# PowerOfAttorney
Excercise for the Rabobank

#### How to execute
Start the stub by running <code>mvn compile exec:java</code>.   
Start the PowerOfAttorney service by running <code>mvn spring-boot:run</code>

The application runs on https://localhost:8443/accountoverview/{userId}  
Log in with user: **user** and the password that is generated at start up, in the console at _**Using generated security password: password**_.  
When using IntelliJ or Eclipse to run the application, don't forget to add the generated sources folder as a source folder on the classpath. 

#### Goal
Build a REST API presenting aggregated information from different services  
Only show data that a user is actually authorized for  
Handle any server errors you might run into gracefully  

#### Assumptions
Only users with grantee or grantor roles can view information they are authorized for, 
due to the fact that the credit card, debit card and account sources do not provide overviews or provide retrieval of cards/accounts by owner/holder name.  
Therefore if a user is a credit card holder but does not appear on a PowerOfAttorney as grantee or grantor he cannot access his information.

#### Bugs in the apiDef
* Account and account api client are missing in the definition
* Spelling errors

#### Design decisions
Create client services for each available rest data source, each using the <code>AbstractResourceService</code> to do the actual calls.   
The <code>AuthorizationService</code> combines the retrieved data into a list of <code>AccountOverviews</code> within an <code>AuthorizedAccountOverview</code>  
Used <code>openapi-generator-maven-plugin</code> to generate the model based on the apiDef.yaml, so that future changes to model in the yaml can be easily adopted.  
Used the GET method to keep things simple.  
I decided to add the Account to the apidef.yaml so it can be generated, but not the Account api, this should be the responsibility of the owner of the account api.   

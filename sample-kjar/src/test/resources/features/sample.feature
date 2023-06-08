Feature: Sample Rules Tests
    This is a sample set of unit test requests used to demonstrate the Cucumber and Embedded Engine adaptors.

	Background: Specify the ruleset and shared data to be used for all scenarios
	    Given a ruleset where the knowledge session ID is "default-stateless" and the KJAR coordinates are ["com.ibm.bamoe.dmoe","sample-kjar","1.0.0-SNAPSHOT"]
		Given the logging level set to "DEBUG"

    Scenario: Test #1
        Given loan application "00001" for applicant: "Walter White" age: 52, amount: 100, deposit: 125 with a maximum amount allowed of 2000
        When the ruleflow named "ruleflow" is executed
        Then application "00001" should be approved 
        And the number of rules that have fired equals 1 

    Scenario: Test #2
        Given loan application "00002" for applicant: "Hank Shrader" age: 55, amount: 100, deposit: 125 with a maximum amount allowed of 2000
        When the ruleflow named "ruleflow" is executed
        Then application "00002" should be approved 
        And the number of rules that have fired equals 1 

    Scenario: Test #3
        Given loan application "00003" for applicant: "Saul Goodman" age: 65, amount: 50, deposit: 125 with a maximum amount allowed of 2000
        When the ruleflow named "ruleflow" is executed
        Then application "00003" should be approved 
        And the number of rules that have fired equals 1 

    Scenario: Test #4
        Given loan application "00004" for applicant: "Jesse Pinkman" age: 19, amount: 3000, deposit: 50 with a maximum amount allowed of 2000
        When the ruleflow named "ruleflow" is executed
        Then application "00004" should not be approved 
        And the rule named "NotAdultApplication" has fired 
        And the rule named "SmallDepositApprove" has not fired 
        And the number of rules that have fired equals 1

    Scenario: Test #5
        Given loan application "00005" for applicant: "Hector Salamonca" age: 92, amount: 1200, deposit: 1000 with a maximum amount allowed of 900
        When the ruleflow named "ruleflow" is executed
        Then application "00005" should not be approved 
        And the number of rules that have fired equals 2 
        And the rule named "LargeDepositReject" has fired
        And the rule named "TooOldApplication" has fired
        And the rule named "SmallDepositReject" has not fired 

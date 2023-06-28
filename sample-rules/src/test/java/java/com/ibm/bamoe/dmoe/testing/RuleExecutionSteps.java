package com.ibm.bamoe.dmoe.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.datatable.DataTable;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.ibm.bamoe.dmoe.adaptors.engine.RuleSet;
import com.ibm.bamoe.dmoe.adaptors.engine.Trace;
import com.ibm.bamoe.dmoe.adaptors.engine.TracingLevel;
import com.ibm.bamoe.dmoe.adaptors.engine.MavenCoordinates;
import com.ibm.bamoe.dmoe.adaptors.engine.KnowledgeContainer;

import com.ibm.bamoe.dmoe.sample.LoanApplication;
import com.ibm.bamoe.dmoe.sample.Applicant;

public class RuleExecutionSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExecutionSteps.class);
    private static final String CONTENT_TYPE            = "application/json";
    private static final String LOGGER_TESTING          = "com.com.ibm.dba.dmoe.adaptors.testing";
    private static final String LOGGER_TESTING_ENGINE   = "com.ibm.dba.dmoe.adaptors.engine";
    private static final String LOGGER_LISTENERS        = "com.ibm.dba.dmoe.adaptors.engine";

    // This holds our state between Cucumber steps (using pico-container)
    private RulesTestingContext context;

    public RuleExecutionSteps(RulesTestingContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(){
    }	

    /*
     * Standard Rule Engine API entries, for basic rule execution via Cucumber
     */
    @Given("a ruleset where the knowledge session ID is {string} and the KJAR coordinates are [{string},{string},{string}]")
    public void setRuleSet(String knowledgeSessionID, String kjarGroupId, String kjarArtifactId, String kjarVersion) {

        RuleSet ruleSet = new RuleSet();
        ruleSet.setKnowledgeSessionID(knowledgeSessionID);

        MavenCoordinates gav = new MavenCoordinates();
        gav.setGroupId(kjarGroupId);
        gav.setArtifactId(kjarArtifactId);
        gav.setVersion(kjarVersion);
        ruleSet.setMavenCoordinates(gav);

        // Set the ruleset on the container
        KnowledgeContainer kContainer = context.getRuleEngineAdaptor().createKnowledgeContainer(ruleSet);
        context.getRuleEngineAdaptor().setKnowledgeContainer(kContainer);
    }

    @Given("a data file location of {string}")
    public void setDataFileLocation(String location) {
        context.setDataFileLocation(location);
    }

    @Given("the logging level set to {string}")
    public void setLoggingLevel(String loggingLevel) {
        context.setLoggingLevel(new String(loggingLevel));
    }
    
    @When("the data file named {string} is executed")
    public void executeCommands(String dataFileName) {

        try {

            // Set the logging level while we are executing this ruleset
            setLoggingLevels(context.getLoggingLevel());

            // Read the contents of the data file, which should be identical to a KIE Server command payload
            String sDataFile = context.getDataFileLocation() + File.separator + dataFileName;
            File fDataFile = new File(sDataFile);

            // Print the local working directory
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            LOGGER.debug("Processing test data: filename=" + sDataFile);

            if (fDataFile.exists() && !fDataFile.isDirectory()) { 

                String payload = new String(Files.readAllBytes(Paths.get(sDataFile)));
                context.setResponse(context.getRuleEngineAdaptor().executeCommands(payload, CONTENT_TYPE, TracingLevel.All));
            }

        } catch (Exception e) {
            LOGGER.error("Exception: msg=" + e.getMessage());
        }
    }

    @When("the ruleflow-group named {string} is executed")
    public void executeRuleflowGroup(String ruleFlowGroup) throws Exception {

        // Set the logging level while we are executing this ruleset
        setLoggingLevels(context.getLoggingLevel());

        // Execute the ruleflow group
        context.getRuleEngineAdaptor().getKnowledgeContainer().getRuleSet().setRuleFlowGroup(ruleFlowGroup);
        context.setResponse(context.getRuleEngineAdaptor().executeStateless(context.getFacts(), TracingLevel.All));
    }

    @When("the ruleflow named {string} is executed")
    public void executeRuleflow(String ruleFlow) throws Exception {

        // Set the logging level while we are executing this ruleset
        setLoggingLevels(context.getLoggingLevel());

        // Execute the ruleflow
        context.getRuleEngineAdaptor().getKnowledgeContainer().getRuleSet().setRuleFlowID(ruleFlow);
        context.setResponse(context.getRuleEngineAdaptor().executeStateless(context.getFacts(), TracingLevel.All));
    }

    @Then("the rule named {string} has fired")
    public void ruleFired(String ruleName) {

        Trace trace = context.getResponse().getTrace();
        boolean ruleFired = false;

        for (String rule : trace.getRulesFired()) {
            if (rule.equals(ruleName)) {
                ruleFired = true;
                break;
            }
        }

        assertEquals(ruleFired, true);
    }

    @Then("the rule named {string} has not fired")
    public void ruleDidNotFire(String ruleName) {

        Trace trace = context.getResponse().getTrace();
        boolean ruleDidNotFire = false;

        for (String rule : trace.getRulesNotFired()) {
            if (rule.equals(ruleName)) {
                ruleDidNotFire = true;
                break;
            }
        }

        assertEquals(ruleDidNotFire, true);
    }

    @Then("the rule named {string} has fired {int} times")
    public void ruleFiredNumberOfTimes(String ruleName, int numberOfTimes) {

        Trace trace = context.getResponse().getTrace();
        int numberTimesFired = 0;

        for (String rule : trace.getRulesFired()) {
            if (rule.equals(ruleName)) {
                numberTimesFired++;
            }
        }

        assertEquals(numberTimesFired, numberOfTimes);
    }

    @Then("the number of rules that have fired equals {int}")
    public void rulesFiredCount(int number) {

        Trace trace = context.getResponse().getTrace();
        assertEquals(number, trace.getRulesFiredCount());
    }

    @Then("the number of rules that have not fired equals {int}")
    public void rulesDidNotFireCount(int number) {

        Trace trace = context.getResponse().getTrace();
        assertEquals(number, trace.getRulesNotFiredCount());
    }

    @Then("rule execution took less than {int} milliseconds")
    public void executionDurationInMilliseconds(int milliseconds) {

        Trace trace = context.getResponse().getTrace();
        assertTrue("Milliseconds", trace.getExecutionDuration().getMilliseconds() < milliseconds);
    }

    @Then("rule execution took less than {int} seconds, and {int} milliseconds")
    public void executionDuration(int seconds, int milliseconds) {

        Trace trace = context.getResponse().getTrace();
        long expectedDuration = milliseconds;
        long actualDuration = trace.getExecutionDuration().getMilliseconds();
 
        // Calculate expected duration
        if (seconds > 0) {
            expectedDuration = ((seconds * 1000) + milliseconds);
        }

        // Calculate actual duration
        if (trace.getExecutionDuration().getSeconds() > 0) {
            actualDuration = ((trace.getExecutionDuration().getSeconds() * 1000) + trace.getExecutionDuration().getMilliseconds());
        }

        assertTrue("Execution Duration (milliseconds)", actualDuration < expectedDuration);
    }

    @Given("loan application {string} for applicant: {string} age: {int}, amount: {int}, deposit: {int} with a maximum amount allowed of {int}")
    public void createApplication(String ID, String applicantName, int applicantAge, int amount, int deposit, int maxAmount) {
    
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(ID);
        loanApplication.setAmount(new Integer(amount));
        loanApplication.setDeposit(new Integer(deposit));
        loanApplication.setMaxAmount(new Integer(maxAmount));

        Applicant applicant = new Applicant();
        applicant.setName(applicantName);
        applicant.setAge(new Integer(applicantAge));
        loanApplication.setApplicant(applicant);

        // Add the fact to working memory
        context.addFact(loanApplication.getId(), loanApplication);
        LOGGER.debug("Processing loan application: " + loanApplication.getId() + " for " + loanApplication.getApplicant().getName() + "...");
    }

    @Given("a maximum amount of {int}")
    public void setMaxAmount(int maxAmount) {

        context.addGlobal("maxAmount", maxAmount);
        LOGGER.debug("Setting maximum loan amount to: " + maxAmount + "...");
    }

    @Then("application {string} should be approved")
    public void isApplicationApproved(String ID) {

        LoanApplication loanApplication = (LoanApplication) context.getFact(ID);
        assertEquals(loanApplication.isApproved(), true);
    }

    @Then("application {string} should not be approved")
    public void isApplicationNotApproved(String ID) {

        LoanApplication loanApplication = (LoanApplication) context.getFact(ID);
        assertEquals(loanApplication.isApproved(), false);
    }

    // This method overrides the current logging level set in the logback.xml configuration file in the KJAR
    private void setLoggingLevels(String loggingLevel) {

        // Testing Logger
        ch.qos.logback.classic.Logger testingLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LOGGER_TESTING);
        testingLogger.setLevel(Level.toLevel(loggingLevel));

        // Testing Engine Logger
        ch.qos.logback.classic.Logger testingEngineLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LOGGER_TESTING_ENGINE);
        testingEngineLogger.setLevel(Level.toLevel(loggingLevel));

        // Listener Logger
        ch.qos.logback.classic.Logger listenerLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LOGGER_LISTENERS);
        listenerLogger.setLevel(Level.toLevel(loggingLevel));
   }
}
package com.ibm.bamoe.dmoe.testing;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(/*plugin="pretty",*/ glue={"classpath:com.ibm.bamoe.dmoe.testing"}, features={"src/test/resources/features"})
public class CucumberTest {
}

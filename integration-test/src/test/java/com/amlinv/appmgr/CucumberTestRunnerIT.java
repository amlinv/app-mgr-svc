package com.amlinv.appmgr;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Run the cucumber tests during Failsafe execution.
 *
 * Created by art on 11/12/15.
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"json:target/cucumber-report.json", "html:target/cucumber-html",
                           "pretty"})
public class CucumberTestRunnerIT {
  static {
    System.out.println("RUNNING TESTS");//TBD999
  }
}

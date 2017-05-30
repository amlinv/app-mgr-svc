package com.amlinv.appmgr;

import com.amlinv.appmgr.process.AsyncProcess;
import com.amlinv.appmgr.process.ProcessManager;
import com.amlinv.appmgr.process.impl.StandardAsyncProcessManager;
import com.amlinv.appmgr.process.util.ProcessOutputBufferListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * Created by art on 11/12/16.
 */
public class AppMgrCucumberSteps01 {

  private static final Logger log = LoggerFactory.getLogger(AppMgrCucumberSteps01.class);

  private List<String> arguments = new LinkedList<>();

  private ProcessManager processManager = new StandardAsyncProcessManager();
  private long pid;
  private ProcessOutputBufferListener standardOutputBufferListener;
  private ProcessOutputBufferListener standardErrorBufferListener;

  // TBD999: can a vararg list be used?
  @Given("^command arguments \"([^\"]*)\"+")
  public void command_arguments(String... args) throws Throwable {
    for (String oneArg : args) {
      System.out.println();
    }

    throw new RuntimeException("TBD");
  }

  @Given("^command argument \"([^\"]*)\"$")
  public void command_argument(String arg) throws Throwable {
    this.arguments.add(arg);
  }

  @Then("^start the process$")
  public void start_the_process() throws Throwable {
    this.standardOutputBufferListener = new ProcessOutputBufferListener();
    this.standardErrorBufferListener = new ProcessOutputBufferListener();

    this.pid =
        this.processManager.makeProcessBuilder()
            .arguments(this.arguments)
            .standardOutputListener(this.standardOutputBufferListener)
            .standardErrorListener(this.standardErrorBufferListener)
            .start();
  }

  @Then("^wait for the process to complete$")
  public void wait_for_the_process_to_complete() throws Throwable {
    AsyncProcess process = this.processManager.getProcess(this.pid);
    process.waitUntilStopped();
  }

  @Then("^wait for the stdout to close")
  public void wait_for_the_stdout_to_complete() throws Throwable {
    this.standardOutputBufferListener.waitUntilClosed();
  }

  @Then("^wait for the stderr to close")
  public void wait_for_the_stderr_to_complete() throws Throwable {
    this.standardErrorBufferListener.waitUntilClosed();
  }

  @Then("^validate the process stdout matches \"([^\"]*)\"$")
  public void validate_the_process_stdout_matches(String expectedOutput) throws Throwable {
    String actualOutput = new String(this.standardOutputBufferListener.getBytes());

    if (!actualOutput.matches(expectedOutput)) {
      String stderr = new String(this.standardErrorBufferListener.getBytes());
      log.error("stdout from process did not match; expected=" + expectedOutput + "; actual="
                + actualOutput + "; stderr=" + stderr);
      throw new RuntimeException("output from process did not match: match=" + expectedOutput);
    }
  }

  @Then("^validate the process stderr matches \"([^\"]*)\"$")
  public void validate_the_process_stderr_matches(String expectedStdout) throws Throwable {
    String actualStderr = new String(this.standardErrorBufferListener.getBytes());

    if (!actualStderr.matches(expectedStdout)) {
      String stdout = new String(this.standardOutputBufferListener.getBytes());
      log.error("stderr from process did not match; expected=" + expectedStdout + "; actual="
                + actualStderr + "; stdout=" + stdout);
      throw new RuntimeException("output from process did not match: match=" + expectedStdout);
    }
  }
}

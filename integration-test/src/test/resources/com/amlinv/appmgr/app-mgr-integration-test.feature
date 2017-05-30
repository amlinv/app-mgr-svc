Feature: Application Manager Integration Test

  Background:
  Integration testing of the Application Manager service.

  # TBD999: how to make tests compatible with windows and unix?
  # TBD999: maybe built-in scripts for each env placed in the PATH?

  @unix
  Scenario: Start a process and confirm the output
    Given command argument "bash"
      And command argument "-c"
      And command argument "echo $(( 1 + 1 ))"
    Then start the process
    Then wait for the process to complete
    Then wait for the stdout to close
    Then wait for the stderr to close
    Then validate the process stdout matches "2[\r\n]*"

  @unix
  Scenario: Start a process and confirm the standard error
    Given command argument "bash"
      And command argument "-c"
      And command argument "echo $(( 2 + 2 )) >&2"
    Then start the process
    Then wait for the process to complete
    Then wait for the stdout to close
    Then wait for the stderr to close
    Then validate the process stderr matches "4[\r\n]*"

  @unix
  Scenario: Start a process and confirm both stderr and stdout
    Given command argument "bash"
      And command argument "-c"
      And command argument "echo $(( 3 + 3 )); echo $(( 4 + 4 )) >&2"
    Then start the process
    Then wait for the process to complete
    Then wait for the stdout to close
    Then validate the process stdout matches "6[\r\n]*"
    Then wait for the stderr to close
    Then validate the process stderr matches "8[\r\n]*"

package com.amlinv.appmgr.process;

import java.util.Collection;

/**
 * Created by art on 11/14/16.
 */
public interface AsyncProcessBuilder {
  AsyncProcessBuilder argument(String arg);
  AsyncProcessBuilder arguments(Collection<String> args);
  AsyncProcessBuilder environmentVariable(String envVar);
  AsyncProcessBuilder environmentVariables(Collection<String> envVars);
  AsyncProcessBuilder standardOutputListener(ProcessOutputListener listener);
  AsyncProcessBuilder standardErrorListener(ProcessOutputListener listener);
  long start();
}

package com.amlinv.appmgr.process.impl;

import com.amlinv.appmgr.process.AsyncProcess;
import com.amlinv.appmgr.process.AsyncProcessBuilder;
import com.amlinv.appmgr.process.ProcessOutputListener;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by art on 11/14/16.
 */
public class StandardAsyncProcessBuilder implements AsyncProcessBuilder {

  private final StandardAsyncProcessManager processManager;

  private final List<String> arguments = new LinkedList<>();
  private final List<String> environment = new LinkedList<>();
  private final List<ProcessOutputListener> stdoutListeners = new LinkedList<>();
  private final List<ProcessOutputListener> stderrListeners = new LinkedList<>();

  public StandardAsyncProcessBuilder(
      StandardAsyncProcessManager processManager) {
    this.processManager = processManager;
  }

  @Override
  public AsyncProcessBuilder argument(String arg) {
    this.arguments.add(arg);
    return this;
  }

  @Override
  public AsyncProcessBuilder arguments(Collection<String> args) {
    this.arguments.addAll(args);
    return this;
  }

  @Override
  public AsyncProcessBuilder environmentVariable(String envVar) {
    this.environment.add(envVar);
    return this;
  }

  @Override
  public AsyncProcessBuilder environmentVariables(Collection<String> envVars) {
    this.environment.addAll(envVars);
    return this;
  }

  @Override
  public AsyncProcessBuilder standardOutputListener(ProcessOutputListener listener) {
    this.stdoutListeners.add(listener);
    return this;
  }

  @Override
  public AsyncProcessBuilder standardErrorListener(ProcessOutputListener listener) {
    this.stderrListeners.add(listener);
    return this;
  }

  @Override
  public long start() {
    long pid = this.processManager.createProcess(toStringArray(this.arguments),
                                                 toStringArray(this.environment));

    AsyncProcess process = this.processManager.getProcess(pid);

    this.stdoutListeners.stream().forEach(process::addStandardOutputListener);
    this.stderrListeners.stream().forEach(process::addStandardErrorListener);
    this.processManager.startPid(pid);

    return pid;
  }

  private String[] toStringArray(List<String> list) {
    return list.toArray(new String[list.size()]);
  }
}

package com.amlinv.appmgr.process.impl;

import com.amlinv.appmgr.process.AsyncProcess;
import com.amlinv.appmgr.process.AsyncProcessBuilder;
import com.amlinv.appmgr.process.ProcessManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by art on 11/12/16.
 */
public class StandardAsyncProcessManager implements ProcessManager {

  private final Map<Long, AsyncProcess> processTable = new ConcurrentHashMap<>();

  private final AtomicLong nextProcessId = new AtomicLong(1);

  @Override
  public long startProcess(String[] args, String[] env) {
    long pid = this.nextProcessId.incrementAndGet();

    StandardAsyncProcess result = new StandardAsyncProcess(args, env);
    result.start();

    this.processTable.put(pid, result);

    return pid;
  }

  @Override
  public boolean stopProcess(long pid) {
    AsyncProcess process = this.processTable.remove(pid);

    if (process != null) {
      process.stop();
      return true;
    }

    return false;
  }

  @Override
  public AsyncProcess getProcess(long pid) {
    return this.processTable.get(pid);
  }

  @Override
  public long createProcess(String[] args, String[] env) {
    long pid = this.nextProcessId.incrementAndGet();

    StandardAsyncProcess result = new StandardAsyncProcess(args, env);

    this.processTable.put(pid, result);

    return pid;
  }

  @Override
  public AsyncProcess startPid(long pid) {
    AsyncProcess process = this.processTable.get(pid);
    if (process != null) {
      process.start();
    }
    return process;
  }

  @Override
  public AsyncProcessBuilder makeProcessBuilder() {
    return new StandardAsyncProcessBuilder(this);
  }
}

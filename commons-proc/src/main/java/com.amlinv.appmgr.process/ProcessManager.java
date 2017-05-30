package com.amlinv.appmgr.process;

/**
 * TBD999: reduce complexity (fewer ways to accomplish the same result)
 * Created by art on 11/12/16.
 */
public interface ProcessManager {
  long startProcess(String[] args, String[] env); // TBD999
  boolean stopProcess(long pid);
  AsyncProcess getProcess(long pid);

  long createProcess(String[] args, String[] env);
  AsyncProcess startPid(long pid);

  AsyncProcessBuilder makeProcessBuilder();
}

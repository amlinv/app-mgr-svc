package com.amlinv.appmgr.process;

/**
 * TBD999: extract commmon listener utils (queueing and synchronous/asynchronous delivery of events to listeners)
 *
 * Created by art on 11/5/16.
 */
public interface AsyncProcess {
  void start();
  void stop();
  void waitUntilStopped() throws InterruptedException;

  void addStateListener(AsyncProcessStateListener listener);
  void addStandardOutputListener(ProcessOutputListener listener);
  void addStandardErrorListener(ProcessOutputListener listener);
}

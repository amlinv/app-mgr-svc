package com.amlinv.appmgr.process.impl;

import com.amlinv.appmgr.process.AsyncProcess;
import com.amlinv.appmgr.process.AsyncProcessStateListener;
import com.amlinv.appmgr.process.ProcessOutputListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * TBD999: way to feed stdin
 * TBD999: best way to detect process death; preferrably asynchronously
 *
 * Created by art on 11/5/16.
 */
public class StandardAsyncProcess implements AsyncProcess {

  private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(StandardAsyncProcess.class);

  private final String[] commandArguments;
  private final String[] environment;

  private Logger log;
  private Process process;
  private ProcessOutputWatcher stdoutWatcher;
  private ProcessOutputWatcher stderrWatcher;
  private Thread stdoutWatcherThread;
  private Thread stderrWatcherThread;
  private CountDownLatch startedLatch = new CountDownLatch(1);

  private ConcurrentLinkedQueue<AsyncProcessStateListener>
      stateListeners =
      new ConcurrentLinkedQueue<>();
  private ConcurrentLinkedQueue<ProcessOutputListener>
      stdoutListeners =
      new ConcurrentLinkedQueue<>();
  private ConcurrentLinkedQueue<ProcessOutputListener>
      stderrListeners =
      new ConcurrentLinkedQueue<>();

  public StandardAsyncProcess(String[] commandArguments, String[] environment) {
    this.commandArguments = commandArguments;
    this.environment = environment;
  }

  public StandardAsyncProcess(String[] commandArguments) {
    this.commandArguments = commandArguments;
    this.environment = null;
  }

  public Logger getLog() {
    return log;
  }

  public void setLog(Logger log) {
    this.log = log;
  }

  @Override
  public void start() {
    Thread startThread = new Thread(this::startProcess);
    startThread.start();
  }

  @Override
  public void stop() {
    this.process.destroy();
  }

  @Override
  public void waitUntilStopped() throws InterruptedException {
    this.startedLatch.await();
    this.process.waitFor();
  }

  @Override
  public void addStateListener(AsyncProcessStateListener listener) {
    this.stateListeners.add(listener);
  }

  @Override
  public void addStandardOutputListener(ProcessOutputListener listener) {
    this.stdoutListeners.add(listener);
  }

  @Override
  public void addStandardErrorListener(ProcessOutputListener listener) {
    this.stderrListeners.add(listener);
  }

//========================================
// Internal Methods
//----------------------------------------

  private void startProcess() {
    try {
      this.process = Runtime.getRuntime().exec(this.commandArguments, this.environment);

      // TODO: convert to async IO.  Unfortunately, Process does not support nio, and converting
      //  the input streams to channels does not produce selectable channels.  So, for now we are
      //  stuck using dedicated threads for each input stream.
      this.stdoutWatcher = new ProcessOutputWatcher(this.process.getInputStream());
      this.stdoutWatcher.addListeners(this.stdoutListeners);
      this.stdoutWatcherThread = new Thread(this.stdoutWatcher);

      this.stderrWatcher = new ProcessOutputWatcher(this.process.getErrorStream());
      this.stderrWatcher.addListeners(this.stderrListeners);
      this.stderrWatcherThread = new Thread(this.stderrWatcher);

      this.stdoutWatcherThread.start();
      this.stderrWatcherThread.start();

      this.startedLatch.countDown();
      this.stateListeners.parallelStream().forEach(AsyncProcessStateListener::onProcessStarted);
    } catch (IOException ioExc) {
      this.log.error("failed to run command: cmd={}; num-arg={}", this.commandArguments[0],
                     this.commandArguments.length);

      // TBD999: read up on parallelStream() and ensure it works as-desired / as-needed here (including
      // TBD999: being completely asynchronous)
      this.stateListeners.parallelStream().forEach(
          listener -> listener.onProcessStartupFailure(ioExc));
    }
  }
}

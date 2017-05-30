package com.amlinv.appmgr.process.impl;

import com.amlinv.appmgr.process.ProcessOutputListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by art on 11/5/16.
 */
public class ProcessOutputWatcher implements Runnable {

  private final InputStream processOutputStream;
  private final ConcurrentLinkedQueue<ProcessOutputListener> listeners = new ConcurrentLinkedQueue<>();

  private long streamPosition = 0;

//========================================
// Constructors
//----------------------------------------

  public ProcessOutputWatcher(InputStream processOutputStream) {
    this.processOutputStream = processOutputStream;
  }

//========================================
// Setters and Getters
//----------------------------------------

  public void addListener(ProcessOutputListener newListener) {
    this.listeners.add(newListener);
  }

  public void addListeners(Collection<ProcessOutputListener> newListeners) {
    this.listeners.addAll(newListeners);
  }

  public long getStreamPosition() {
    return streamPosition;
  }

//========================================
// Public API
//----------------------------------------

  @Override
  public void run() {
    byte[] buffer = new byte[16 * 1024];

    try {
      int amountRead = this.processOutputStream.read(buffer);
      while (amountRead > 0) {
        // TBD999: process output

        byte[] data = Arrays.copyOf(buffer, amountRead);
        this.listeners.parallelStream().forEach(
            listener -> listener.onProcessOutputReceived(data, this.streamPosition));

        this.streamPosition += amountRead;

        amountRead = this.processOutputStream.read(buffer);
      }
    } catch (IOException ioExc) {
      // TBD999
    }

    // Notify listeners that the output stream is now closed.
    this.listeners.parallelStream().forEach(ProcessOutputListener::onProcessOutputClosed);
  }
}

package com.amlinv.appmgr.process.util;

import com.amlinv.appmgr.process.ProcessOutputListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by art on 11/14/16.
 */
public class ProcessOutputBufferListener implements ProcessOutputListener {
  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

  private final CountDownLatch completionLatch = new CountDownLatch(1);

  @Override
  public void onProcessOutputReceived(byte[] data, long fileOffset) {
    try {
      this.buffer.write(data);
    } catch (IOException ioExc) {
      throw new RuntimeException("unexpected exception on buffering output", ioExc);
    }
  }

  @Override
  public void onProcessOutputClosed() {
    this.completionLatch.countDown();
  }

  public void waitUntilClosed() throws InterruptedException {
    this.completionLatch.await();
  }

  public byte[] getBytes() {
    return this.buffer.toByteArray();
  }
}

package com.amlinv.appmgr.process;

/**
 * Created by art on 11/5/16.
 */
public interface ProcessOutputListener {

  /**
   * Data was received by the JVM from the external process.
   *
   * @param data buffer of data received; the size of the buffer matches the amount of data received.
   * @param fileOffset the byte offset from the start of the process output at which the data was
   *                   received.
   */
  void onProcessOutputReceived(byte[] data, long fileOffset);
  void onProcessOutputClosed();
}

package com.amlinv.appmgr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by art on 10/22/16.
 */
public class TestRunApplication {
  public static void main(String[] args) {
    try {
      Process process = Runtime.getRuntime().exec(args);

      InputStream stdoutStream = process.getInputStream();
      InputStream stderrStream = process.getErrorStream();

      Thread stdoutThread = new Thread(new ProcessOutputWatcher(stdoutStream, "STDOUT"));
      Thread stderrThread = new Thread(new ProcessOutputWatcher(stderrStream, "STDERR"));

      stdoutThread.run();
      stderrThread.run();

      process.waitFor();

      stdoutThread.join();
      stderrThread.join();
    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }

  public static class ProcessOutputWatcher implements Runnable {
    private final InputStream inputStream;
    private final String label;

    public ProcessOutputWatcher(InputStream inputStream, String label) {
      this.inputStream = inputStream;
      this.label = label;
    }

    @Override
    public void run() {
      try {
        InputStreamReader reader = new InputStreamReader(this.inputStream);
        char[] buffer = new char[16 * 1024];

        int len = 0;
        len = reader.read(buffer);

        while (len > 0) {
          System.out.print(label + ": ");
          System.out.print(new String(buffer, 0, len));
          len = reader.read(buffer);
        }
      } catch (IOException ioExc) {
        ioExc.printStackTrace();
      }
    }
  }
}

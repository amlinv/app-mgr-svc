package com.amlinv.appmgr.process;

/**
 * Created by art on 11/5/16.
 */
public interface AsyncProcessStateListener {
  void onProcessStarted();
  void onProcessStartupFailure(Exception exc);
  void onProcessStopped(int exitCode);
}

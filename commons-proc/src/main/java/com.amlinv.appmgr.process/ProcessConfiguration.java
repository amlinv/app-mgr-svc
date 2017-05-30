package com.amlinv.appmgr.process;

import java.util.List;

/**
 * Created by art on 5/27/17.
 */
public interface ProcessConfiguration {
  List<String> getArguments();
  List<String> getEnvironment();
}

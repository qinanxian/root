
package com.vekai.crops.util.mongo3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionObject {

  public Logger logger = LoggerFactory.getLogger(this.getClass());

  public void fail(String sMessage) throws Exception {
    throw new Exception(sMessage);
  }
}

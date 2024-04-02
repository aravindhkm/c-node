package org.tron.core.consensus.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadIp {

  /**
   * readFile from path.
   */
  public String readFile(String path) {
    String laststr = "";
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
      String tempString = null;
      while ((tempString = reader.readLine()) != null) {
        laststr += tempString;
      }
    } catch (IOException e) {
      logger.debug(e.getMessage(), e);
    }
    return laststr;
  }

}

package org.tron.core.consensus.server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CypherTest {

  /**
   * main of trontest.
   */
  public static void main(String[] args) {
    try {
      Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
      InetAddress ip = null;
      while (allNetInterfaces.hasMoreElements()) {
        NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
        System.out.println(netInterface.getName());
        Enumeration addresses = netInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
          ip = (InetAddress) addresses.nextElement();
          if (ip != null && ip instanceof Inet4Address) {
            System.out.println("IP = " + ip.getHostAddress());
          }
        }
      }
    } catch (Exception e) {
      logger.debug(e.getMessage(), e);
    }


  }
}

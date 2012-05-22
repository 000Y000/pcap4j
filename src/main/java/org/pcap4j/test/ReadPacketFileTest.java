package org.pcap4j.test;

import java.io.EOFException;
import java.sql.Timestamp;
import java.util.concurrent.TimeoutException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

public class ReadPacketFileTest {

  private static final String COUNT_KEY
    = ReadPacketFileTest.class.getName() + ".count";
  private static final int COUNT
    = Integer.getInteger(COUNT_KEY, 5);

  private static final String READ_TIMEOUT_KEY
    = ReadPacketFileTest.class.getName() + ".readTimeOut";
  private static final int READ_TIMEOUT
    = Integer.getInteger(READ_TIMEOUT_KEY, 5); // [ms]

  private static final String MAX_PACKT_SIZE_KEY
    = ReadPacketFileTest.class.getName() + ".maxPacketSize";
  private static final int MAX_PACKT_SIZE
    = Integer.getInteger(MAX_PACKT_SIZE_KEY, 65536); // [bytes]

  private static final String PCAP_FILE_KEY
    = ReadPacketFileTest.class.getName() + ".pcapFile";
  private static final String PCAP_FILE
    = System.getProperty(PCAP_FILE_KEY, "DumpTest.pcap");

  public static void main(String[] args) throws PcapNativeException {
    System.out.println(COUNT_KEY + ": " + COUNT);
    System.out.println(READ_TIMEOUT_KEY + ": " + READ_TIMEOUT);
    System.out.println(MAX_PACKT_SIZE_KEY + ": " + MAX_PACKT_SIZE);

    PcapHandle handle = Pcaps.openOffline(PCAP_FILE);

    for (int i = 0; i < COUNT; i++) {
      try {
        Packet packet = handle.getNextPacketEx();
        Timestamp ts = new Timestamp(handle.getTimestampInts() * 1000L);
        ts.setNanos(handle.getTimestampMicros() * 1000);

        System.out.println(ts);
        System.out.println(packet);
      } catch (TimeoutException e) {
      } catch (EOFException e) {
        System.out.println("EOF");
        break;
      }
    }

    handle.close();
  }

}
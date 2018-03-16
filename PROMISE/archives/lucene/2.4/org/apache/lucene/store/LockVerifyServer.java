import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Simple standalone server that must be running when you
 * use {@link VerifyingLockFactory}.  This server simply
 * verifies at most one process holds the lock at a time.
 * Run without any args to see usage.
 *
 * @see VerifyingLockFactory
 * @see LockStressTest
 */

public class LockVerifyServer {

  private static String getTime(long startTime) {
    return "[" + ((System.currentTimeMillis()-startTime)/1000) + "s] ";
  }

  public static void main(String[] args) throws IOException {

    if (args.length != 1) {
      System.out.println("\nUsage: java org.apache.lucene.store.LockVerifyServer port\n");
      System.exit(1);
    }

    final int port = Integer.parseInt(args[0]);

    ServerSocket s = new ServerSocket(port);
    s.setReuseAddress(true);
    System.out.println("\nReady on port " + port + "...");

    int lockedID = 0;
    long startTime = System.currentTimeMillis();

    while(true) {
      Socket cs = s.accept();
      OutputStream out = cs.getOutputStream();
      InputStream in = cs.getInputStream();

      int id = in.read();
      int command = in.read();

      boolean err = false;

      if (command == 1) {
        if (lockedID != 0) {
          err = true;
          System.out.println(getTime(startTime) + " ERROR: id " + id + " got lock, but " + lockedID + " already holds the lock");
        }
        lockedID = id;
      } else if (command == 0) {
        if (lockedID != id) {
          err = true;
          System.out.println(getTime(startTime) + " ERROR: id " + id + " released the lock, but " + lockedID + " is the one holding the lock");
        }
        lockedID = 0;
      } else
        throw new RuntimeException("unrecognized command " + command);

      System.out.print(".");

      if (err)
        out.write(1);
      else
        out.write(0);

      out.close();
      in.close();
      cs.close();
    }
  }
}

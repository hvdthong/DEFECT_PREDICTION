import java.io.IOException;
import java.io.File;

/**
 * Simple standalone tool that forever acquires & releases a
 * lock using a specific LockFactory.  Run without any args
 * to see usage.
 *
 * @see VerifyingLockFactory
 * @see LockVerifyServer
 */ 

public class LockStressTest {

  public static void main(String[] args) throws Exception {

    if (args.length != 6) {
      System.out.println("\nUsage: java org.apache.lucene.store.LockStressTest myID verifierHostOrIP verifierPort lockFactoryClassName lockDirName sleepTime\n" +
                         "\n" +
                         "  myID = int from 0 .. 255 (should be unique for test process)\n" +
                         "  verifierHostOrIP = host name or IP address where LockVerifyServer is running\n" +
                         "  verifierPort = port that LockVerifyServer is listening on\n" +
                         "  lockFactoryClassName = primary LockFactory class that we will use\n" +
                         "  lockDirName = path to the lock directory (only set for Simple/NativeFSLockFactory\n" +
                         "  sleepTimeMS = milliseconds to pause betweeen each lock obtain/release\n" +
                         "\n" +
                         "You should run multiple instances of this process, each with its own\n" +
                         "unique ID, and each pointing to the same lock directory, to verify\n" +
                         "that locking is working correctly.\n" +
                         "\n" +
                         "Make sure you are first running LockVerifyServer.\n" + 
                         "\n");
      System.exit(1);
    }

    final int myID = Integer.parseInt(args[0]);

    if (myID < 0 || myID > 255) {
      System.out.println("myID must be a unique int 0..255");
      System.exit(1);
    }

    final String verifierHost = args[1];
    final int verifierPort = Integer.parseInt(args[2]);
    final String lockFactoryClassName = args[3];
    final String lockDirName = args[4];
    final int sleepTimeMS = Integer.parseInt(args[5]);

    Class c;
    try {
      c = Class.forName(lockFactoryClassName);
    } catch (ClassNotFoundException e) {
      throw new IOException("unable to find LockClass " + lockFactoryClassName);
    }

    LockFactory lockFactory;
    try {
      lockFactory = (LockFactory) c.newInstance();          
    } catch (IllegalAccessException e) {
      throw new IOException("IllegalAccessException when instantiating LockClass " + lockFactoryClassName);
    } catch (InstantiationException e) {
      throw new IOException("InstantiationException when instantiating LockClass " + lockFactoryClassName);
    } catch (ClassCastException e) {
      throw new IOException("unable to cast LockClass " + lockFactoryClassName + " instance to a LockFactory");
    }

    File lockDir = new File(lockDirName);

    if (lockFactory instanceof NativeFSLockFactory) {
      ((NativeFSLockFactory) lockFactory).setLockDir(lockDir);
    } else if (lockFactory instanceof SimpleFSLockFactory) {
      ((SimpleFSLockFactory) lockFactory).setLockDir(lockDir);
    }

    lockFactory.setLockPrefix("test");
    
    LockFactory verifyLF = new VerifyingLockFactory((byte) myID, lockFactory, verifierHost, verifierPort);

    Lock l = verifyLF.makeLock("test.lock");

    while(true) {

      boolean obtained = false;

      try {
        obtained = l.obtain(10);
      } catch (LockObtainFailedException e) {
        System.out.print("x");
      }

      if (obtained) {
        System.out.print("l");
        l.release();
      }
      Thread.sleep(sleepTimeMS);
    }
  }
}

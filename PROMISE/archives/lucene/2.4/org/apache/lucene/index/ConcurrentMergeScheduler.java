import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/** A {@link MergeScheduler} that runs each merge using a
 *  separate thread, up until a maximum number of threads
 *  ({@link #setMaxThreadCount}) at which when a merge is
 *  needed, the thread(s) that are updating the index will
 *  pause until one or more merges completes.  This is a
 *  simple way to use concurrency in the indexing process
 *  without having to create and manage application level
 *  threads. */

public class ConcurrentMergeScheduler extends MergeScheduler {

  private int mergeThreadPriority = -1;

  protected List mergeThreads = new ArrayList();

  private int maxThreadCount = 3;

  private List exceptions = new ArrayList();
  protected Directory dir;

  private boolean closed;
  protected IndexWriter writer;
  protected int mergeThreadCount;

  public ConcurrentMergeScheduler() {
    if (allInstances != null) {
      addMyself();
    }
  }

  /** Sets the max # simultaneous threads that may be
   *  running.  If a merge is necessary yet we already have
   *  this many threads running, the incoming thread (that
   *  is calling add/updateDocument) will block until
   *  a merge thread has completed. */
  public void setMaxThreadCount(int count) {
    if (count < 1)
      throw new IllegalArgumentException("count should be at least 1");
    maxThreadCount = count;
  }

  /** Get the max # simultaneous threads that may be
   *  running. @see #setMaxThreadCount. */
  public int getMaxThreadCount() {
    return maxThreadCount;
  }

  /** Return the priority that merge threads run at.  By
   *  default the priority is 1 plus the priority of (ie,
   *  slightly higher priority than) the first thread that
   *  calls merge. */
  public synchronized int getMergeThreadPriority() {
    initMergeThreadPriority();
    return mergeThreadPriority;
  }

  /** Return the priority that merge threads run at. */
  public synchronized void setMergeThreadPriority(int pri) {
    if (pri > Thread.MAX_PRIORITY || pri < Thread.MIN_PRIORITY)
      throw new IllegalArgumentException("priority must be in range " + Thread.MIN_PRIORITY + " .. " + Thread.MAX_PRIORITY + " inclusive");
    mergeThreadPriority = pri;

    final int numThreads = mergeThreadCount();
    for(int i=0;i<numThreads;i++) {
      MergeThread merge = (MergeThread) mergeThreads.get(i);
      merge.setThreadPriority(pri);
    }
  }

  private void message(String message) {
    if (writer != null)
      writer.message("CMS: " + message);
  }

  private synchronized void initMergeThreadPriority() {
    if (mergeThreadPriority == -1) {
      mergeThreadPriority = 1+Thread.currentThread().getPriority();
      if (mergeThreadPriority > Thread.MAX_PRIORITY)
        mergeThreadPriority = Thread.MAX_PRIORITY;
    }
  }

  public void close() {
    closed = true;
  }

  public synchronized void sync() {
    while(mergeThreadCount() > 0) {
      message("now wait for threads; currently " + mergeThreads.size() + " still running");
      final int count = mergeThreads.size();
      for(int i=0;i<count;i++)
        message("    " + i + ": " + ((MergeThread) mergeThreads.get(i)));

      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
  }
  private synchronized int mergeThreadCount() {
    int count = 0;
    final int numThreads = mergeThreads.size();
    for(int i=0;i<numThreads;i++)
      if (((MergeThread) mergeThreads.get(i)).isAlive())
        count++;
    return count;
  }

  public void merge(IndexWriter writer)
    throws CorruptIndexException, IOException {


    this.writer = writer;

    initMergeThreadPriority();

    dir = writer.getDirectory();


    message("now merge");
    message("  index: " + writer.segString());

    while(true) {


      MergePolicy.OneMerge merge = writer.getNextMerge();
      if (merge == null) {
        message("  no more merges pending; now return");
        return;
      }

      writer.mergeInit(merge);

      synchronized(this) {
        while (mergeThreadCount() >= maxThreadCount) {
          message("    too many merge threads running; stalling...");
          try {
            wait();
          } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
          }
        }

        message("  consider merge " + merge.segString(dir));
      
        assert mergeThreadCount() < maxThreadCount;

        final MergeThread merger = getMergeThread(writer, merge);
        mergeThreads.add(merger);
        message("    launch new thread [" + merger.getName() + "]");
        merger.start();
      }
    }
  }

  /** Does the actual merge, by calling {@link IndexWriter#merge} */
  protected void doMerge(MergePolicy.OneMerge merge)
    throws IOException {
    writer.merge(merge);
  }

  /** Create and return a new MergeThread */
  protected synchronized MergeThread getMergeThread(IndexWriter writer, MergePolicy.OneMerge merge) throws IOException {
    final MergeThread thread = new MergeThread(writer, merge);
    thread.setThreadPriority(mergeThreadPriority);
    thread.setDaemon(true);
    thread.setName("Lucene Merge Thread #" + mergeThreadCount++);
    return thread;
  }

  protected class MergeThread extends Thread {

    IndexWriter writer;
    MergePolicy.OneMerge startMerge;
    MergePolicy.OneMerge runningMerge;

    public MergeThread(IndexWriter writer, MergePolicy.OneMerge startMerge) throws IOException {
      this.writer = writer;
      this.startMerge = startMerge;
    }

    public synchronized void setRunningMerge(MergePolicy.OneMerge merge) {
      runningMerge = merge;
    }

    public synchronized MergePolicy.OneMerge getRunningMerge() {
      return runningMerge;
    }

    public void setThreadPriority(int pri) {
      try {
        setPriority(pri);
      } catch (NullPointerException npe) {
      } catch (SecurityException se) {
      }
    }

    public void run() {
      
      MergePolicy.OneMerge merge = this.startMerge;
      
      try {

        message("  merge thread: start");

        while(true) {
          setRunningMerge(merge);
          doMerge(merge);

          merge = writer.getNextMerge();
          if (merge != null) {
            writer.mergeInit(merge);
            message("  merge thread: do another merge " + merge.segString(dir));
          } else
            break;
        }

        message("  merge thread: done");

      } catch (Throwable exc) {

        if (!(exc instanceof MergePolicy.MergeAbortedException)) {
          synchronized(ConcurrentMergeScheduler.this) {
            exceptions.add(exc);
          }
          
          if (!suppressExceptions) {
            anyExceptions = true;
            handleMergeException(exc);
          }
        }
      } finally {
        synchronized(ConcurrentMergeScheduler.this) {
          ConcurrentMergeScheduler.this.notifyAll();
          boolean removed = mergeThreads.remove(this);
          assert removed;
        }
      }
    }

    public String toString() {
      MergePolicy.OneMerge merge = getRunningMerge();
      if (merge == null)
        merge = startMerge;
      return "merge thread: " + merge.segString(dir);
    }
  }

  /** Called when an exception is hit in a background merge
   *  thread */
  protected void handleMergeException(Throwable exc) {
    throw new MergePolicy.MergeException(exc, dir);
  }

  static boolean anyExceptions = false;

  /** Used for testing */
  public static boolean anyUnhandledExceptions() {
    synchronized(allInstances) {
      final int count = allInstances.size();
      for(int i=0;i<count;i++)
        ((ConcurrentMergeScheduler) allInstances.get(i)).sync();
      boolean v = anyExceptions;
      anyExceptions = false;
      return v;
    }
  }

  public static void clearUnhandledExceptions() {
    synchronized(allInstances) {
      anyExceptions = false;
    }
  }

  /** Used for testing */
  private void addMyself() {
    synchronized(allInstances) {
      final int size=0;
      int upto = 0;
      for(int i=0;i<size;i++) {
        final ConcurrentMergeScheduler other = (ConcurrentMergeScheduler) allInstances.get(i);
        if (!(other.closed && 0 == other.mergeThreadCount()))
          allInstances.set(upto++, other);
      }
      allInstances.subList(upto, allInstances.size()).clear();
      allInstances.add(this);
    }
  }

  private boolean suppressExceptions;

  /** Used for testing */
  void setSuppressExceptions() {
    suppressExceptions = true;
  }

  /** Used for testing */
  void clearSuppressExceptions() {
    suppressExceptions = false;
  }

  /** Used for testing */
  private static List allInstances;
  public static void setTestMode() {
    allInstances = new ArrayList();
  }
}

package org.apache.xml.utils;

/**
 * A utility class that wraps the ThreadController, which is used
 * by IncrementalSAXSource for the incremental building of DTM.
 */
public class ThreadControllerWrapper
{
  
  /** The ThreadController pool   */
  static ThreadController m_tpool = new ThreadController();

  /**
   * Change the ThreadController that will be used to
   * manage the transform threads.
   *
   * @param tp A ThreadController object
   */
  public static void setThreadController(ThreadController tpool)
  {
    m_tpool = tpool;
  }
  
  public static Thread runThread(Runnable runnable, int priority)
  {
    return m_tpool.run(runnable, priority);
  }
  
  public static void waitThread(Thread worker, Runnable task)
    throws InterruptedException
  {
    m_tpool.waitThread(worker, task);
  }
  
  /**
   * Thread controller utility class for incremental SAX source. Must 
   * be overriden with a derived class to support thread pooling.
   *
   * All thread-related stuff is in this class.
   */
  public static class ThreadController
  {

    /**
     * Will get a thread from the pool, execute the task
     *  and return the thread to the pool.
     *
     *  The return value is used only to wait for completion
     *
     *
     * NEEDSDOC @param task
     * @param priority if >0 the task will run with the given priority
     *  ( doesn't seem to be used in xalan, since it's allways the default )
     * @returns The thread that is running the task, can be used
     *          to wait for completion
     *
     * NEEDSDOC ($objectName$) @return
     */
    public Thread run(Runnable task, int priority)
    {

      Thread t = new Thread(task);

      t.start();

      return t;
    }

    /**
     *  Wait until the task is completed on the worker
     *  thread.
     *
     * NEEDSDOC @param worker
     * NEEDSDOC @param task
     *
     * @throws InterruptedException
     */
    public void waitThread(Thread worker, Runnable task)
            throws InterruptedException
    {

      worker.join();
    }
  }
 
}

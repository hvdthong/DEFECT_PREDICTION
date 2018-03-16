public class TimeLimitedCollector extends HitCollector {
  
  /** 
   * Default timer resolution.
   * @see #setResolution(long) 
   */
  public static final int DEFAULT_RESOLUTION = 20;

  /**
   * Default for {@link #isGreedy()}.
   * @see #isGreedy()
   */
  public boolean DEFAULT_GREEDY = false; 

  private static long resolution = DEFAULT_RESOLUTION;
  
  private boolean greedy = DEFAULT_GREEDY ;

  private static class TimerThread extends Thread  {

    private volatile long time = 0;

    /**
     * TimerThread provides a pseudo-clock service to all searching
     * threads, so that they can count elapsed time with less overhead
     * than repeatedly calling System.currentTimeMillis.  A single
     * thread should be created to be used for all searches.
     */
    private TimerThread() {
      super("TimeLimitedCollector timer thread");
      this.setDaemon( true );
    }

    public void run() {
      boolean interrupted = false;
      try {
        while( true ) {
          time += resolution;
          try {
            Thread.sleep( resolution );
          } catch( final InterruptedException e ) {
            interrupted = true;
          }
        }
      }
      finally {
        if( interrupted ) {
          Thread.currentThread().interrupt();
        }
      }
    }

    /**
     * Get the timer value in milliseconds.
     */
    public long getMilliseconds() {
      return time;
    }
  }

  /**
   * Thrown when elapsed search time exceeds allowed search time. 
   */
  public static class TimeExceededException extends RuntimeException {
    private long timeAllowed;
    private long timeElapsed;
    private int lastDocCollected;
    private TimeExceededException(long timeAllowed, long timeElapsed, int lastDocCollected) {
      super("Elapsed time: " + timeElapsed + "Exceeded allowed search time: " + timeAllowed + " ms.");
      this.timeAllowed = timeAllowed;
      this.timeElapsed = timeElapsed;
      this.lastDocCollected = lastDocCollected;
    }
    /**
     * Returns allowed time (milliseconds).
     */
    public long getTimeAllowed() {
      return timeAllowed;
    }
    /**
     * Returns elapsed time (milliseconds).
     */
    public long getTimeElapsed() {
      return timeElapsed;
    }
    /**
     * Returns last doc that was collected when the search time exceeded.  
     */
    public int getLastDocCollected() {
      return lastDocCollected;
    }
  }

  private final static TimerThread TIMER_THREAD = new TimerThread();
  
  static  {
    TIMER_THREAD.start();
  }

  private final long t0;
  private final long timeout;
  private final HitCollector hc;

  /**
   * Create a TimeLimitedCollector wrapper over another HitCollector with a specified timeout.
   * @param hc the wrapped HitCollector
   * @param timeAllowed max time allowed for collecting hits after which {@link TimeExceededException} is thrown
   */
  public TimeLimitedCollector( final HitCollector hc, final long timeAllowed ) {
    this.hc = hc;
    t0 = TIMER_THREAD.getMilliseconds();
    this.timeout = t0 + timeAllowed;
  }

  /**
   * Calls collect() on the decorated HitCollector.
   * 
   * @throws TimeExceededException if the time allowed has been exceeded.
   */
  public void collect( final int doc, final float score ) {
    long time = TIMER_THREAD.getMilliseconds();
    if( timeout < time) {
      if (greedy) {
        hc.collect( doc, score );
      }
      throw new TimeExceededException( timeout-t0, time-t0, doc );
    }
    hc.collect( doc, score );
  }

  /** 
   * Return the timer resolution.
   * @see #setResolution(long)
   */
  public static long getResolution() {
    return resolution;
  }

  /**
   * Set the timer resolution.
   * The default timer resolution is 20 milliseconds. 
   * This means that a search required to take no longer than 
   * 800 milliseconds may be stopped after 780 to 820 milliseconds.
   * <br>Note that: 
   * <ul>
   * <li>Finer (smaller) resolution is more accurate but less efficient.</li>
   * <li>Setting resolution to less than 5 milliseconds will be silently modified to 5 milliseconds.</li>
   * <li>Setting resolution smaller than current resolution might take effect only after current 
   * resolution. (Assume current resolution of 20 milliseconds is modified to 5 milliseconds, 
   * then it can take up to 20 milliseconds for the change to have effect.</li>
   * </ul>      
   */
  public static void setResolution(long newResolution) {
  }

  /**
   * Checks if this time limited collector is greedy in collecting the last hit.
   * A non greedy collector, upon a timeout, would throw a {@link TimeExceededException} 
   * without allowing the wrapped collector to collect current doc. A greedy one would 
   * first allow the wrapped hit collector to collect current doc and only then 
   * throw a {@link TimeExceededException}.
   * @see #setGreedy(boolean)
   */
  public boolean isGreedy() {
    return greedy;
  }

  /**
   * Sets whether this time limited collector is greedy.
   * @param greedy true to make this time limited greedy
   * @see #isGreedy()
   */
  public void setGreedy(boolean greedy) {
    this.greedy = greedy;
  }
}

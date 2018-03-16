package org.apache.xalan.trace;

/**
 * <meta name="usage" content="advanced"/>
 * Extends TraceListenerEx but adds a EndTrace event.
 */
public interface TraceListenerEx2 extends TraceListenerEx
{
  /**
   * Method that is called when the end of a trace event occurs.
   * The method is blocking.  It must return before processing continues.
   *
   * @param ev the trace event.
   */
  public void traceEnd(TracerEvent ev);
}

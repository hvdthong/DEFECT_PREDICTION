package org.apache.xalan.trace;

/**
 * Extends TraceListenerEx but adds a EndTrace event.
 * @xsl.usage advanced
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

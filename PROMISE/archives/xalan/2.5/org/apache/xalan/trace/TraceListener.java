package org.apache.xalan.trace;


/**
 * <meta name="usage" content="advanced"/>
 * Interface the XSL processor calls when it matches a source node, selects a set of source nodes,
 * or generates a result node.
 * If you want an object instance to be called when a trace event occurs, use the TransformerImpl setTraceListener method.
 * @see org.apache.xalan.trace.TracerEvent
 * @see org.apache.xalan.trace.TraceManager#addTraceListener
 */
public interface TraceListener extends java.util.EventListener
{

  /**
   * Method that is called when a trace event occurs.
   * The method is blocking.  It must return before processing continues.
   *
   * @param ev the trace event.
   */
  public void trace(TracerEvent ev);

  /**
   * Method that is called just after the formatter listener is called.
   *
   * @param ev the generate event.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void selected(SelectionEvent ev) throws javax.xml.transform.TransformerException;

  /**
   * Method that is called just after the formatter listener is called.
   *
   * @param ev the generate event.
   */
  public void generated(GenerateEvent ev);
}

 
package org.apache.xalan.trace;

/**
 * Extends TraceListenerEx2 but adds extensions trace events.
 * @xsl.usage advanced
 */
public interface TraceListenerEx3 extends TraceListenerEx2 {

	/**
	 * Method that is called when an extension event occurs.
	 * The method is blocking.  It must return before processing continues.
	 *
	 * @param ev the extension event.
	 */
	public void extension(ExtensionEvent ee);

	/**
	 * Method that is called when the end of an extension event occurs.
	 * The method is blocking.  It must return before processing continues.
	 *
	 * @param ev the extension event.
	 */
	public void extensionEnd(ExtensionEvent ee);

}


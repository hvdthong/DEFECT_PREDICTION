package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.BoundedFIFO;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.LogLog;
import java.util.Enumeration;

/**
   The AsyncAppender lets users log events asynchronously. It uses a
   bounded buffer to store logging events.

   <p>The AsyncAppender will collect the events sent to it and then
   dispatch them to all the appenders that are attached to it. You can
   attach multiple appenders to an AsyncAppender.

   <p>The AsyncAppender uses a separate thread to serve the events in
   its bounded buffer.

   <p>Refer to the results in {@link org.apache.log4j.performance.Logging}
   for the impact of using this appender.

   <p><b>Important note:</b> The <code>AsyncAppender</code> can only
   be script configured using the {@link
   org.apache.log4j.xml.DOMConfigurator}. Refer to example  configuration
   files <a href="xml/examples/doc-files/sample4.xml">sample4.xml</a>
   and <a href="xml/examples/doc-files/sample5.xml">sample5.xml</a>.



   @author Ceki G&uuml;lc&uuml;
   @since 0.9.1 */
public class AsyncAppender extends AppenderSkeleton
                                            implements AppenderAttachable {

  /** The default buffer size is set to 128 events. */
  public static final int DEFAULT_BUFFER_SIZE = 128;


  BoundedFIFO bf = new BoundedFIFO(DEFAULT_BUFFER_SIZE);

  AppenderAttachableImpl aai;
  Dispatcher dispatcher;
  boolean locationInfo = false;

  boolean interruptedWarningMessage = false;

  public
  AsyncAppender() {
    aai = new AppenderAttachableImpl();
    dispatcher = new Dispatcher(bf, this);
    dispatcher.start();
  }


  public
  void addAppender(Appender newAppender) {
    synchronized(aai) {
      aai.addAppender(newAppender);
    }
  }

  public
  void append(LoggingEvent event) {
    event.getNDC();
    event.getThreadName();
    event.getMDCCopy();
    if(locationInfo) {
      event.getLocationInformation();
    }
    synchronized(bf) {
      while(bf.isFull()) {
	try {
	  bf.wait();
	} catch(InterruptedException e) {
	  if(!interruptedWarningMessage) {
	    interruptedWarningMessage = true;
	    LogLog.warn("AsyncAppender interrupted.", e);
	  } else {
	    LogLog.warn("AsyncAppender interrupted again.");
	  }
	}
      }

      bf.put(event);
      if(bf.wasEmpty()) {
	bf.notify();
      }
    }
  }

  /**
     Close this <code>AsyncAppender</code> by interrupting the
     dispatcher thread which will process all pending events before
     exiting.
  */
  public
  void close() {
    synchronized(this) {
      if(closed) { 
	return;
      }
      closed = true;
    }

    dispatcher.close();
    try {
      dispatcher.join();
    } catch(InterruptedException e) {
      LogLog.error("Got an InterruptedException while waiting for the "+
		   "dispatcher to finish.", e);
    }
    dispatcher = null;
    bf = null;
  }

  public
  Enumeration getAllAppenders() {
    synchronized(aai) {
      return aai.getAllAppenders();
    }
  }

  public
  Appender getAppender(String name) {
    synchronized(aai) {
      return aai.getAppender(name);
    }
  }

  /**
     Returns the current value of the <b>LocationInfo</b> option.
  */
  public
  boolean getLocationInfo() {
    return locationInfo;
  }

  /**
     Is the appender passed as parameter attached to this category?
   */
  public
  boolean isAttached(Appender appender) {
    return aai.isAttached(appender);
  }


  /**
     The <code>AsyncAppender</code> does not require a layout. Hence,
     this method always returns <code>false</code>. */
  public
  boolean requiresLayout() {
    return false;
  }

  public
  void removeAllAppenders() {
    synchronized(aai) {
      aai.removeAllAppenders();
    }
  }


  public
  void removeAppender(Appender appender) {
    synchronized(aai) {
      aai.removeAppender(appender);
    }
  }

  public
  void removeAppender(String name) {
    synchronized(aai) {
      aai.removeAppender(name);
    }
  }

  /**
     The <b>LocationInfo</b> option takes a boolean value. By
     default, it is set to false which means there will be no effort
     to extract the location information related to the event. As a
     result, the event that will be ultimately logged will likely to
     contain the wrong location information (if present in the log
     format).

     <p>Location information extraction is comparatively very slow and
     should be avoided unless performance is not a concern.
   */
  public
  void setLocationInfo(boolean flag) {
    locationInfo = flag;
  }


  /**
     The <b>BufferSize</b> option takes a non-negative integer
     value.  This integer value determines the maximum size of the
     bounded buffer. Increasing the size of the buffer is always
     safe. However, if an existing buffer holds unwritten elements,
     then <em>decreasing the buffer size will result in event
     loss.</em> Nevertheless, while script configuring the
     AsyncAppender, it is safe to set a buffer size smaller than the
     {@link #DEFAULT_BUFFER_SIZE default buffer size} because
     configurators guarantee that an appender cannot be used before
     being completely configured.
   */
  public
  void setBufferSize(int size) {
    bf.resize(size);
  }

  /**
     Returns the current value of the <b>BufferSize</b> option.
   */
  public
  int getBufferSize() {
    return bf.getMaxSize();
  }

}
class Dispatcher extends Thread {

  BoundedFIFO bf;
  AppenderAttachableImpl aai;
  boolean interrupted = false;
  AsyncAppender container;

  Dispatcher(BoundedFIFO bf, AsyncAppender container) {
    this.bf = bf;
    this.container = container;
    this.aai = container.aai;
    this.setPriority(Thread.MIN_PRIORITY);
    this.setName("Dispatcher-"+getName());

  }

  void close() {
    synchronized(bf) {
      interrupted = true;
      if(bf.length() == 0) {
	bf.notify();
      }
    }
  }



  /**
     The dispatching strategy is to wait until there are events in the
     buffer to process. After having processed an event, we release
     the monitor (variable bf) so that new events can be placed in the
     buffer, instead of keeping the monitor and processing the remaining
     events in the buffer.

    <p>Other approaches might yield better results.

  */
  public
  void run() {


    LoggingEvent event;

    while(true) {
      synchronized(bf) {
	if(bf.length() == 0) {
	  if(interrupted) {
	    return;
	  }
	  try {
	    bf.wait();
	  } catch(InterruptedException e) {
	    LogLog.error("The dispathcer should not be interrupted.");
	    break;
	  }
	}
	event = bf.get();
	if(bf.wasFull()) {
	  bf.notify();
	}

      synchronized(container.aai) {
	if(aai != null && event != null) {
	  aai.appendLoopOnAppenders(event);
	}
      }
  }
}

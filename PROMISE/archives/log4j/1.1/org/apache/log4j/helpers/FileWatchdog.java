package org.apache.log4j.helpers;

import java.io.File;
import org.apache.log4j.helpers.LogLog;

/**
   Check every now and then that a certain file has not changed. If it
   has, then call the {@link #doOnChange} method.


   @author Ceki G&uuml;lc&uuml;
   @since version 0.9.1 */
public abstract class FileWatchdog extends Thread {

  /**
     The default delay between every file modification check, set to 60
     seconds.  */
  static final public long DEFAULT_DELAY = 60000; 
  /**
     The name of the file to observe  for changes.
   */
  protected String filename;
  
  /**
     The delay to observe between every check. By default set {@link
     #DEFAULT_DELAY}. */
  protected long delay = DEFAULT_DELAY; 
  
  File file;
  long lastModif = 0; 
  boolean warnedAlready = false;
  boolean interrupted = false;

  protected
  FileWatchdog(String filename) {
    this.filename = filename;
    file = new File(filename);
    setDaemon(true);
    checkAndConfigure();
  }

  /**
     Set the delay to observe between each check of the file changes.
   */
  public
  void setDelay(long delay) {
    this.delay = delay;
  }

  abstract 
  protected 
  void doOnChange();

  protected
  void checkAndConfigure() {
    boolean fileExists;
    try {
      fileExists = file.exists();
    } catch(SecurityException  e) {
      LogLog.warn("Was not allowed to read check file existance, file:["+
		  filename+"].");
      return;
    }

    if(fileExists) {
	doOnChange();
	warnedAlready = false;
      }
    } else {
      if(!warnedAlready) {
	LogLog.debug("["+filename+"] does not exist.");
	warnedAlready = true;
      }
    }
  }

  public
  void run() {    
    while(!interrupted) {
      try {
	Thread.currentThread().sleep(delay);
      } catch(InterruptedException e) {
      }
      checkAndConfigure();
    }
  }
}

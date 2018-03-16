package org.apache.log4j.spi;


/**
   A string based interface to configure package components.

   @author Ceki G&uuml;lc&uuml;
   @author Anders Kristensen
   @since 0.8.1
 */
public interface OptionHandler {

  /**
     Activate the options that were previously set with calls to option
     setters.

     <p>This allows to defer activiation of the options until all
     options have been set. This is required for components which have
     related options that remain ambigous until all are set.

     <p>For example, the FileAppender has the {@link
     org.apache.log4j.FileAppender#setFile File} and {@link
     org.apache.log4j.FileAppender#setAppend Append} options both of
     which are ambigous until the other is also set.  */
  void activateOptions();

  /**
     Return list of strings that the OptionHandler instance recognizes.

     @deprecated We now use JavaBeans style getters/setters.
   */

  /**
     Set <code>option</code> to <code>value</code>.

     <p>The handling of each option depends on the OptionHandler
     instance. Some options may become active immediately whereas
     other may be activated only when {@link #activateOptions} is
     called.

     @deprecated We now use JavaBeans style getters/setters.
  */
}

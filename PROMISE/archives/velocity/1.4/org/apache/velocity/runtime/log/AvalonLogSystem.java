import java.io.File;

import org.apache.log.Priority;
import org.apache.log.Logger;
import org.apache.log.Hierarchy;
import org.apache.log.LogTarget;
import org.apache.log.output.io.FileTarget;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * Implementation of a Avalon logger.
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: AvalonLogSystem.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class AvalonLogSystem implements LogSystem
{
    private Logger logger = null;

    private RuntimeServices rsvc = null;

    /**
     *  default CTOR.  Initializes itself using the property RUNTIME_LOG
     *  from the Velocity properties
     */

    public AvalonLogSystem()
    {
    }

    public void init( RuntimeServices rs )
        throws Exception
    {
        this.rsvc = rs;

        /*
         *  if a logger is specified, we will use this instead of
         *  the default
         */
        String loggerName = (String) rsvc.getProperty("runtime.log.logsystem.avalon.logger");
        
        if (loggerName != null)
        {
            this.logger = Hierarchy.getDefaultHierarchy().getLoggerFor(loggerName);
        } 
        else 
        {
            /*
             *  since this is a Velocity-provided logger, we will
             *  use the Runtime configuration
             */
            String logfile = (String) rsvc.getProperty( RuntimeConstants.RUNTIME_LOG );

            /*
             *  now init.  If we can't, panic!
             */
            try
            {
                init( logfile );

                logVelocityMessage( 0,
                    "AvalonLogSystem initialized using logfile '" + logfile + "'" );
            }
            catch( Exception e )
            {
                System.out.println(
                    "PANIC : Error configuring AvalonLogSystem : " + e );
                System.err.println(
                    "PANIC : Error configuring AvalonLogSystem : " + e );

                throw new Exception("Unable to configure AvalonLogSystem : " + e );
            }
        }
    }

    /**
     *  initializes the log system using the logfile argument
     *
     *  @param logFile   file for log messages
     */
    public void init(String logFile)
        throws Exception
    {

	/*
	 *  make our FileTarget.  Note we are going to keep the 
	 *  default behavior of not appending...
	 */
        FileTarget target = new FileTarget( new File( logFile), 
					    false, 
					    new VelocityFormatter("%{time} %{message}\\n%{throwable}" ) );
       
        /*
         *  use the toString() of RuntimeServices to make a unique logger
         */

        logger = Hierarchy.getDefaultHierarchy().getLoggerFor( rsvc.toString() );
        logger.setPriority( Priority.DEBUG );
        logger.setLogTargets( new LogTarget[] { target } );
    }
    
    /**
     *  logs messages
     *
     *  @param level severity level
     *  @param message complete error message
     */
    public void logVelocityMessage(int level, String message)
    {
        /*
         *  based on level, call teh right logger method
         *  and prefix with the appropos prefix
         */

        switch (level) 
        {
            case LogSystem.WARN_ID:
                logger.warn( RuntimeConstants.WARN_PREFIX + message );
                break;
            case LogSystem.INFO_ID:
                logger.info( RuntimeConstants.INFO_PREFIX + message);
                break;
            case LogSystem.DEBUG_ID:
                logger.debug( RuntimeConstants.DEBUG_PREFIX + message);
                break;
            case LogSystem.ERROR_ID:
                logger.error(RuntimeConstants.ERROR_PREFIX + message);
                break;
            default:
                logger.info( message);
                break;
        }
    }
}

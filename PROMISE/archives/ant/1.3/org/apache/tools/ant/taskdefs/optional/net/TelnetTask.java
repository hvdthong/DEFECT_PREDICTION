package org.apache.tools.ant.taskdefs.optional.net;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import com.oroinc.net.telnet.*;
import org.apache.tools.ant.BuildException;
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Class to provide automated telnet protocol support for the Ant build tool
 *
 * @author Scott Carlson<a href="mailto:ScottCarlson@email.com">ScottCarlson@email.com</a>
 * @version $Revision: 268179 $
 */

public class TelnetTask extends Task {
    /**
     *  The userid to login with, if automated login is used
     */
    private String userid  = null;

    /**
     *  The password to login with, if automated login is used
     */
    private String password= null;

    /**
     *  The server to connect to. 
     */
    private String server  = null;

    /**
     *  The tcp port to connect to. 
     */
    private int port = 23;

    /**
     *  The Object which handles the telnet session.
     */
    private AntTelnetClient telnet = null;

    /**
     *  The list of read/write commands for this session
     */
    private Vector telnetTasks = new Vector();

    /** 
     *  If true, adds a CR to beginning of login script
     */
    private boolean addCarriageReturn = false;

    /**
     *  Default time allowed for waiting for a valid response
     *  for all child reads.  A value of 0 means no limit.
     */
    private Integer defaultTimeout = null;

    /** 
     *  Verify that all parameters are included. 
     *  Connect and possibly login
     *  Iterate through the list of Reads and writes 
     */
    public void execute() throws BuildException 
    {
       /**  A server name is required to continue */
       if (server== null)
           throw new BuildException("No Server Specified");
       /**  A userid and password must appear together 
        *   if they appear.  They are not required.
        */
       if (userid == null && password != null)
           throw new BuildException("No Userid Specified");
       if (password == null && userid != null)
           throw new BuildException("No Password Specified");

       /**  Create the telnet client object */
       telnet = new AntTelnetClient();
       try {
           telnet.connect(server, port);
       } catch(IOException e) {
           throw new BuildException("Can't connect to "+server);
       }
       /**  Login if userid and password were specified */
       if (userid != null && password != null)
          login();
       /**  Process each sub command */
       Enumeration tasksToRun = telnetTasks.elements();
       while (tasksToRun!=null && tasksToRun.hasMoreElements())
       {
           TelnetSubTask task = (TelnetSubTask) tasksToRun.nextElement();
           if (task instanceof TelnetRead && defaultTimeout != null)
               ((TelnetRead)task).setDefaultTimeout(defaultTimeout);
           task.execute(telnet);
       }
    }

    /**  
     *  Process a 'typical' login.  If it differs, use the read 
     *  and write tasks explicitely
     */
    private void login()
    {
       if (addCarriageReturn)
          telnet.sendString("\n", true);
       telnet.waitForString("ogin:");
       telnet.sendString(userid, true);
       telnet.waitForString("assword:");
       telnet.sendString(password, false);
    }

    /**
     *  Set the userid attribute 
     */
    public void setUserid(String u) { this.userid = u; }

    /**
     *  Set the password attribute 
     */
    public void setPassword(String p) { this.password = p; }

    /**
     *  Set the server address attribute 
     */
    public void setServer(String m) { this.server = m; }

    /**
     *  Set the tcp port to connect to attribute 
     */
    public void setPort(int p) { this.port = p; }

    /**
     *  Set the tcp port to connect to attribute 
     */
    public void setInitialCR(boolean b)
    {
       this.addCarriageReturn = b;
    }

    /**
     *  Change the default timeout to wait for 
     *  valid responses
     */
    public void setTimeout(Integer i)
    {
       this.defaultTimeout = i;
    }

    /**
     *  A subTask <read> tag was found.  Create the object, 
     *  Save it in our list, and return it.
     */
   
    public TelnetSubTask createRead()
    {
        TelnetSubTask task = (TelnetSubTask)new TelnetRead();
        telnetTasks.addElement(task);
        return task;
    }

    /**
     *  A subTask <write> tag was found.  Create the object, 
     *  Save it in our list, and return it.
     */
    public TelnetSubTask createWrite()
    {
        TelnetSubTask task = (TelnetSubTask)new TelnetWrite();
        telnetTasks.addElement(task);
        return task;
    }

    /**  
     *  This class is the parent of the Read and Write tasks.
     *  It handles the common attributes for both.
     */
    public class TelnetSubTask
    {
        protected String taskString= "";
        public void execute(AntTelnetClient telnet) 
                throws BuildException
        {
            throw new BuildException("Shouldn't be able instantiate a SubTask directly");
        }
        public void addText(String s) { setString(s);}
        public void setString(String s)
        {
           taskString += s; 
        }
    }
    /**
     *  This class sends text to the connected server 
     */
    public class TelnetWrite extends TelnetSubTask
    {
        private boolean echoString = true;
        public void execute(AntTelnetClient telnet) 
               throws BuildException
        {
           telnet.sendString(taskString, echoString);
        }
        
        public void setEcho(boolean b)
        {
           echoString = b;
        }
    }
    /**
     *  This class reads the output from the connected server
     *  until the required string is found. 
     */
    public class TelnetRead extends TelnetSubTask
    {
        private Integer timeout = null;
        public void execute(AntTelnetClient telnet) 
               throws BuildException
        {
            telnet.waitForString(taskString, timeout);
        }
        /**
         *  Override any default timeouts
         */
        public void setTimeout(Integer i)
        {
           this.timeout = i;
        }
        /**
         *  Sets the default timeout if none has been set already
         */
        public void setDefaultTimeout(Integer defaultTimeout)
        {
           if (timeout == null)
              timeout = defaultTimeout;
    }
    }
    /**
     *  This class handles the abstraction of the telnet protocol.
     *  Currently it is a wrapper around <a href="www.oroinc.com">ORO</a>'s 
     *  NetComponents
     */
    public class AntTelnetClient extends TelnetClient
    {
      /**
       * Read from the telnet session until the string we are 
       * waiting for is found 
       * @parm s The string to wait on 
       */
      public void waitForString(String s)
      {
           waitForString(s, null);
      }

      /**
       * Read from the telnet session until the string we are 
       * waiting for is found or the timeout has been reached
       * @parm s The string to wait on 
       * @parm timeout The maximum number of seconds to wait
       */
      public void waitForString(String s, Integer timeout)
      {
        InputStream is =this.getInputStream();
        try {
          StringBuffer sb = new StringBuffer();
          if (timeout == null || timeout.intValue() == 0)
          {
              while (sb.toString().indexOf(s) == -1)
                  {
                      sb.append((char) is.read());
                  }
          }
          else
          {
              Calendar endTime = Calendar.getInstance(); 
              endTime.add(Calendar.SECOND,timeout.intValue());
              while ( sb.toString().indexOf(s) == -1)
              {
                  while (Calendar.getInstance().before(endTime) &&
                         is.available() == 0) {
                      Thread.sleep(250);
                  }
                  if (is.available() == 0)
                      throw new BuildException("Response Timed-Out", getLocation());
                  sb.append((char) is.read());
              }
          }
          log(sb.toString(), Project.MSG_INFO);
        } catch (BuildException be)
        { 
            throw be;
        } catch (Exception e)
        { 
            throw new BuildException(e, getLocation());
        }
      }

    
      /**
       * Write this string to the telnet session.
       * @parm echoString  Logs string sent
       */
      public void sendString(String s, boolean echoString)
      {
        OutputStream os =this.getOutputStream();
        try {
          os.write((s + "\n").getBytes());
          if (echoString)
              log(s, Project.MSG_INFO);
          os.flush();
        } catch (Exception e)
        { 
          throw new BuildException(e, getLocation());
        }
      }
    }
}

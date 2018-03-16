package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.TimeoutObserver;
import org.apache.tools.ant.util.Watchdog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.PrintStream;

/**
 *
 * @author thomas.haas@softwired-inc.com
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @since Ant 1.2
 */
public class ExecuteJava implements Runnable, TimeoutObserver {

    private Commandline javaCommand = null;
    private Path classpath = null;
    private CommandlineJava.SysProperties sysProperties = null;
    private Method main = null;
    private Long timeout = null;
    private Throwable caught = null;
    private boolean timedOut = false;
    private Thread thread = null;

    public void setJavaCommand(Commandline javaCommand) {
        this.javaCommand = javaCommand;
    }

    /**
     * Set the classpath to be used when running the Java class
     * 
     * @param p an Ant Path object containing the classpath.
     */
    public void setClasspath(Path p) {
        classpath = p;
    }

    public void setSystemProperties(CommandlineJava.SysProperties s) {
        sysProperties = s;
    }

    /**
     * All output (System.out as well as System.err) will be written
     * to this Stream.
     *
     * @deprecated manage output at the task level
     */
    public void setOutput(PrintStream out) {
    }

    /**
     * @since 1.19, Ant 1.5
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void execute(Project project) throws BuildException{
        final String classname = javaCommand.getExecutable();

        AntClassLoader loader = null; 
        try {
            if (sysProperties != null) {
                sysProperties.setSystem();
            }

            final Class[] param = { Class.forName("[Ljava.lang.String;") };
            Class target = null;
            if (classpath == null) {
                target = Class.forName(classname);
            } else {
                loader = new AntClassLoader(project.getCoreLoader(), project, 
                                            classpath, false);
                loader.setIsolated(true);
                loader.setThreadContextLoader();
                target = loader.forceLoadClass(classname);
                AntClassLoader.initializeClass(target);
            }
            main = target.getMethod("main", param);
            if (main == null) {
                throw new BuildException("Could not find main() method in " 
                                         + classname);
            }

            if (timeout == null) {
                run();
            } else {
                thread = new Thread(this, "ExecuteJava");
                Task currentThreadTask 
                    = project.getThreadTask(Thread.currentThread());
                project.registerThreadTask(thread, currentThreadTask);
                thread.setDaemon(true);
                Watchdog w = new Watchdog(timeout.longValue());
                w.addTimeoutObserver(this);
                synchronized (this) {
                    thread.start();
                    w.start();
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                    if (timedOut) {
                        project.log("Timeout: sub-process interrupted",
                                    Project.MSG_WARN); 
                    } else {
                        thread = null;
                        w.stop();
                    }
                }
            }

            if (caught != null) {
                throw caught;
            }

        } catch (ClassNotFoundException e) {
            throw new BuildException("Could not find " + classname + "."
                                     + " Make sure you have it in your"
                                     + " classpath");
        } catch (SecurityException e) {
            throw e;
        } catch (Throwable e) {
            throw new BuildException(e);
        } finally {
            if (loader != null) {
                loader.resetThreadContextLoader();
                loader.cleanup();
            }
            if (sysProperties != null) {
                sysProperties.restoreSystem();
            }
        }
    }

    /**
     * @since 1.19, Ant 1.5
     */
    public void run() {
        final Object[] argument = { javaCommand.getArguments() };
        try {
            main.invoke(null, argument);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (!(t instanceof InterruptedException)) {
                caught = t;
            } /* else { swallow, probably due to timeout } */
        } catch (Throwable t) {
            caught = t;
        } finally {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * @since 1.19, Ant 1.5
     */
    public synchronized void timeoutOccured(Watchdog w) {
        if (thread != null) {
            timedOut = true;
            thread.interrupt();
        }
        notifyAll();
    }

    /**
     * @since 1.19, Ant 1.5
     */
    public boolean killedProcess() {
        return timedOut;
    }
}

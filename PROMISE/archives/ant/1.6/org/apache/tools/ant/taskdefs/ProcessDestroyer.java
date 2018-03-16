package org.apache.tools.ant.taskdefs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Destroys all registered <code>Process</code>es when the VM exits.
 *
 * @since Ant 1.5
 */
class ProcessDestroyer implements Runnable {

    private Vector processes = new Vector();
    private Method addShutdownHookMethod;
    private Method removeShutdownHookMethod;
    private ProcessDestroyerImpl destroyProcessThread = null;

    private boolean added = false;
    private boolean running = false;

    private class ProcessDestroyerImpl extends Thread {
        private boolean shouldDestroy = true;

        public ProcessDestroyerImpl() {
            super("ProcessDestroyer Shutdown Hook");
        }
        public void run() {
            if (shouldDestroy) {
                ProcessDestroyer.this.run();
            }
        }

        public void setShouldDestroy(boolean shouldDestroy) {
            this.shouldDestroy = shouldDestroy;
        }
    }

    /**
     * Constructs a <code>ProcessDestroyer</code> and obtains
     * <code>Runtime.addShutdownHook()</code> and
     * <code>Runtime.removeShutdownHook()</code> through reflection. The
     * ProcessDestroyer manages a list of processes to be destroyed when the
     * VM exits. If a process is added when the list is empty,
     * this <code>ProcessDestroyer</code> is registered as a shutdown hook. If
     * removing a process results in an empty list, the
     * <code>ProcessDestroyer</code> is removed as a shutdown hook.
     */
    public ProcessDestroyer() {
        try {
            Class[] paramTypes = {Thread.class};
            addShutdownHookMethod =
                Runtime.class.getMethod("addShutdownHook", paramTypes);

            removeShutdownHookMethod =
                Runtime.class.getMethod("removeShutdownHook", paramTypes);
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers this <code>ProcessDestroyer</code> as a shutdown hook,
     * uses reflection to ensure pre-JDK 1.3 compatibility.
     */
    private void addShutdownHook() {
        if (addShutdownHookMethod != null && !running) {
            destroyProcessThread = new ProcessDestroyerImpl();
            Object[] args = {destroyProcessThread};
            try {
                addShutdownHookMethod.invoke(Runtime.getRuntime(), args);
                added = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t != null && t.getClass() == IllegalStateException.class) {
                    running = true;
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes this <code>ProcessDestroyer</code> as a shutdown hook,
     * uses reflection to ensure pre-JDK 1.3 compatibility
     */
    private void removeShutdownHook() {
        if (removeShutdownHookMethod != null && added && !running) {
            Object[] args = {destroyProcessThread};
            try {
                Boolean removed =
                    (Boolean) removeShutdownHookMethod.invoke(
                        Runtime.getRuntime(),
                        args);
                if (!removed.booleanValue()) {
                    System.err.println("Could not remove shutdown hook");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t != null && t.getClass() == IllegalStateException.class) {
                    running = true;
                } else {
                    e.printStackTrace();
                }
            }
            destroyProcessThread.setShouldDestroy(false);
            destroyProcessThread.start();
            try {
                destroyProcessThread.join(20000);
            } catch (InterruptedException ie) {
            }
            destroyProcessThread = null;
            added = false;
        }
    }

    /**
     * Returns whether or not the ProcessDestroyer is registered as
     * as shutdown hook
     * @return true if this is currently added as shutdown hook
     */
    public boolean isAddedAsShutdownHook() {
        return added;
    }

    /**
     * Returns <code>true</code> if the specified <code>Process</code> was
     * successfully added to the list of processes to destroy upon VM exit.
     *
     * @param   process the process to add
     * @return  <code>true</code> if the specified <code>Process</code> was
     *          successfully added
     */
    public boolean add(Process process) {
        synchronized (processes) {
            if (processes.size() == 0) {
                addShutdownHook();
            }
            processes.addElement(process);
            return processes.contains(process);
        }
    }

    /**
     * Returns <code>true</code> if the specified <code>Process</code> was
     * successfully removed from the list of processes to destroy upon VM exit.
     *
     * @param   process the process to remove
     * @return  <code>true</code> if the specified <code>Process</code> was
     *          successfully removed
     */
    public boolean remove(Process process) {
        synchronized (processes) {
            boolean processRemoved = processes.removeElement(process);
            if (processRemoved && processes.size() == 0) {
                removeShutdownHook();
            }
            return processRemoved;
        }
    }

    /**
     * Invoked by the VM when it is exiting.
     */
    public void run() {
        synchronized (processes) {
            running = true;
            Enumeration e = processes.elements();
            while (e.hasMoreElements()) {
                ((Process) e.nextElement()).destroy();
            }
        }
    }
}

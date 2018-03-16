package org.apache.tools.ant.taskdefs;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Destroys all registered <code>Process</code>es when the VM exits.
 *
 * @author <a href="mailto:mnewcomb@tacintel.com">Michael Newcomb</a>
 * @since Ant 1.5
 */
class ProcessDestroyer
    extends Thread {

    private Vector processes = new Vector();

    /**
     * Constructs a <code>ProcessDestroyer</code> and registers it as
     * a shutdown hook.
     */
    public ProcessDestroyer() {
        try {
            Class[] paramTypes = {Thread.class};
            Method addShutdownHook =
                Runtime.class.getMethod("addShutdownHook", paramTypes);

            Object[] args = {this};
            addShutdownHook.invoke(Runtime.getRuntime(), args);
        } catch (Exception e) {
        }
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
        processes.addElement(process);
        return processes.contains(process);
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
        return processes.removeElement(process);
    }

    /**
     * Invoked by the VM when it is exiting.
     */
    public void run() {
        synchronized (processes) {
            Enumeration e = processes.elements();
            while (e.hasMoreElements()) {
                ((Process) e.nextElement()).destroy();
            }
        }
    }
}

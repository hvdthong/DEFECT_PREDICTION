package org.apache.tools.ant;

import java.lang.reflect.Method;

/**
 * Uses introspection to "adapt" an arbitrary Bean which doesn't
 * itself extend Task, but still contains an execute method and optionally
 * a setProject method.
 *
 */
public class TaskAdapter extends Task implements TypeAdapter {

    /** Object to act as a proxy for. */
    private Object proxy;

    /**
     * Checks whether or not a class is suitable to be adapted by TaskAdapter.
     *
     * This only checks conditions which are additionally required for
     * tasks adapted by TaskAdapter. Thus, this method should be called by
     * Project.checkTaskClass.
     *
     * Throws a BuildException and logs as Project.MSG_ERR for
     * conditions that will cause the task execution to fail.
     * Logs other suspicious conditions with Project.MSG_WARN.
     *
     * @param taskClass Class to test for suitability.
     *                  Must not be <code>null</code>.
     * @param project   Project to log warnings/errors to.
     *                  Must not be <code>null</code>.
     *
     * @see Project#checkTaskClass(Class)
     */
    public static void checkTaskClass(final Class taskClass,
                                      final Project project) {
        try {
            final Method executeM = taskClass.getMethod("execute", null);
            if (!Void.TYPE.equals(executeM.getReturnType())) {
                final String message = "return type of execute() should be "
                    + "void but was \"" + executeM.getReturnType() + "\" in "
                    + taskClass;
                project.log(message, Project.MSG_WARN);
            }
        } catch (NoSuchMethodException e) {
            final String message = "No public execute() in " + taskClass;
            project.log(message, Project.MSG_ERR);
            throw new BuildException(message);
        } catch (LinkageError e) {
            String message = "Could not load " + taskClass + ": " + e;
            project.log(message, Project.MSG_ERR);
            throw new BuildException(message, e);
        }
    }

    /**
     * check if the proxy class is a valid class to use
     * with this adapter.
     * the class must have a public no-arg "execute()" method.
     * @param proxyClass the class to check
     */
    public void checkProxyClass(Class proxyClass) {
        checkTaskClass(proxyClass, getProject());
    }

    /**
     * Executes the proxied task.
     *
     * @exception BuildException if the project could not be set
     * or the method could not be executed.
     */
    public void execute() throws BuildException {
        Method setProjectM = null;
        try {
            Class c = proxy.getClass();
            setProjectM =
                c.getMethod("setProject", new Class[] {Project.class});
            if (setProjectM != null) {
                setProjectM.invoke(proxy, new Object[] {getProject()});
            }
        } catch (NoSuchMethodException e) {
        } catch (Exception ex) {
            log("Error setting project in " + proxy.getClass(),
                Project.MSG_ERR);
            throw new BuildException(ex);
        }


        Method executeM = null;
        try {
            Class c = proxy.getClass();
            executeM = c.getMethod("execute", new Class[0]);
            if (executeM == null) {
                log("No public execute() in " + proxy.getClass(),
                    Project.MSG_ERR);
                throw new BuildException("No public execute() in "
                    + proxy.getClass());
            }
            executeM.invoke(proxy, null);
            return;
        } catch (java.lang.reflect.InvocationTargetException ie) {
            log("Error in " + proxy.getClass(), Project.MSG_VERBOSE);
            Throwable t = ie.getTargetException();
            if (t instanceof BuildException) {
                throw ((BuildException) t);
            } else {
                throw new BuildException(t);
            }
        } catch (Exception ex) {
            log("Error in " + proxy.getClass(), Project.MSG_VERBOSE);
            throw new BuildException(ex);
        }

    }

    /**
     * Sets the target object to proxy for.
     *
     * @param o The target object. Must not be <code>null</code>.
     */
    public void setProxy(Object o) {
        this.proxy = o;
    }

    /**
     * Returns the target object being proxied.
     *
     * @return the target proxy object
     */
    public Object getProxy() {
        return proxy;
    }

}

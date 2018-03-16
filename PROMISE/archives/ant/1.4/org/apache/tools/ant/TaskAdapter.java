package org.apache.tools.ant;

import java.lang.reflect.*;
import java.util.*;

/**
 *  Use introspection to "adapt" an arbitrary Bean ( not extending Task, but with similar
 *  patterns).
 *
 * @author costin@dnt.ro
 */
public class TaskAdapter extends Task {

    Object proxy;
    
    /**
     * Do the execution.
     */
    public void execute() throws BuildException {
        Method setProjectM = null;
        try {
            Class c = proxy.getClass();
            setProjectM = 
                c.getMethod( "setProject", new Class[] {Project.class});
            if(setProjectM != null) {
                setProjectM.invoke(proxy, new Object[] {project});
            }
        } catch( Exception ex ) {
            log("Error setting project in " + proxy.getClass(), 
                Project.MSG_ERR);
            throw new BuildException( ex );
        }


        Method executeM=null;
        try {
            Class c=proxy.getClass();
            executeM=c.getMethod( "execute", new Class[0] );
            if( executeM == null ) {
                log("No execute in " + proxy.getClass(), Project.MSG_ERR);
                throw new BuildException("No execute in " + proxy.getClass());
            }
            executeM.invoke(proxy, null);
            return; 
        } catch( Exception ex ) {
            log("Error in " + proxy.getClass(), Project.MSG_ERR);
            throw new BuildException( ex );
        }

    }
    
    /**
     * Set the target object class
     */
    public void setProxy(Object o) {
        this.proxy = o;
    }

    public Object getProxy() {
        return this.proxy ;
    }

}

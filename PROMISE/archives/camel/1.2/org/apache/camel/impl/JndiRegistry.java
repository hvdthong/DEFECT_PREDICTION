package org.apache.camel.impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Registry;

/**
 * A {@link Registry} implementation which looks up the objects in JNDI
 * 
 * @version $Revision: 1.1 $
 */
public class JndiRegistry implements Registry {
    private Context context;

    public JndiRegistry() {
    }

    public JndiRegistry(Context context) {
        this.context = context;
    }

    public <T> T lookup(String name, Class<T> type) {
        Object value = lookup(name);
        return type.cast(value);
    }

    public Object lookup(String name) {
        try {
            return getContext().lookup(name);
        } catch (NameNotFoundException e) {
            return null;
        } catch (NamingException e) {
            throw new RuntimeCamelException(e);
        }
    }

    public void bind(String s, Object o) {
        try {
            getContext().bind(s, o);
        } catch (NamingException e) {
            throw new RuntimeCamelException(e);
        }
    }

    public void close() throws NamingException {
        getContext().close();
    }

    public Context getContext() throws NamingException {
        if (context == null) {
            context = createContext();
        }
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected Context createContext() throws NamingException {
        Hashtable properties = new Hashtable(System.getProperties());
        return new InitialContext(properties);
    }
}

package org.apache.camel.util.jndi;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * A factory of the Camel InitialContext which allows a Map to be used to create a
 * JNDI context.
 *
 * @version $Revision: 656978 $
 */
public class CamelInitialContextFactory implements InitialContextFactory {

    /**
     * Creates a new context with the given environment.
     *
     * @param  environment  the environment, must not be <tt>null</tt>
     * @return the created context.
     * @throws NamingException is thrown if creation failed.
     */
    public Context getInitialContext(Hashtable environment) throws NamingException {
        try {
            return new JndiContext(environment);
        } catch (NamingException e) {
            throw e;
        } catch (Exception e) {
            NamingException exception = new NamingException(e.getMessage());
            exception.initCause(e);
            throw exception;
        }
    }
}

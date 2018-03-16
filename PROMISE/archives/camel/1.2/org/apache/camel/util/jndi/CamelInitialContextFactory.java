package org.apache.camel.util.jndi;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * A factory of the Cameel InitialContext which allows a Map to be used to create a
 * JNDI context.
 *
 * @version $Revision: 1.2 $
 */
public class CamelInitialContextFactory implements InitialContextFactory {

    public Context getInitialContext(Hashtable environment) throws NamingException {
        try {
            return new JndiContext(environment);
        }
        catch (NamingException e) {
            throw e;
        }
        catch (Exception e) {
            NamingException exception = new NamingException(e.getMessage());
            exception.initCause(e);
            throw exception;
        }
    }
}

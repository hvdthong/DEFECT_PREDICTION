package org.apache.xpath.jaxp;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.res.XPATHErrorResources;
import org.apache.xalan.res.XSLMessages;

import org.apache.xpath.functions.FuncExtFunction;
import java.util.Vector;
import java.util.ArrayList;
import javax.xml.namespace.QName;

/**
 * 
 * @author Ramesh Mandava ( ramesh.mandava@sun.com )
 */
public class JAXPExtensionsProvider implements ExtensionsProvider {
    	
    private final XPathFunctionResolver resolver;
    private boolean extensionInvocationDisabled = false;
	
    public JAXPExtensionsProvider(XPathFunctionResolver resolver) {
        this.resolver = resolver;
        this.extensionInvocationDisabled = false;
    }

    public JAXPExtensionsProvider(XPathFunctionResolver resolver, 
        boolean featureSecureProcessing ) {
        this.resolver = resolver;
        this.extensionInvocationDisabled = featureSecureProcessing;
    }

    /**
     * Is the extension function available?
     */

    public boolean functionAvailable(String ns, String funcName)
          throws javax.xml.transform.TransformerException {
      try {
        if ( funcName == null ) {
            String fmsg = XSLMessages.createXPATHMessage( 
                XPATHErrorResources.ER_ARG_CANNOT_BE_NULL,
                new Object[] {"Function Name"} );
            throw new NullPointerException ( fmsg ); 
        }
        javax.xml.namespace.QName myQName = new QName( ns, funcName );
        javax.xml.xpath.XPathFunction xpathFunction = 
            resolver.resolveFunction ( myQName, 0 );
        if (  xpathFunction == null ) {
            return false;
        }
        return true;
      } catch ( Exception e ) {
        return false;
      }
       

    }
        

    /**
     * Is the extension element available?
     */
    public boolean elementAvailable(String ns, String elemName)
          throws javax.xml.transform.TransformerException {
        return false;
    }

    /**
     * Execute the extension function.
     */
    public Object extFunction(String ns, String funcName, Vector argVec,
        Object methodKey) throws javax.xml.transform.TransformerException {
        try {

            if ( funcName == null ) {
                String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_ARG_CANNOT_BE_NULL,
                    new Object[] {"Function Name"} );
                throw new NullPointerException ( fmsg ); 
            }
            javax.xml.namespace.QName myQName = new QName( ns, funcName );

            if ( extensionInvocationDisabled ) {
                String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED,
                    new Object[] { myQName.toString() } );
                throw new XPathFunctionException ( fmsg );
            }

            int arity = argVec.size();

            javax.xml.xpath.XPathFunction xpathFunction = 
                resolver.resolveFunction ( myQName, arity );

            ArrayList argList = new ArrayList( arity);
            for ( int i=0; i<arity; i++ ) {
                Object argument = argVec.elementAt( i );
                if ( argument instanceof XNodeSet ) {
                    argList.add ( i, ((XNodeSet)argument).nodelist() );
                } else if ( argument instanceof XObject ) {
                    Object passedArgument = ((XObject)argument).object();
                    argList.add ( i, passedArgument );
                } else {
                    argList.add ( i, argument );
                }
            }

            return ( xpathFunction.evaluate ( argList ));
        } catch ( XPathFunctionException xfe ) {
            throw new org.apache.xml.utils.WrappedRuntimeException ( xfe );
        } catch ( Exception e ) {
            throw new javax.xml.transform.TransformerException ( e );
        }
    
    }

    /**
     * Execute the extension function.
     */
    public Object extFunction(FuncExtFunction extFunction,
                              Vector argVec)
        throws javax.xml.transform.TransformerException {
        try {
            String namespace = extFunction.getNamespace();
            String functionName = extFunction.getFunctionName();
            int arity = extFunction.getArgCount();
            javax.xml.namespace.QName myQName = 
                new javax.xml.namespace.QName( namespace, functionName );

            if ( extensionInvocationDisabled ) {
                String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED,                    new Object[] { myQName.toString() } );
                throw new XPathFunctionException ( fmsg );
            }

            XPathFunction xpathFunction = 
                resolver.resolveFunction( myQName, arity );

            ArrayList argList = new ArrayList( arity);
            for ( int i=0; i<arity; i++ ) {
                Object argument = argVec.elementAt( i );
                if ( argument instanceof XNodeSet ) {
                    argList.add ( i, ((XNodeSet)argument).nodelist() );
                } else if ( argument instanceof XObject ) {
                    Object passedArgument = ((XObject)argument).object();
                    argList.add ( i, passedArgument );
                } else {
                    argList.add ( i, argument );
                }
            }
       
            return ( xpathFunction.evaluate ( argList ));

        } catch ( XPathFunctionException xfe ) {
            throw new org.apache.xml.utils.WrappedRuntimeException ( xfe );
        } catch ( Exception e ) {
            throw new javax.xml.transform.TransformerException ( e );
        }
    }

}

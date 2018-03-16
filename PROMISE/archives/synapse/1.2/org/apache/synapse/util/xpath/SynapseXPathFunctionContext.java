package org.apache.synapse.util.xpath;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.GetPropertyFunction;
import org.jaxen.Function;
import org.jaxen.FunctionContext;
import org.jaxen.UnresolvableException;

/**
 * <p>XPath function context to be used when resolving XPath functions when using the
 * <code>SynapseXPath</code> and this resolves one function except for the standard XPath functions
 * and Jaxen extension functions.</p>
 *
 * <p>The function that has been resolved by this FunctionContext is; <tt>get-property(String)</tt>
 * which is used to retrieve message context properties</p>
 *
 * @see org.jaxen.XPathFunctionContext
 * @see org.apache.synapse.util.xpath.SynapseXPath
 */
public class SynapseXPathFunctionContext implements FunctionContext {
    /** Parent function context */
    private final FunctionContext parent;
    
    /** MessageContext to be used by the function resolver */
    private final MessageContext synCtx;

    /**
     * <p>Initialises the function context</p>
     *
     * @param parent the parent function context
     * @param synCtx message to be used for the function initialization
     *
     * @see org.jaxen.XPathFunctionContext
     */
    public SynapseXPathFunctionContext(FunctionContext parent, MessageContext synCtx) {
        this.parent = parent;
        this.synCtx = synCtx;
    }

    /**
     * Get the function with a given namespace and name.
     * <p>
     * Only the <tt>get-property</tt> function is recognized by this class. Any other
     * function will be resolved using the parent function context.
     * 
     * @param namespaceURI namespace of the function to be resolved
     * @param prefix string prefix to be resolved
     * @param localName string local name of the function
     * @return resolved function
     * @throws UnresolvableException if the function specified does not found
     */
    public Function getFunction(String namespaceURI, String prefix, String localName)
        throws UnresolvableException {

        if (localName != null && SynapseXPathConstants.GET_PROPERTY_FUNCTION.equals(localName)) {
            
            GetPropertyFunction getPropertyFunc = new GetPropertyFunction();
            getPropertyFunc.setSynCtx(synCtx);

            return getPropertyFunc;
        }

        return parent.getFunction(namespaceURI, prefix, localName);
    }
}

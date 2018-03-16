package org.apache.xpath.jaxp;

import org.apache.xpath.res.XPATHErrorResources;
import org.apache.xalan.res.XSLMessages;

import javax.xml.XMLConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

/**
 * The XPathFactory builds XPaths.
 *
 * @version $Revision: 338151 $
 * @author  Ramesh Mandava
 */
public  class XPathFactoryImpl extends XPathFactory {
	
	/**
	 * <p>Name of class as a constant to use for debugging.</p>
	 */
	private static final String CLASS_NAME = "XPathFactoryImpl";
	
	/**
	 *<p>XPathFunctionResolver for this XPathFactory and created XPaths.</p>
	 */
	private XPathFunctionResolver xPathFunctionResolver = null;
	
	/**
	 * <p>XPathVariableResolver for this XPathFactory and created XPaths</p>
	 */
	private XPathVariableResolver xPathVariableResolver = null;

	/**
	 * <p>State of secure processing feature.</p>
	 */
	private boolean featureSecureProcessing = false;
		
	/**
	 * <p>Is specified object model supported by this 
         * <code>XPathFactory</code>?</p>
	 * 
	 * @param objectModel Specifies the object model which the returned
         * <code>XPathFactory</code> will understand.
	 *  
	 * @return <code>true</code> if <code>XPathFactory</code> supports 
         * <code>objectModel</code>, else <code>false</code>.
	 * 
	 * @throws NullPointerException If <code>objectModel</code> is <code>null</code>.
	 * @throws IllegalArgumentException If <code>objectModel.length() == 0</code>.
	 */
	public boolean isObjectModelSupported(String objectModel) {
		
            if (objectModel == null) {
                String fmsg = XSLMessages.createXPATHMessage(
                        XPATHErrorResources.ER_OBJECT_MODEL_NULL,
                        new Object[] { this.getClass().getName() } );

                throw new NullPointerException( fmsg );
            }
		
            if (objectModel.length() == 0) {
                String fmsg = XSLMessages.createXPATHMessage(
                        XPATHErrorResources.ER_OBJECT_MODEL_EMPTY,
                        new Object[] { this.getClass().getName() } );
                throw new IllegalArgumentException( fmsg );
            }
		
            if (objectModel.equals(XPathFactory.DEFAULT_OBJECT_MODEL_URI)) {
                return true;
            }
		
            return false;
	}

        /**
         * <p>Returns a new <code>XPath</code> object using the underlying
         * object model determined when the factory was instantiated.</p>
	 * 
	 * @return New <code>XPath</code>
	 */
	public javax.xml.xpath.XPath newXPath() {
	    return new org.apache.xpath.jaxp.XPathImpl(
                    xPathVariableResolver, xPathFunctionResolver,
                    featureSecureProcessing );
	}
	    
	/**
	 * <p>Set a feature for this <code>XPathFactory</code> and 
         * <code>XPath</code>s created by this factory.</p>
	 * 
	 * <p>
	 * Feature names are fully qualified {@link java.net.URI}s.
	 * Implementations may define their own features.
	 * An {@link XPathFactoryConfigurationException} is thrown if this
         * <code>XPathFactory</code> or the <code>XPath</code>s
	 *  it creates cannot support the feature.
	 * It is possible for an <code>XPathFactory</code> to expose a feature
         * value but be unable to change its state.
	 * </p>
	 * 
	 * <p>See {@link javax.xml.xpath.XPathFactory} for full documentation
         * of specific features.</p>
	 * 
	 * @param name Feature name.
	 * @param value Is feature state <code>true</code> or <code>false</code>.
	 *  
	 * @throws XPathFactoryConfigurationException if this 
         * <code>XPathFactory</code> or the <code>XPath</code>s
	 *   it creates cannot support this feature.
         * @throws NullPointerException if <code>name</code> is 
         * <code>null</code>.
	 */
	public void setFeature(String name, boolean value)
		throws XPathFactoryConfigurationException {
			
            if (name == null) {
                String fmsg = XSLMessages.createXPATHMessage(
                        XPATHErrorResources.ER_FEATURE_NAME_NULL,
                        new Object[] { CLASS_NAME, new Boolean( value) } );
                throw new NullPointerException( fmsg );
             }
		
            if (name.equals(XMLConstants.FEATURE_SECURE_PROCESSING)) {

                featureSecureProcessing = value;
						
                return;
            }
		
            String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_FEATURE_UNKNOWN,
                    new Object[] { name, CLASS_NAME, new Boolean(value) } );
            throw new XPathFactoryConfigurationException( fmsg );
	}

	/**
	 * <p>Get the state of the named feature.</p>
	 * 
	 * <p>
	 * Feature names are fully qualified {@link java.net.URI}s.
	 * Implementations may define their own features.
	 * An {@link XPathFactoryConfigurationException} is thrown if this
         * <code>XPathFactory</code> or the <code>XPath</code>s
	 * it creates cannot support the feature.
	 * It is possible for an <code>XPathFactory</code> to expose a feature 
         * value but be unable to change its state.
	 * </p>
	 * 
	 * @param name Feature name.
	 * 
	 * @return State of the named feature.
	 * 
	 * @throws XPathFactoryConfigurationException if this 
         * <code>XPathFactory</code> or the <code>XPath</code>s
	 *   it creates cannot support this feature.
         * @throws NullPointerException if <code>name</code> is 
         * <code>null</code>.
	 */
	public boolean getFeature(String name)
		throws XPathFactoryConfigurationException {

            if (name == null) {
                String fmsg = XSLMessages.createXPATHMessage(
                        XPATHErrorResources.ER_GETTING_NULL_FEATURE,
                        new Object[] { CLASS_NAME } );
                throw new NullPointerException( fmsg );
            }
		
            if (name.equals(XMLConstants.FEATURE_SECURE_PROCESSING)) {
                return featureSecureProcessing;
            }
		
            String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_GETTING_UNKNOWN_FEATURE,
                    new Object[] { name, CLASS_NAME } );

            throw new XPathFactoryConfigurationException( fmsg );
        }
		
	/**
         * <p>Establish a default function resolver.</p>
         * 
	 * <p>Any <code>XPath</code> objects constructed from this factory will use
	 * the specified resolver by default.</p>
	 *
	 * <p>A <code>NullPointerException</code> is thrown if 
         * <code>resolver</code> is <code>null</code>.</p>
         * 
	 * @param resolver XPath function resolver.
	 * 
	 * @throws NullPointerException If <code>resolver</code> is 
         * <code>null</code>.
	 */
        public void setXPathFunctionResolver(XPathFunctionResolver resolver) {
			
            if (resolver == null) {
                String fmsg = XSLMessages.createXPATHMessage(
                        XPATHErrorResources.ER_NULL_XPATH_FUNCTION_RESOLVER,
                        new Object[] {  CLASS_NAME } );
                throw new NullPointerException( fmsg );
            }
			
            xPathFunctionResolver = resolver;
        }
		
	/**
	 * <p>Establish a default variable resolver.</p>
	 *
	 * <p>Any <code>XPath</code> objects constructed from this factory will use
	 * the specified resolver by default.</p>
	 * 
	 * <p>A <code>NullPointerException</code> is thrown if <code>resolver</code> is <code>null</code>.</p>
	 * 
	 * @param resolver Variable resolver.
	 * 
	 *  @throws NullPointerException If <code>resolver</code> is 
         * <code>null</code>.
	 */
	public void setXPathVariableResolver(XPathVariableResolver resolver) {

		if (resolver == null) {
                    String fmsg = XSLMessages.createXPATHMessage(
                            XPATHErrorResources.ER_NULL_XPATH_VARIABLE_RESOLVER,
                            new Object[] {  CLASS_NAME } );
		    throw new NullPointerException( fmsg );
		}
			
		xPathVariableResolver = resolver;
	}
}




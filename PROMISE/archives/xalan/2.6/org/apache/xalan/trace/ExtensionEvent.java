 
package org.apache.xalan.trace;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.xalan.transformer.TransformerImpl;

/**
 * An event representing an extension call.
 */
public class ExtensionEvent {

	public static final int DEFAULT_CONSTRUCTOR = 0;
	public static final int METHOD = 1;
	public static final int CONSTRUCTOR = 2;
	
	public final int m_callType; 
	public final TransformerImpl m_transformer;
	public final Object m_method;
	public final Object m_instance;
	public final Object[] m_arguments;
	
	
	public ExtensionEvent(TransformerImpl transformer, Method method, Object instance, Object[] arguments) {
		m_transformer = transformer;
		m_method = method;
		m_instance = instance;
		m_arguments = arguments;
		m_callType = METHOD;
	}		

	public ExtensionEvent(TransformerImpl transformer, Constructor constructor, Object[] arguments) {
		m_transformer = transformer;
		m_instance = null;		
		m_arguments = arguments;
		m_method = constructor;
		m_callType = CONSTRUCTOR;		
	}

	public ExtensionEvent(TransformerImpl transformer, Class clazz) {
		m_transformer = transformer;
		m_instance = null;
		m_arguments = null;
		m_method = clazz;
		m_callType = DEFAULT_CONSTRUCTOR;
	}

}


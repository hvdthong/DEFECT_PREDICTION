package org.apache.camel.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;

/**
 * @version $Revision: 519943 $
 */
public class InterceptorBuilder implements ProcessorFactory {
    private final List<DelegateProcessor> intercepts = new ArrayList<DelegateProcessor>();
	private final FromBuilder parent;
	private FromBuilder target;

	public InterceptorBuilder(FromBuilder parent) {
        this.parent = parent;
	}
	
	@Fluent("interceptor")
	public InterceptorBuilder add(@FluentArg("ref") DelegateProcessor interceptor) {
		intercepts.add(interceptor);
		return this;
	}
	
	@Fluent(callOnElementEnd=true)
    public FromBuilder target() {
        this.target = new FromBuilder(parent);
        return target;
    }

    public Processor createProcessor() throws Exception {
    	
    	if( target == null ) 
    		throw new RuntimeCamelException("target provided.");
    	
    	DelegateProcessor first=null;
    	DelegateProcessor last=null;
        for (DelegateProcessor p : intercepts) {
            if( first == null ) {
            	first = p;
            }
            if( last != null ) {
            	last.setNext(p);
            }
            last = p;
        }
        
        Processor p = target.createProcessor();
        if( last != null ) {
        	last.setNext(p);
        }
        return first == null ? p : first;
    }
}

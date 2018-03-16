package org.apache.camel.builder;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Policy;

/**
 * @version $Revision: 519943 $
 */
public class PolicyBuilder implements ProcessorFactory {
    private final ArrayList<Policy> policies = new ArrayList<Policy>();
	private final FromBuilder parent;
	private FromBuilder target;

	public PolicyBuilder(FromBuilder parent) {
        this.parent = parent;
	}
	
	@Fluent("policy")
	public PolicyBuilder add(@FluentArg("ref") Policy interceptor) {
		policies.add(interceptor);
		return this;
	}
	
	@Fluent(callOnElementEnd=true)
    public FromBuilder target() {
        this.target = new FromBuilder(parent);
        return target;
    }

    public Processor createProcessor() throws Exception {
    	
    	if( target == null ) 
    		throw new RuntimeCamelException("target not provided.");
    	
        Processor last = target.createProcessor();
    	Collections.reverse(policies);
        for (Policy p : policies) {
            last = p.wrap(last);
        }
        
        return last;
    }
}

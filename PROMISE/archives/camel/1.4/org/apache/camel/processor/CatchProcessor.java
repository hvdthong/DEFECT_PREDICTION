package org.apache.camel.processor;

import java.util.List;

import org.apache.camel.Processor;

/**
 * A processor which catches exceptions.
 *
 * @version $Revision: 659760 $
 */
public class CatchProcessor extends DelegateProcessor {
    private List<Class> exceptions;

    public CatchProcessor(List<Class> exceptions, Processor processor) {
        super(processor);
        this.exceptions = exceptions;
    }

    @Override
    public String toString() {
        return "Catch[" + exceptions + " -> " + getProcessor() + "]";
    }

    public boolean catches(Throwable e) {
        for (Class type : exceptions) {
            if (type.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    public List<Class> getExceptions() {
        return exceptions;
    }
}

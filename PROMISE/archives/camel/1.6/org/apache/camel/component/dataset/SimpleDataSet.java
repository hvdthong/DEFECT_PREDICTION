package org.apache.camel.component.dataset;

import org.apache.camel.Processor;

/**
 * A simple DataSet that allows a static payload to be used to create each message exchange
 * along with using a pluggable transformer to randomize the message.
 *
 * @version $Revision: 647890 $
 */
public class SimpleDataSet extends DataSetSupport {
    private Object defaultBody = "<hello>world!</hello>";
    private Processor inputTransformer;

    public SimpleDataSet() {
    }

    public SimpleDataSet(int size) {
        super(size);
    }


    public Object getDefaultBody() {
        return defaultBody;
    }

    public void setDefaultBody(Object defaultBody) {
        this.defaultBody = defaultBody;
    }

    public Processor getInputTransformer() {
        return inputTransformer;
    }

    public void setInputTransformer(Processor inputTransformer) {
        this.inputTransformer = inputTransformer;
    }


    /**
     * Creates the message body for a given message
     */
    protected Object createMessageBody(long messageIndex) {
        return getDefaultBody();
    }
}

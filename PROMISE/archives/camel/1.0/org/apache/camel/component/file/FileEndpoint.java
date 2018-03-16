package org.apache.camel.component.file;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ScheduledPollEndpoint;

import java.io.File;

/**
 *
 * @version $Revision: 523016 $
 */
public class FileEndpoint extends ScheduledPollEndpoint<FileExchange> {
    private File file;
    private boolean autoCreate=true;

    protected FileEndpoint(File file, String endpointUri, FileComponent component) {
        super(endpointUri, component);
        this.file = file;
    }

    /**
     * @return a Producer
     * @throws Exception
     * @see org.apache.camel.Endpoint#createProducer()
     */
    public Producer<FileExchange> createProducer() throws Exception {
        Producer<FileExchange> result = new FileProducer(this);
        return result;
    }

    /**
     * @param file
     * @return a Consumer
     * @throws Exception
     * @see org.apache.camel.Endpoint#createConsumer(org.apache.camel.Processor)
     */
    public Consumer<FileExchange> createConsumer(Processor file) throws Exception {
        Consumer<FileExchange> result = new FileConsumer(this, file);
        configureConsumer(result);
        return result;
    }

    /**
     * @param file
     * @return a FileExchange
     * @see org.apache.camel.Endpoint#createExchange()
     */
    public FileExchange createExchange(File file) {
        return new FileExchange(getContext(), file);
    }

    /**
     * @return an Exchange
     * @see org.apache.camel.Endpoint#createExchange()
     */
    public FileExchange createExchange() {
        return createExchange(getFile());
    }

    public File getFile() {
        if (autoCreate && !file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public boolean isSingleton() {
        return true;
    }

    
    /**
     * @return the autoCreate
     */
    public boolean isAutoCreate(){
        return this.autoCreate;
    }

    
    /**
     * @param autoCreate the autoCreate to set
     */
    public void setAutoCreate(boolean autoCreate){
        this.autoCreate=autoCreate;
    }
}

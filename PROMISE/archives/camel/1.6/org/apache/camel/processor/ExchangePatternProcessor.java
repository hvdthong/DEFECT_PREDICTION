package org.apache.camel.processor;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;

public class ExchangePatternProcessor implements AsyncProcessor {
    private ExchangePattern exchangePattern = ExchangePattern.InOnly;
    
    public ExchangePatternProcessor() {
        
    }
    
    public ExchangePatternProcessor(ExchangePattern ep) {
        setExchangePattern(ep);
    }
    
    public void setExchangePattern(ExchangePattern ep) {
        exchangePattern = ep;
    }
   
    public void process(Exchange exchange) throws Exception {
        exchange.setPattern(exchangePattern);        
    }

    public boolean process(Exchange exchange, AsyncCallback callback) {
        exchange.setPattern(exchangePattern);
        callback.done(true);
        return true;
    }

}

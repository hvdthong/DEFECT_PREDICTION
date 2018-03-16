package org.apache.camel;

import org.apache.camel.spi.HeaderFilterStrategy;

/**
 * An interface to represent an object which can make use of
 * injected {@link HeaderFilterStrategy}.
 * 
 * @since 1.5
 * @version $Revision: 682597 $
 */
public interface HeaderFilterStrategyAware {

    HeaderFilterStrategy getHeaderFilterStrategy();
    
    void setHeaderFilterStrategy(HeaderFilterStrategy strategy);
}

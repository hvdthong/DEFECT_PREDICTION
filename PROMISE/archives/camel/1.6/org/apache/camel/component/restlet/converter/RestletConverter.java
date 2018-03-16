package org.apache.camel.component.restlet.converter;

import org.apache.camel.Converter;
import org.restlet.data.MediaType;
import org.restlet.data.Method;

/**
 *
 * @version $Revision: 740599 $
 */
@Converter
public class RestletConverter {

    @Converter
    public Method toMethod(String name) {
        return Method.valueOf(name.toUpperCase());
    }
    
    @Converter
    public MediaType toMediaType(String name) {
        return MediaType.valueOf(name);
    }

}

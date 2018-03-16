package org.apache.camel.rest.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.PackagesResourceConfig;

/**
 * @version $Revision: 706111 $
 */
public class CamelResourceConfig extends PackagesResourceConfig {
    public CamelResourceConfig() {
        super("org.apache.camel.rest");
    }

    public Map<String, MediaType> getMediaTypeMappings() {
        Map<String, MediaType> m = new HashMap<String, MediaType>();
        m.put("html", MediaType.TEXT_HTML_TYPE);
        m.put("xml", MediaType.APPLICATION_XML_TYPE);
        m.put("json", MediaType.APPLICATION_JSON_TYPE);
        return m;
    }
}

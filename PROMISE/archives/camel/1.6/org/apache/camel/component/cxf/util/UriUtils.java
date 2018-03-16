package org.apache.camel.component.cxf.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.camel.component.cxf.CxfConstants;
import org.apache.cxf.common.classloader.ClassLoaderUtils;

public final class UriUtils {

    private UriUtils() {
    }
    
    static URL getWsdlUrl(final URI uri) throws MalformedURLException {
        URL wsdlUrl = null;
        
        if (uri.getScheme().equals(CxfConstants.PROTOCOL_NAME_RES)) {       
            if (uri.getPath() != null) {
                String path = uri.isAbsolute() ? getRelativePath(uri) : uri.getPath();
                wsdlUrl = ClassLoaderUtils.getResource(path, UriUtils.class);
            }
        } else {
            wsdlUrl = new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath());
        }

        return wsdlUrl;
    }

    private static String getRelativePath(URI uri) {
        URI base = null;
        try {
            base = new URI(CxfConstants.PROTOCOL_NAME_RES, "", "/", "");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return base.relativize(uri).getPath();
    }
}


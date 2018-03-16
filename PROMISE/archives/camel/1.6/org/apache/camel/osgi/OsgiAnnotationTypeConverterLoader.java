package org.apache.camel.osgi;

import org.apache.camel.impl.converter.AnnotationTypeConverterLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;

public class OsgiAnnotationTypeConverterLoader extends AnnotationTypeConverterLoader {
    private static final transient Log LOG = LogFactory.getLog(OsgiAnnotationTypeConverterLoader.class);
    
    public OsgiAnnotationTypeConverterLoader(BundleContext context) {
        super(new OsgiResolverUtil(context));
    }
    
    protected String[] findPackageNames() {
        return Activator.findTypeConverterPackageNames();
    }
    
    

}

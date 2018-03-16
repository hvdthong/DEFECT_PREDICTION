package org.apache.camel.component.restlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 * A simple router that routes requests to target Restlets based on method. 
 * 
 * @version $Revision: 730276 $
 */
class MethodBasedRouter extends Restlet {
    private static final Log LOG = LogFactory.getLog(MethodBasedRouter.class);
    private String uriPattern;
    private Map<Method, Restlet> routes = new ConcurrentHashMap<Method, Restlet>();
    private AtomicBoolean hasBeenAttachedFlag = new AtomicBoolean(false);

    MethodBasedRouter(String uriPattern) {
        this.uriPattern = uriPattern;
    }
    
    @Override
    public void handle(Request request, Response response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("MethodRouter (" + uriPattern + ") received request method: " 
                    + request.getMethod());
        }
        
        Restlet target = routes.get(request.getMethod());
        if (target != null) {
            target.handle(request, response);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No route for request method: " + request.getMethod());
            }
            response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }
    }

    void addRoute(Method method, Restlet target) {
        routes.put(method, target);
    }
    
    void removeRoute(Method method) {
        routes.remove(method);
    }
    
    /**
     * This method does "test-and-set" on the underlying flag that indicates
     * whether this router restlet has been attached to a server or not.  It 
     * is the caller's responsibility to perform the "attach" when this method 
     * returns false. 
     * 
     * @return true only this method is called the first time.
     */
    boolean hasBeenAttached() {
        return hasBeenAttachedFlag.getAndSet(true);
    }
    
}

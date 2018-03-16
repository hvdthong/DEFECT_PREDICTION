package org.apache.synapse.core.axis2;

import org.apache.axis2.transport.http.AxisServlet;
import org.apache.axis2.transport.http.ListingAgent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.ServerManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Extends axis2 servlet functionality so that  avoid  starting listeners again
 */

public class SynapseAxisServlet extends AxisServlet {
    private final static Log log = LogFactory.getLog(SynapseAxisServlet.class);
    
    /**
     * Overrides init method so that avoid  starting listeners again
     *
     * @param config
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.configContext = ServerManager.getInstance().getConfigurationContext();
        this.axisConfiguration = this.configContext.getAxisConfiguration();
        servletContext.setAttribute(this.getClass().getName(), this);
        this.servletConfig = config;
        agent = new ListingAgent(configContext);
        initParams();
    }

    public void initContextRoot(HttpServletRequest req) {
        this.configContext.setContextRoot("/");
    }
}

package org.apache.synapse.core.axis2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.ServerManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * This servlet will start and stop all the listeners.
 */

public class SynapseStartUpServlet extends HttpServlet {

    private static Log log = LogFactory.getLog(SynapseStartUpServlet.class);

    public void init() throws ServletException {
        super.init();
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        ServerManager serverManager = ServerManager.getInstance();
        ServletContext servletContext = servletConfig.getServletContext();
        if ("true".equals(servletContext.getAttribute("hasAlreadyInit"))) {
            return;
        }
        String synapseHome = resolveSynapseHome(servletConfig);
        if (synapseHome != null) {
            if (synapseHome.endsWith("/")) {
                synapseHome = synapseHome.substring(0, synapseHome.lastIndexOf("/"));
            }
            System.setProperty(SynapseConstants.SYNAPSE_HOME, synapseHome);
            String axis2Repo = System.getProperty(org.apache.axis2.Constants.AXIS2_REPO);
            if (axis2Repo == null) {
                ServerManager.getInstance().setAxis2Repolocation(synapseHome + "/WEB-INF" +
                    File.separator + "repository");
                System.setProperty(org.apache.axis2.Constants.AXIS2_REPO,
                    synapseHome + "/WEB-INF" +
                        File.separator + "repository");
            }
            String axis2Xml = System.getProperty(org.apache.axis2.Constants.AXIS2_CONF);
            if (axis2Xml == null) {
                System.setProperty(org.apache.axis2.Constants.AXIS2_CONF,
                    synapseHome + File.separator
                        + "WEB-INF/conf"
                        + File.separator + org.apache.axis2.Constants.AXIS2_CONF);
            }
            String synapseXml = System.getProperty(org.apache.synapse.SynapseConstants.SYNAPSE_XML);
            if (synapseXml == null) {
                System.setProperty(org.apache.synapse.SynapseConstants.SYNAPSE_XML,
                    synapseHome + File.separator
                        + "WEB-INF/conf"
                        + File.separator + org.apache.synapse.SynapseConstants.SYNAPSE_XML);

            }
        } else {
            log.fatal("Can not resolve synapse home  : startup failed");
            return;
        }
        serverManager.start();
        servletContext.setAttribute("hasAlreadyInit", "true");
    }


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
        IOException {
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException,
        IOException {
    }

    public void destroy() {
        try {
            ServerManager serverManager = ServerManager.getInstance();
        } catch (Exception ignored) {
        }
    }

    private String resolveSynapseHome(ServletConfig servletConfig) {
        String synapseHomeAsParam = servletConfig.getInitParameter(SynapseConstants.SYNAPSE_HOME);
        if (synapseHomeAsParam != null) {
            if (synapseHomeAsParam.endsWith("/")) {
                return synapseHomeAsParam.substring(0, synapseHomeAsParam.lastIndexOf("/"));
            }
        }
        String synapseHome = System.getProperty(SynapseConstants.SYNAPSE_HOME);
        if (synapseHome == null || "".equals(synapseHome)) {
            ServletContext servletContext = servletConfig.getServletContext();
            String webinfPath = servletContext.getRealPath("WEB-INF");
            if (webinfPath != null) {
                synapseHome = webinfPath.substring(0, webinfPath.lastIndexOf("WEB-INF"));
                if (synapseHome != null) {
                    if (synapseHome.endsWith("/")) {
                        synapseHome = synapseHome.substring(0, synapseHome.lastIndexOf("/"));
                    }
                }
            }
        }
        return synapseHome;
    }
}

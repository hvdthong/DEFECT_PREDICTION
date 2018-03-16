package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;


import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;

/**
 * Condition to wait for a HTTP request to succeed. Its attribute(s) are:
 *   url - the URL of the request.
 *   errorsBeginAt - number at which errors begin at; default=400. 
 * @author <a href="mailto:denis@network365.com">Denis Hennessy</a>
 * @since Ant 1.5
 */
public class Http extends ProjectComponent implements Condition {
    private String spec = null;

    public void setUrl(String url) {
        spec = url;
    }

    private int errorsBeginAt=400;
    
    public void setErrorsBeginAt(int errorsBeginAt) {
        this.errorsBeginAt=errorsBeginAt;
    }
    
    public boolean eval() throws BuildException {
        if (spec == null) {
            throw new BuildException("No url specified in http condition");
        }
        log("Checking for " + spec, Project.MSG_VERBOSE);
        try {
            URL url = new URL(spec);
            try {
                URLConnection conn = url.openConnection();
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection http = (HttpURLConnection) conn;
                    int code = http.getResponseCode();
                    log("Result code for " + spec + " was " + code, 
                        Project.MSG_VERBOSE);
                    if (code > 0 && code < errorsBeginAt) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (java.io.IOException e) {
                return false;
            }
        } catch (MalformedURLException e) {
            throw new BuildException("Badly formed URL: " + spec, e);
        }
        return true;
    }
}

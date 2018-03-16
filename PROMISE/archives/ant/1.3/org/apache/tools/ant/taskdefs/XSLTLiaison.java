package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;

/**
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @version $Revision: 268392 $ $Date: 2001-01-03 22:18:52 +0800 (周三, 2001-01-03) $
 */
public interface XSLTLiaison {

    public void setStylesheet(String fileName) throws Exception;

    public void addParam(String name, String expression) throws Exception;

    public void transform(String infile, String outfile) throws Exception;


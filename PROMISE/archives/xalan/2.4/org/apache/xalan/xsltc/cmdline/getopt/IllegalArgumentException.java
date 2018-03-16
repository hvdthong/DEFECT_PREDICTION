package org.apache.xalan.xsltc.cmdline.getopt;

import org.apache.xalan.xsltc.cmdline.getopt.GetOptsException;

class IllegalArgumentException extends GetOptsException{
    public IllegalArgumentException(String msg){
	super(msg);
    }
}

package org.apache.xalan.xsltc.cmdline.getopt; 

import org.apache.xalan.xsltc.cmdline.getopt.GetOptsException;

class MissingOptArgException extends GetOptsException{
    public MissingOptArgException(String msg){
	super(msg);
    }
}

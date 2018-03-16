package org.apache.xalan.xsltc.cmdline.getopt; 


/**
 * @author G Todd Miller 
 */
class MissingOptArgException extends GetOptsException{
    public MissingOptArgException(String msg){
	super(msg);
    }
}

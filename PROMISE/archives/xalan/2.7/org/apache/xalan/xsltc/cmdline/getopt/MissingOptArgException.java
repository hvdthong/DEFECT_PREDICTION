package org.apache.xalan.xsltc.cmdline.getopt; 


/**
 * @author G Todd Miller 
 */
class MissingOptArgException extends GetOptsException{
    static final long serialVersionUID = -1972471465394544822L;
    public MissingOptArgException(String msg){
	super(msg);
    }
}

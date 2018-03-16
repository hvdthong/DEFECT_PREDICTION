package org.apache.xalan.xsltc.cmdline.getopt; 


class MissingOptArgException extends GetOptsException{
    public MissingOptArgException(String msg){
	super(msg);
    }
}

package org.apache.tools.ant.taskdefs.optional.perforce;

import java.io.*;
import org.apache.tools.ant.*;

public abstract class P4HandlerAdapter implements P4Handler {

    public abstract void process(String line);


    String p4input = "";

    public void setOutput(String p4Input) {
        this.p4input = p4Input;
    }


    public void start() throws BuildException {

    try{
        if(p4input != null && p4input.length() >0 && os != null) {
                    os.write(p4input.getBytes());
                    os.flush();
                    os.close();
            }

      
        BufferedReader input = new BufferedReader(
                                     new InputStreamReader(
                                       new SequenceInputStream(is,es)));

            String line;
	    	while((line = input.readLine()) != null) {
	    	   process(line);
	    	}
	    	
	    	input.close();


        }catch(Exception e) {
            throw new BuildException(e);
        }
    }


    public void setProcessInputStream(OutputStream os) throws IOException {
        this.os = os;
    }

    public void setProcessErrorStream(InputStream is) throws IOException {
        this.es = is;
    }

    public void setProcessOutputStream(InputStream is) throws IOException {
        this.is = is;
    }

    public void stop(){}
}

package org.apache.tools.ant.taskdefs.optional.perforce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.SequenceInputStream;

import org.apache.tools.ant.BuildException;

public abstract class P4HandlerAdapter implements P4Handler {

    public abstract void process(String line);


    String p4input = "";

    public void setOutput(String p4Input) {
        this.p4input = p4Input;
    }


    public void start() throws BuildException {

        try {
            if (p4input != null && p4input.length() > 0 && os != null) {
                os.write(p4input.getBytes());
                os.flush();
                os.close();
            }

            Thread output = new Thread(new Reader(is));
            Thread error = new Thread(new Reader(es));
            output.start();
            error.start();

        } catch (Exception e) {
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

    public void stop() {
    }
    
    public class Reader implements Runnable {
        protected InputStream mystream;
        public Reader(InputStream is)
        {
            mystream=is;
        }
        public void setStream(InputStream is) {
            mystream=is;
        }
        public void run() {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(mystream));

            String line;
            try {
                while ((line = input.readLine()) != null) {
                    synchronized (this){
                        process(line);
                    }
                }
            }
            catch (Exception e) {
                throw new BuildException(e);
            }
        }

    }
}


package org.apache.camel.component.file.remote;

import org.apache.camel.Processor;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

public class FtpConsumer extends RemoteFileConsumer<RemoteFileExchange> {
    private boolean recursive = true;
    private String regexPattern = "";
    private long lastPollTime = 0L;
    private final FtpEndpoint endpoint;
    private FTPClient client;

    public FtpConsumer(FtpEndpoint endpoint, Processor processor, FTPClient client) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.client = client;
    }

    public FtpConsumer(FtpEndpoint endpoint, Processor processor, FTPClient client, ScheduledExecutorService executor) {
        super(endpoint, processor, executor);
        this.endpoint = endpoint;
        this.client = client;
    }

    protected void poll() throws Exception {
        final String fileName = endpoint.getConfiguration().getFile();
        if (endpoint.getConfiguration().isDirectory()) {
            pollDirectory(fileName);
        }
        else {
            client.changeWorkingDirectory(fileName.substring(0, fileName.lastIndexOf('/')));
            final FTPFile[] files = client.listFiles(fileName.substring(fileName.lastIndexOf('/') + 1));
            pollFile(files[0]);
        }
        lastPollTime = System.currentTimeMillis();
    }

    protected void pollDirectory(String dir) throws Exception {
        client.changeWorkingDirectory(dir);
        for (FTPFile ftpFile : client.listFiles()) {
            if (ftpFile.isFile()) {
                pollFile(ftpFile);
            }
            else if (ftpFile.isDirectory()) {
                if (isRecursive()) {
                    pollDirectory(getFullFileName(ftpFile));
                }
            }
            else {
                throw new RuntimeException("");
            }
        }
    }

    protected String getFullFileName(FTPFile ftpFile) throws IOException {
        return client.printWorkingDirectory() + "/" + ftpFile.getName();
    }

    private void pollFile(FTPFile ftpFile) throws Exception {
            if (isMatched(ftpFile)) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                client.retrieveFile(ftpFile.getName(), byteArrayOutputStream);
                getProcessor().process(endpoint.createExchange(getFullFileName(ftpFile), byteArrayOutputStream));
            }
        }
    }

    protected boolean isMatched(FTPFile file) {
        boolean result = true;
        if (regexPattern != null && regexPattern.length() > 0) {
            result = file.getName().matches(getRegexPattern());
        }
        return result;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public long getLastPollTime() {
        return lastPollTime;
    }

    public void setLastPollTime(long lastPollTime) {
        this.lastPollTime = lastPollTime;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }
}

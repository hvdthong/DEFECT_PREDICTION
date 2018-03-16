package org.apache.camel.component.file.remote;

import java.io.OutputStream;
import java.util.Map;

import org.apache.camel.impl.DefaultMessage;

public class RemoteFileMessage extends DefaultMessage {
    private OutputStream outputStream;
    private String fullFileName;
    private String fileName;
    private String hostname;
    private long fileLength;

    public RemoteFileMessage() {
    }

    public RemoteFileMessage(String hostname, String fullFileName, String fileName, long fileLength, OutputStream outputStream) {
        this.hostname = hostname;
        this.fullFileName = fullFileName;
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.outputStream = outputStream;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public RemoteFileExchange getExchange() {
        return (RemoteFileExchange) super.getExchange();
    }

    @Override
    protected Object createBody() {
        if (outputStream != null) {
            return getExchange().getBinding().extractBodyFromOutputStream(getExchange(), outputStream);
        }
        return null;
    }

    @Override
    public RemoteFileMessage newInstance() {
        return new RemoteFileMessage();
    }

    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        super.populateInitialHeaders(map);
        map.put("file.remote.host", hostname);
        map.put("file.remote.fullName", fullFileName);
        map.put("file.remote.name", fileName);

        map.put("CamelFileName", fileName);
        map.put("CamelFilePath", fullFileName);
        if (fullFileName != null && fullFileName.indexOf("/") != -1) {
            String parent = fullFileName.substring(0, fullFileName.lastIndexOf("/"));
            map.put("CamelFileParent", parent);
        }
        if (fileLength > 0) {
            map.put("CamelFileLength", new Long(fileLength));
        }
    }
}

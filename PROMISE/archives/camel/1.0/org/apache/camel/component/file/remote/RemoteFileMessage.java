package org.apache.camel.component.file.remote;

import org.apache.camel.impl.DefaultMessage;

import java.io.OutputStream;
import java.util.Map;

public class RemoteFileMessage extends DefaultMessage {
    private OutputStream outputStream;
    private String fullFileName;
    private String hostname;

    public RemoteFileMessage() {
    }

    public RemoteFileMessage(String hostname, String fullFileName, OutputStream outputStream) {
        this.hostname = hostname;
        this.fullFileName = fullFileName;
        this.outputStream = outputStream;
        setMessageId(hostname + ":" + fullFileName);
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
        map.put("file.remote.name", fullFileName);
    }
}

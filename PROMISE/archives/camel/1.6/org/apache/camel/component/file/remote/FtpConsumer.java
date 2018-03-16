package org.apache.camel.component.file.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.Processor;
import org.apache.camel.component.file.FileComponent;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpConsumer extends RemoteFileConsumer<RemoteFileExchange> {

    private FtpEndpoint endpoint;
    private FTPClient client;
    private boolean loggedIn;

    public FtpConsumer(FtpEndpoint endpoint, Processor processor, FTPClient client) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.client = client;
    }

    public FtpConsumer(FtpEndpoint endpoint, Processor processor, FTPClient client,
                       ScheduledExecutorService executor) {
        super(endpoint, processor, executor);
        this.endpoint = endpoint;
        this.client = client;
    }

    protected void doStart() throws Exception {
        log.info("Starting");
        super.doStart();
    }

    protected void doStop() throws Exception {
        log.info("Stopping");
        try {
            disconnect();
        } catch (Exception e) {
            String message = "Could not disconnect from " + remoteServer()
                    + ". Reason: " + client.getReplyString() + ". Code: " + client.getReplyCode();
            log.warn(message);
        }
        super.doStop();
    }

    protected void connectIfNecessary() throws IOException {
        if (!client.isConnected() || !loggedIn) {
            if (log.isDebugEnabled()) {
                log.debug("Not connected/logged in, connecting to " + remoteServer());
            }
            loggedIn = FtpUtils.connect(client, endpoint.getConfiguration());
            if (!loggedIn) {
                return;
            }
        }
        
        log.info("Connected and logged in to " + remoteServer());
    }

    protected void disconnect() throws IOException {
        loggedIn = false;
        log.debug("Disconnecting from " + remoteServer());
        FtpUtils.disconnect(client);
    }

    protected void poll() throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("Polling " + endpoint.getConfiguration());
        }

        try {
            connectIfNecessary();

            if (!loggedIn) {
                String message = "Could not connect/login to " + endpoint.getConfiguration();
                log.warn(message);
                throw new FtpOperationFailedException(client.getReplyCode(), client.getReplyString(), message);
            }

            final String fileName = endpoint.getConfiguration().getFile();
            if (endpoint.getConfiguration().isDirectory()) {
                pollDirectory(fileName);
            } else {
                int index = fileName.lastIndexOf('/');
                if (index > -1) {
                    client.changeWorkingDirectory(fileName.substring(0, index));
                }
                final FTPFile[] files = client.listFiles(fileName.substring(index + 1));
                pollFile(files[0]);
            }

            lastPollTime = System.currentTimeMillis();

        } catch (Exception e) {
            loggedIn = false;
            if (isStopping() || isStopped()) {
                log.warn("Consumer is stopping. Ignoring caught exception: "
                         + e.getClass().getCanonicalName() + " message: " + e.getMessage());
            } else {
                log.warn("Exception occured during polling: "
                         + e.getClass().getCanonicalName() + " message: " + e.getMessage());
                disconnect();
                throw e;
            }
        }
    }

    protected void pollDirectory(String dir) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("Polling directory: " + dir);
        }
        String currentDir = client.printWorkingDirectory();

        client.changeWorkingDirectory(dir);
        for (FTPFile ftpFile : client.listFiles()) {
            if (ftpFile.isFile()) {
                pollFile(ftpFile);
            } else if (ftpFile.isDirectory()) {
                if (isRecursive()) {
                    pollDirectory(getFullFileName(ftpFile));
                }
            } else {
                log.debug("Unsupported type of FTPFile: " + ftpFile + " (not a file or directory). It is skipped.");
            }
        }

        client.changeWorkingDirectory(currentDir);
    }

    protected String getFullFileName(FTPFile ftpFile) throws IOException {
        return client.printWorkingDirectory() + "/" + ftpFile.getName();
    }

    private void pollFile(FTPFile ftpFile) throws Exception {
        if (ftpFile == null) {
            return;
        }

        if (log.isTraceEnabled()) {
            log.trace("Polling file: " + ftpFile);
        }

        boolean timestampMatched = true;
        if (isTimestamp()) {
            long ts = ftpFile.getTimestamp().getTimeInMillis();
            timestampMatched = ts > lastPollTime;
            if (log.isTraceEnabled()) {
                log.trace("The file is to old + " + ftpFile + ". lastPollTime=" + lastPollTime + " > fileTimestamp=" + ts);
            }
        }

        if (timestampMatched && isMatched(ftpFile)) {
            String fullFileName = getFullFileName(ftpFile);

            if (exclusiveReadLock) {
                acquireExclusiveReadLock(client, ftpFile);
            }

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            client.retrieveFile(ftpFile.getName(), byteArrayOutputStream);
            if (log.isDebugEnabled()) {
                log.debug("Retrieved file: " + ftpFile.getName() + " from: " + remoteServer());
            }

            RemoteFileExchange exchange = endpoint.createExchange(fullFileName, ftpFile.getName(),
                    ftpFile.getSize(), byteArrayOutputStream);

            if (isSetNames()) {
                String ftpBasePath = endpoint.getConfiguration().getFile();
                String relativePath = fullFileName.substring(ftpBasePath.length() + 1);
                relativePath = relativePath.replaceFirst("/", "");

                if (log.isDebugEnabled()) {
                    log.debug("Setting exchange filename to " + relativePath);
                }
                exchange.getIn().setHeader(FileComponent.HEADER_FILE_NAME, relativePath);
            }

            if (deleteFile) {
                if (log.isDebugEnabled()) {
                    log.debug("Deleteing file: " + ftpFile.getName() + " from: " + remoteServer());
                }
                boolean deleted = client.deleteFile(ftpFile.getName());
                if (!deleted) {
                    String message = "Can not delete file: " + ftpFile.getName() + " from: " + remoteServer();
                    throw new FtpOperationFailedException(client.getReplyCode(), client.getReplyString(), message);
                }
            } else if (isMoveFile()) {
                String fromName = ftpFile.getName();
                String toName = getMoveFileName(fromName, exchange);
                if (log.isDebugEnabled()) {
                    log.debug("Moving file: " + fromName + " to: " + toName);
                }

                boolean deleted = client.deleteFile(toName);
                if (!deleted) {
                    int lastPathIndex = toName.lastIndexOf('/');
                    if (lastPathIndex != -1) {
                        String directory = toName.substring(0, lastPathIndex);
                        if (!FtpUtils.buildDirectory(client, directory)) {
                            log.warn("Can not build directory: " + directory + " (maybe because of denied permissions)");
                        }
                    }
                }

                boolean success = client.rename(fromName, toName);
                if (!success) {
                    String message = "Can not move file: " + fromName + " to: " + toName;
                    throw new FtpOperationFailedException(client.getReplyCode(), client.getReplyString(), message);
                }
            }

            getProcessor().process(exchange);
        }
    }

    protected void acquireExclusiveReadLock(FTPClient client, FTPFile ftpFile) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("Waiting for exclusive read lock to file: " + ftpFile);
        }

        String originalName = ftpFile.getName();
        String newName = originalName + ".camelExclusiveReadLock";
        boolean exclusive = false;
        while (!exclusive) {
            exclusive = client.rename(originalName, newName);
            if (exclusive) {
                if (log.isDebugEnabled()) {
                    log.debug("Acquired exclusive read lock to file: " + originalName);
                }
                client.rename(newName, originalName);
            } else {
                log.trace("Exclusive read lock not granted. Sleeping for 1000 millis.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    protected String getFileName(Object file) {
        FTPFile ftpFile = (FTPFile) file;
        return ftpFile.getName();
    }

}

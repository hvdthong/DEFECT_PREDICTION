package org.apache.tools.ant.taskdefs.optional.ssh;

import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;

public class ScpFromMessage extends AbstractSshMessage {

    private final byte LINE_FEED = 0x0a;
    private final int BUFFER_SIZE = 1024;

    private String remoteFile;
    private File localFile;
    private boolean isRecursive = false;

    public ScpFromMessage(Session session,
                           String aRemoteFile,
                           File aLocalFile,
                           boolean recursive) {
        super(session);
        this.remoteFile = aRemoteFile;
        this.localFile = aLocalFile;
        this.isRecursive = recursive;
    }

    public void execute() throws IOException, JSchException {
        String command = "scp -f ";
        if (isRecursive) {
            command += "-r ";
        }
        command += remoteFile;
        Channel channel = openExecChannel(command);
        try {
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            sendAck(out);
            startRemoteCpProtocol(in, out, localFile);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
        log("done\n");
    }

    private void startRemoteCpProtocol(InputStream in,
                                       OutputStream out,
                                       File localFile) throws IOException {
        File startFile = localFile;
        while (true) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            while (true) {
                int read = in.read();
                if (read < 0) {
                    return;
                }
                if ((byte) read == LINE_FEED) {
                    break;
                }
                stream.write(read);
            }
            String serverResponse = stream.toString("UTF-8");
            if (serverResponse.charAt(0) == 'C') {
                parseAndFetchFile(serverResponse, startFile, out, in);
            } else if (serverResponse.charAt(0) == 'D') {
                startFile = parseAndCreateDirectory(serverResponse,
                        startFile);
                sendAck(out);
            } else if (serverResponse.charAt(0) == 'E') {
                startFile = startFile.getParentFile();
                sendAck(out);
            } else if (serverResponse.charAt(0) == '\01'
                    || serverResponse.charAt(0) == '\02') {
                throw new IOException(serverResponse.substring(1));
            }
        }
    }

    private File parseAndCreateDirectory(String serverResponse,
                                         File localFile) {
        StringTokenizer token = new StringTokenizer(serverResponse);
        String command = token.nextToken();
        String directoryName = token.nextToken();
        if (localFile.isDirectory()) {
            File dir = new File(localFile, directoryName);
            dir.mkdir();

            return dir;
        }
        return null;
    }

    private void parseAndFetchFile(String serverResponse,
                                   File localFile,
                                   OutputStream out,
                                   InputStream in) throws IOException {
        StringTokenizer token = new StringTokenizer(serverResponse);
        String command = token.nextToken();
        int filesize = Integer.parseInt(token.nextToken());
        String filename = token.nextToken();
        log("Receiving: " + filename + " : " + filesize);
        File transferFile = (localFile.isDirectory())
                ? new File(localFile, filename)
                : localFile;
        fetchFile(transferFile, filesize, out, in);
        waitForAck(in);
        sendAck(out);
    }

    private void fetchFile(File localFile,
                            int filesize,
                            OutputStream out,
                            InputStream in) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        sendAck(out);

        FileOutputStream fos = new FileOutputStream(localFile);
        int length;
        int totalLength = 0;
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                length = in.read(buf, 0,
                        (buf.length < filesize) ? buf.length : filesize);
                if (length < 0) {
                    throw new EOFException("Unexpected end of stream.");
                }
                fos.write(buf, 0, length);
                filesize -= length;
                totalLength += length;
                if (filesize == 0) {
                    break;
                }
            }
        } finally {
            long endTime = System.currentTimeMillis();
            logStats(startTime, endTime, totalLength);
            fos.flush();
            fos.close();
        }
    }

}

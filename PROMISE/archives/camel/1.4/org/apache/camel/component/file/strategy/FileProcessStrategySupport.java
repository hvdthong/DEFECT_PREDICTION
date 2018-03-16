package org.apache.camel.component.file.strategy;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.FileExchange;
import org.apache.camel.component.file.FileProcessStrategy;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for {@link org.apache.camel.component.file.FileProcessStrategy} implementation to extend.
 *
 * @version $Revision: 647463 $
 */
public abstract class FileProcessStrategySupport implements FileProcessStrategy {
    public static final String DEFAULT_LOCK_FILE_POSTFIX = ".cameLock";
    
    private static final transient Log LOG = LogFactory.getLog(FileProcessStrategySupport.class);
    private boolean lockFile;
    private FileRenamer lockFileRenamer;

    protected FileProcessStrategySupport() {
        this(true);
    }

    protected FileProcessStrategySupport(boolean lockFile) {
        this(lockFile, new DefaultFileRenamer(null, DEFAULT_LOCK_FILE_POSTFIX));
    }

    protected FileProcessStrategySupport(boolean lockFile, FileRenamer lockFileRenamer) {
        this.lockFile = lockFile;
        this.lockFileRenamer = lockFileRenamer;
    }

    public boolean begin(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception {
        if (isLockFile()) {
            File newFile = lockFileRenamer.renameFile(file);
            String lockFileName = newFile.getAbsolutePath();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Locking the file: " + file + " using the lock file name: " + lockFileName);
            }

            FileChannel channel = new RandomAccessFile(lockFileName, "rw").getChannel();
            FileLock lock = channel.lock();
            if (lock != null) {
                exchange.setProperty("org.apache.camel.fileChannel", channel);
                exchange.setProperty("org.apache.camel.file.lock", lock);
                exchange.setProperty("org.apache.camel.file.lock.name", lockFileName);
                return true;
            }
            return false;
        }
        return true;
    }

    public void commit(FileEndpoint endpoint, FileExchange exchange, File file) throws Exception {
        if (isLockFile()) {
            Channel channel = ExchangeHelper.getMandatoryProperty(exchange, "org.apache.camel.fileChannel", Channel.class);
            String lockfile = ExchangeHelper.getMandatoryProperty(exchange, "org.apache.camel.file.lock.name", String.class);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Unlocking file: " + file);
            }
            channel.close();
            File lock = new File(lockfile);
            lock.delete();
        }
    }

    public boolean isLockFile() {
        return lockFile;
    }

    public void setLockFile(boolean lockFile) {
        this.lockFile = lockFile;
    }

    public FileRenamer getLockFileRenamer() {
        return lockFileRenamer;
    }

    public void setLockFileRenamer(FileRenamer lockFileRenamer) {
        this.lockFileRenamer = lockFileRenamer;
    }
}

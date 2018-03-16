package org.apache.ivy.plugins.repository.url;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.ivy.plugins.repository.AbstractRepository;
import org.apache.ivy.plugins.repository.RepositoryCopyProgressListener;
import org.apache.ivy.plugins.repository.Resource;
import org.apache.ivy.plugins.repository.TransferEvent;
import org.apache.ivy.util.FileUtil;
import org.apache.ivy.util.url.ApacheURLLister;

public class URLRepository extends AbstractRepository {
    private RepositoryCopyProgressListener progress = new RepositoryCopyProgressListener(this);

    private Map resourcesCache = new HashMap();

    public Resource getResource(String source) throws IOException {
        Resource res = (Resource) resourcesCache.get(source);
        if (res == null) {
            res = new URLResource(new URL(source));
            resourcesCache.put(source, res);
        }
        return res;
    }
    
    public void get(String source, File destination) throws IOException {
        fireTransferInitiated(getResource(source), TransferEvent.REQUEST_GET);
        try {
            Resource res = getResource(source);
            long totalLength = res.getContentLength();
            if (totalLength > 0) {
                progress.setTotalLength(new Long(totalLength));
            }
            FileUtil.copy(new URL(source), destination, progress);
        } catch (IOException ex) {
            fireTransferError(ex);
            throw ex;
        } catch (RuntimeException ex) {
            fireTransferError(ex);
            throw ex;
        } finally {
            progress.setTotalLength(null);
        }
    }

    public void put(File source, String destination, boolean overwrite) throws IOException {
        if (!overwrite) {
            throw new UnsupportedOperationException(
                    "URL repository do not support append operations at the moment");
        }

        fireTransferInitiated(getResource(destination), TransferEvent.REQUEST_PUT);
        try {
            long totalLength = source.length();
            if (totalLength > 0) {
                progress.setTotalLength(new Long(totalLength));
            }
            FileUtil.copy(source, new URL(destination), progress);
        } catch (IOException ex) {
            fireTransferError(ex);
            throw ex;
        } catch (RuntimeException ex) {
            fireTransferError(ex);
            throw ex;
        } finally {
            progress.setTotalLength(null);
        }
    }

    private ApacheURLLister lister = new ApacheURLLister();

    public List list(String parent) throws IOException {
        if (parent.startsWith("http")) {
            List urls = lister.listAll(new URL(parent));
            if (urls != null) {
                List ret = new ArrayList(urls.size());
                for (ListIterator iter = urls.listIterator(); iter.hasNext();) {
                    URL url = (URL) iter.next();
                    ret.add(url.toExternalForm());
                }
                return ret;
            }
        } else if (parent.startsWith("file")) {
            String path;
            try {
                path = new URI(parent).getPath();
            } catch (URISyntaxException e) {
                IOException ioe = new IOException("Couldn't list content of '" + parent + "'");
                ioe.initCause(e);
                throw ioe;
            }
            
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                String[] files = file.list();
                List ret = new ArrayList(files.length);
                URL context = path.endsWith("/") ? new URL(parent) : new URL(parent + "/");
                for (int i = 0; i < files.length; i++) {
                    ret.add(new URL(context, files[i]).toExternalForm());
                }
                return ret;
            } else {
                return Collections.EMPTY_LIST;
            }

        }
        return null;
    }

}

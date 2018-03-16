package org.apache.synapse.config;

import org.apache.synapse.SynapseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;

/**
 * Represents an Entry contained in the local registry used by Synapse
 *
 * @see org.apache.synapse.config.SynapseConfiguration#localRegistry
 */
public class Entry {

    private static final Log log = LogFactory.getLog(Entry.class);

    /** The key of the entry */
    private String key;
    /** The type of the entry */
    private int type;
    /** Source URL of the entry if it is a URL_SRC */
    private URL src;
    /** The value of the entry. This can be either an OMElement or an String */
    private Object value;
    /** An XML to Object mapper - if one is available */
    private XMLToObjectMapper mapper;
    /** The version of the cached resource */
    private long version;
    /** The local expiry time for the cached resource */
    private long expiryTime;

    public Entry() {}
    
    public Entry(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public boolean isRemote() {
        return type == REMOTE_ENTRY;
    }

    public boolean isURLSource() {
        return type == URL_SRC;
    }

    public boolean isInlineXML() {
        return type == INLINE_XML;
    }

    public boolean isInlineText() {
        return type == INLINE_TEXT;
    }

    public void setType(int type) {
        if (type <= 4 && type >= 0)
            this.type = type;
        else
            handleException("Invalid entry type for the static entry");
    }

    public URL getSrc() {
        return src;
    }

    public void setSrc(URL src) {
        this.src = src;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets the value of the entry. String if the type is INLINE_TEXT or VALUE_TYPE,
     * OMElement otherwise.
     * @return Either an OMElement or a String
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     *
     * @return mapper
     */
    public XMLToObjectMapper getMapper() {
        return mapper;
    }

    /**
     *
     * @param mapper
     */
    public void setMapper(XMLToObjectMapper mapper) {
        this.mapper = mapper;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isExpired() {
        if(getType() == REMOTE_ENTRY) {
            return System.currentTimeMillis() > expiryTime;
        } else {
            return false;
        }
    }

    public boolean isCached() {
        return value != null;
    }

    public boolean isDynamic() {
        return type == REMOTE_ENTRY;
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }

    public static final int INLINE_TEXT = 0;
    public static final int INLINE_XML = 1;
    public static final int URL_SRC = 2;
    public static final int REMOTE_ENTRY = 3;
}

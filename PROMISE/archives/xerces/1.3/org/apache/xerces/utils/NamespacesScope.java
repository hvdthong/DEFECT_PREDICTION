package org.apache.xerces.utils;

/**
 * NamespacesScope provides a data structure for mapping namespace prefixes
 * to their URI's.  The mapping accurately reflects the scoping of namespaces
 * at a particular instant in time.
 */
public class NamespacesScope {
    /**
     * NamespacesHandler allows a client to be notified when namespace scopes change
     */
    public interface NamespacesHandler {
        /**
         * startNamespaceDeclScope is called when a new namespace scope is created
         *
         * @param prefix the StringPool handle of the namespace prefix being declared
         * @param uri the StringPool handle of the namespace's URI
         * @exception java.lang.Exception
         */
        public void startNamespaceDeclScope(int prefix, int uri) throws Exception;
        /**
         * endNamespaceDeclScope is called when a namespace scope ends
         * 
         * @param prefix the StringPool handle of the namespace prefix going out of scope
         * @exception java.lang.Exception
         */
        public void endNamespaceDeclScope(int prefix) throws Exception;
    }
    public NamespacesScope() {
        this(new NamespacesHandler() {
            public void startNamespaceDeclScope(int prefix, int uri) throws Exception {
            }
            public void endNamespaceDeclScope(int prefix) throws Exception {
            }
        });
    }
    public NamespacesScope(NamespacesHandler handler) {
        fHandler = handler;
        fNamespaceMappings[0] = new int[9];
        fNamespaceMappings[0][0] = 1;
    }
    /**
     * set the namespace URI for given prefix
     *
     * @param prefix the StringPool handler of the prefix
     * @param namespace the StringPool handle of the namespace URI
     */
    public void setNamespaceForPrefix(int prefix, int namespace) throws Exception {
        int offset = fNamespaceMappings[fElementDepth][0];
        if (offset == fNamespaceMappings[fElementDepth].length) {
            int[] newMappings = new int[offset + 8];
            System.arraycopy(fNamespaceMappings[fElementDepth], 0, newMappings, 0, offset);
            fNamespaceMappings[fElementDepth] = newMappings;
        }
        fNamespaceMappings[fElementDepth][offset++] = prefix;
        fNamespaceMappings[fElementDepth][offset++] = namespace;
        fNamespaceMappings[fElementDepth][0] = offset;
        if (fElementDepth > 0)
            fHandler.startNamespaceDeclScope(prefix, namespace);
    }
    /**
     * retreive the namespace URI for a prefix
     *
     * @param prefix the StringPool handle of the prefix
     */
    public int getNamespaceForPrefix(int prefix) {
        for (int depth = fElementDepth; depth >= 0; depth--) {
            int offset = fNamespaceMappings[depth][0];
            for (int i = 1; i < offset; i += 2) {
                if (prefix == fNamespaceMappings[depth][i]) {
                    return fNamespaceMappings[depth][i+1];
                }
            }
        }
        return -1;
    }
    /**
     *  Add a new namespace mapping
     */
    public void increaseDepth() throws Exception {
        fElementDepth++;
        if (fElementDepth == fNamespaceMappings.length) {
            int[][] newMappings = new int[fElementDepth + 8][];
            System.arraycopy(fNamespaceMappings, 0, newMappings, 0, fElementDepth);
            fNamespaceMappings = newMappings;
        }
        if (fNamespaceMappings[fElementDepth] == null)
            fNamespaceMappings[fElementDepth] = new int[9];
        fNamespaceMappings[fElementDepth][0] = 1;
    }
    /**
     *  Remove a namespace mappng
     */
    public void decreaseDepth() throws Exception {
        if (fElementDepth > 0) {
            int offset = fNamespaceMappings[fElementDepth][0];
            while (offset > 1) {
                offset -= 2;
                fHandler.endNamespaceDeclScope(fNamespaceMappings[fElementDepth][offset]);
            }
        }
        fElementDepth--;
    }
    private NamespacesHandler fHandler = null;
    private int fElementDepth = 0;
    private int[][] fNamespaceMappings = new int[8][];
}

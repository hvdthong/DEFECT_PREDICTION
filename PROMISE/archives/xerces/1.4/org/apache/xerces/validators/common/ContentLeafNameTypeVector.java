package org.apache.xerces.validators.common;

import org.apache.xerces.utils.QName;

/**
 * A light-weight class that stored the leaf names and types from a DFAContentModel
 * @version
 */

public final class ContentLeafNameTypeVector {

    public QName[] leafNames = null;
    public int[] leafTypes = null;
    public int leafCount = 0;


    public ContentLeafNameTypeVector () {
    }
    
    public ContentLeafNameTypeVector (QName[] names, int[] types, int count) {
        setValues(names, types, count);
    }

    public ContentLeafNameTypeVector (ContentLeafNameTypeVector clntv) {
        this(clntv.leafNames, clntv.leafTypes, clntv.leafCount);
    }

    public void setValues(QName[] names, int[] types, int count) {
        if (count > 0) {
            leafNames = new QName[count];
            leafTypes = new int[count];
            leafCount = count;

            System.arraycopy( names, 0, leafNames, 0, count);
            System.arraycopy( types, 0, leafTypes, 0, count);
        }
    }

}

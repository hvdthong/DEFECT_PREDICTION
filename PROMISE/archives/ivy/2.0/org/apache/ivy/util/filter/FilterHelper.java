package org.apache.ivy.util.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class FilterHelper {
    private FilterHelper() {
    }
    
    public static final Filter NO_FILTER = NoFilter.INSTANCE;

    public static Filter getArtifactTypeFilter(String types) {
        if (types == null || types.trim().equals("*")) {
            return NO_FILTER;
        }
        String[] t = types.split(",");
        List acceptedTypes = new ArrayList(t.length); 
        for (int i = 0; i < t.length; i++) {
            acceptedTypes.add(t[i].trim());
        }
        return new ArtifactTypeFilter(acceptedTypes);
    }

    /**
     * Returns a new collection containing only the items from the given collectoin, which are
     * accepted by the filter.
     * 
     * @param  col  The collection to filter.
     * @param  filter  The filter to use.
     * @return  A new collection instance containing the only the instance accepted by the filter.
     *
     * <br />
     * Comment: We could have used 
     *          facility for this. If we accepted to add dependencies on third party jars.
     */
    public static Collection filter(Collection col, Filter filter) {
        if (filter == null) {
            return col;
        }
        Collection ret = new ArrayList(col);
        for (Iterator iter = ret.iterator(); iter.hasNext();) {
            Object element = (Object) iter.next();
            if (!filter.accept(element)) {
                iter.remove();
            }
        }
        return ret;
    }
}

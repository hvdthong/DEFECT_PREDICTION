package org.apache.tools.ant.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A <CODE>ContainerMapper</CODE> that unites the results of its constituent
 * <CODE>FileNameMapper</CODE>s into a single set of result filenames.
 */
public class CompositeMapper extends ContainerMapper {

    /** {@inheritDoc}. */
    public String[] mapFileName(String sourceFileName) {
        HashSet results = new HashSet();

        FileNameMapper mapper = null;
        for (Iterator mIter = getMappers().iterator(); mIter.hasNext();) {
            mapper = (FileNameMapper) (mIter.next());
            if (mapper != null) {
                String[] mapped = mapper.mapFileName(sourceFileName);
                if (mapped != null) {
                    results.addAll(Arrays.asList(mapped));
                }
            }
        }
        return (results.size() == 0) ? null
            : (String[]) results.toArray(new String[results.size()]);
    }

}


package org.apache.tools.ant.util;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.tools.ant.types.Mapper;

/**
 * A <CODE>ContainerMapper</CODE> that chains the results of the first
 * nested <CODE>FileNameMapper</CODE>s into sourcefiles for the second,
 * the second to the third, and so on, returning the resulting mapped
 * filenames from the last nested <CODE>FileNameMapper</CODE>.
 */
public class ChainedMapper extends ContainerMapper {

    public String[] mapFileName(String sourceFileName) {
        List inputs = new ArrayList();
        List results = new ArrayList();
        results.add(sourceFileName);
        FileNameMapper mapper = null;

        for (Iterator mIter = getMappers().iterator(); mIter.hasNext();) {
            mapper = (FileNameMapper)(mIter.next());
            if (mapper != null) {
                inputs.clear();
                inputs.addAll(results);
                results.clear();

                for (Iterator it = inputs.iterator(); it.hasNext();) {
                    String[] mapped = mapper.mapFileName((String)(it.next()));
                    if (mapped != null) {
                        results.addAll(Arrays.asList(mapped));
                    }
                }
            }
        }
        return (results.size() == 0) ? null
            : (String[]) results.toArray(new String[results.size()]);
    }
}


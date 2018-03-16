package org.apache.camel.component.flatpack;

import java.util.HashMap;
import java.util.Map;

import net.sf.flatpack.DataSet;
import org.apache.camel.Converter;

/**
 * @version $Revision: 1.1 $
 */
@Converter
public final class FlatpackConverter {
    private FlatpackConverter() {
    }
    @Converter
    public static Map toMap(DataSet dataSet) {
        Map<String, Object> map = new HashMap<String, Object>();
        putValues(map, dataSet);
        return map;
    }

    /**
     * Puts the values of the dataset into the map
     * @param map
     * @param dataSet
     */
    public static void putValues(Map<String, Object> map, DataSet dataSet) {
        String[] columns = dataSet.getColumns();
        for (String column : columns) {
            String value = dataSet.getString(column);
            map.put(column, value);
        }
    }
}

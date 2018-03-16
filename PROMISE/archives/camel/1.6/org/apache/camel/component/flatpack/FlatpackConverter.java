package org.apache.camel.component.flatpack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.flatpack.DataSet;
import org.apache.camel.Converter;

/**
 * @version $Revision: 687545 $
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

    @Converter
    public static List toList(DataSet dataSet) {
        List<Map<String, Object>> answer = new ArrayList<Map<String, Object>>();
        dataSet.goTop();

        while (dataSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            putValues(map, dataSet);
            answer.add(map);
        }

        return answer;
    }

    /**
     * Puts the values of the dataset into the map
     */
    public static void putValues(Map<String, Object> map, DataSet dataSet) {
        boolean header = dataSet.isRecordID(FlatpackComponent.HEADER_ID);
        boolean trailer = dataSet.isRecordID(FlatpackComponent.TRAILER_ID);

        String[] columns;
        if (header) {
            columns = dataSet.getColumns(FlatpackComponent.HEADER_ID);
        } else if (trailer) {
            columns = dataSet.getColumns(FlatpackComponent.TRAILER_ID);
        } else {
            columns = dataSet.getColumns();
        }

        for (String column : columns) {
            String value = dataSet.getString(column);
            map.put(column, value);
        }
    }

}

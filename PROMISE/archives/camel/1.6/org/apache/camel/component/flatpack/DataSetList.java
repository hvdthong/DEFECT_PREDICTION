package org.apache.camel.component.flatpack;

import java.util.AbstractList;
import java.util.Iterator;

import net.sf.flatpack.DataSet;

/**
 * @version $Revision: 686614 $
 */
public class DataSetList extends AbstractList {
    private final DataSet dataSet;

    public DataSetList(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public Object get(int index) {
        dataSet.absolute(index);
        return FlatpackConverter.toMap(dataSet);
    }

    public int size() {
        return dataSet.getRowCount();
    }

    @Override
    public Iterator iterator() {
        dataSet.goTop();
        return new Iterator() {

            public boolean hasNext() {
                return dataSet.next();
            }

            public Object next() {
                return FlatpackConverter.toMap(dataSet);
            }

            public void remove() {
                throw new UnsupportedOperationException("remove() not supported");
            }
        };
    }
    
}

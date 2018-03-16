package org.apache.camel.component.flatpack;

import net.sf.flatpack.DataSet;

import java.util.AbstractList;
import java.util.Iterator;

/**
 * @version $Revision: 1.1 $
 */
public class DataSetList extends AbstractList {
    private final DataSet dataSet;

    public DataSetList(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public Object get(int index) {
        Iterator iter = iterator();
        for (int i = 0; iter.hasNext(); i++) {
            Object value = iter.next();
            if (i == index) {
                return value;
            }
        }
        return null;
    }

    public int size() {
        int answer = 0;
        for (Iterator iter = iterator(); iter.hasNext(); ) {
            iter.next();
            answer++;
        }
        return answer;
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

package org.apache.camel.dataformat.csv;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.csv.writer.CSVConfig;
import org.apache.commons.csv.writer.CSVField;
import org.apache.commons.csv.writer.CSVWriter;

/**
 * @version $Revision: 673468 $
 */
public class CsvDataFormat implements DataFormat {
    private CSVStrategy strategy = CSVStrategy.DEFAULT_STRATEGY;
    private CSVConfig config = new CSVConfig();

    public void marshal(Exchange exchange, Object object, OutputStream outputStream) throws Exception {
        Map map = ExchangeHelper.convertToMandatoryType(exchange, Map.class, object);
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        try {
            CSVConfig conf = getConfig();
            Set set = map.keySet();
            for (Object value : set) {
                if (value != null) {
                    String text = value.toString();
                    CSVField field = new CSVField(text);
                    conf.addField(field);
                }
            }
            CSVWriter writer = new CSVWriter(conf);
            writer.setWriter(out);
            writer.writeRecord(map);
        } finally {
            out.close();
        }
    }

    public Object unmarshal(Exchange exchange, InputStream inputStream) throws Exception {
        InputStreamReader in = new InputStreamReader(inputStream);
        try {
            CSVParser parser = new CSVParser(in, getStrategy());
            List<List<String>> list = new ArrayList<List<String>>();
            while (true) {
                String[] strings = parser.getLine();
                if (strings == null) {
                    break;
                }
                List<String> line = Arrays.asList(strings);
                list.add(line);
            }
            if (list.size() == 1) {
                return list.get(0);
            } else {
                return list;
            }
        } finally {
            in.close();
        }
    }

    public CSVConfig getConfig() {
        if (config == null) {
            config = createConfig();
        }
        return config;
    }

    public void setConfig(CSVConfig config) {
        this.config = config;
    }

    public CSVStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(CSVStrategy strategy) {
        this.strategy = strategy;
    }

    protected CSVConfig createConfig() {
        return new CSVConfig();
    }
}

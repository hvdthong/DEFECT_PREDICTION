package org.apache.camel.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.camel.Exchange;
import org.apache.camel.converter.IOConverter;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;

public class ZipDataFormat implements DataFormat {

    private int compressionLevel;

    public ZipDataFormat() {
        this.compressionLevel = Deflater.BEST_SPEED;
    }

    public ZipDataFormat(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public void marshal(Exchange exchange, Object graph, OutputStream stream)
        throws Exception {

        InputStream is = exchange.getContext().getTypeConverter().convertTo(InputStream.class, graph);
        if (is == null) {
            throw new IllegalArgumentException("Cannot get the inputstream for ZipDataFormat mashalling");
        }

        DeflaterOutputStream zipOutput = new DeflaterOutputStream(stream, new Deflater(compressionLevel));
        try {
            IOConverter.copy(is, zipOutput);
        } finally {
            zipOutput.close();
        }
    }

    public Object unmarshal(Exchange exchange, InputStream stream)
        throws Exception {

        InputStream is = ExchangeHelper.getMandatoryInBody(exchange, InputStream.class);
        InflaterInputStream unzipInput = new InflaterInputStream(is);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOConverter.copy(unzipInput, bos);
        return bos.toByteArray();
    }

}

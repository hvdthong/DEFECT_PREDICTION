package org.apache.camel.component.mina;

import java.nio.charset.Charset;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

/**
 * Text line codec that supports setting charset and delimiter.
 * <p/>
 * Uses Mina's default TextLineEncoder and TextLineDncoder. 
 */
public class TextLineCodecFactory implements ProtocolCodecFactory {

    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;

    public TextLineCodecFactory(Charset charset, LineDelimiter delimiter) {
        if (delimiter.equals(LineDelimiter.AUTO)) {
            encoder = new TextLineEncoder(charset);
        } else {
            encoder = new TextLineEncoder(charset, delimiter);
        }
        decoder = new TextLineDecoder(charset, delimiter);
    }

    public ProtocolEncoder getEncoder() throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder() throws Exception {
        return decoder;
    }

}

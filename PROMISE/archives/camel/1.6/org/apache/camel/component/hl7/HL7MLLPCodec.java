package org.apache.camel.component.hl7;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import ca.uhn.hl7v2.model.Message;

import org.apache.camel.dataformat.hl7.HL7Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * HL7 MLLP codec.
 * <p/>
 * This codec supports encoding/decoding the HL7 MLLP protocol.
 * It will use the default markers for start and end combination:
 * <ul>
 *   <li>0x0b (11 decimal) = start marker</li>
 *   <li>0x0d (13 decimal = the \r char) = segment terminators</li>
 *   <li>0x1c (28 decimal) = end 1 marker</li>
 *   <li>0x0d (13 decimal) = end 2 marker</li>
 * </ul>
 * <p/>
 * The decoder is used for decoding from MLLP (bytes) to String. The String will not contain any of
 * the start and end markers.
 * <p/>
 * The encoder is used for encoding from String to MLLP (bytes). The String should <b>not</b> contain
 * any of the start and end markers, the enoder will add these, and stream the string as bytes.
 * Also the enocder will convert any <tt>\n</tt> (line breaks) as segment terminators to <tt>\r</tt>.
 * <p/>
 * This codes supports charset encoding/decoding between bytes and String. The JVM platform default charset
 * is used, but the charset can be configued on this codec using the setter method.
 * The decoder will use the JVM platform default charset for decoding, but the charset can be configued on the this codec.
 */
public class HL7MLLPCodec implements ProtocolCodecFactory {

    private static final transient Log LOG = LogFactory.getLog(HL7MLLPCodec.class);

    private static final String CHARSET_ENCODER = HL7MLLPCodec.class.getName() + ".charsetencoder";
    private static final String CHARSET_DECODER = HL7MLLPCodec.class.getName() + ".charsetdecoder";


    private Charset charset = Charset.defaultCharset();
    private boolean convertLFtoCR = true;

    public ProtocolEncoder getEncoder() throws Exception {
        return new ProtocolEncoder() {
            public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
                throws Exception {

                if (message == null) {
                    throw new IllegalArgumentException("Message to encode is null");
                } else if (message instanceof Exception) {
                    throw (Exception) message;
                }

                CharsetEncoder encoder = (CharsetEncoder)session.getAttribute(CHARSET_ENCODER);
                if (encoder == null) {
                    encoder = charset.newEncoder();
                    session.setAttribute(CHARSET_ENCODER, encoder);
                }

                String body;
                if (message instanceof Message) {
                    body = HL7Converter.toString((Message)message);
                } else if (message instanceof String) {
                    body = (String)message;
                } else if (message instanceof byte[]) {
                    body = new String((byte[])message);
                } else {
                    throw new IllegalArgumentException("The message to encode is not a supported type: "
                            + message.getClass().getCanonicalName());
                }

                if (convertLFtoCR) {
                    body = body.replace('\n', '\r');
                }

                ByteBuffer bb = ByteBuffer.allocate(body.length() + 3).setAutoExpand(true);
                bb.put((byte) startByte);
                bb.putString(body, encoder);
                bb.put((byte) endByte1);
                bb.put((byte) endByte2);

                bb.flip();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Encoding HL7 from " + message.getClass().getCanonicalName() + " to byte stream");
                }
                out.write(bb);
            }

            public void dispose(IoSession session) throws Exception {
                session.removeAttribute(CHARSET_ENCODER);
            }
        };
    }

    public ProtocolDecoder getDecoder() throws Exception {
        return new ProtocolDecoder() {
            public void decode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {

                int posEnd = 0;
                int posStart = 0;
                while (in.hasRemaining()) {
                    byte b = in.get();
                    if (b == startByte) {
                        posStart = in.position();
                    }
                    if (b == endByte1) {
                        byte next = in.get();
                        if (next == endByte2) {
                            break;
                        } else {
                            LOG.warn("The 2nd end byte " + endByte2 + " was not found, but was " + b);
                        }
                    }
                }

                in.rewind();

                in.skip(posStart);
                if (posEnd > 0) {
                    in.limit(posEnd);
                }

                try {
                    CharsetDecoder decoder = (CharsetDecoder)session.getAttribute(CHARSET_DECODER);
                    if (decoder == null) {
                        decoder = charset.newDecoder();
                        session.setAttribute(CHARSET_DECODER, decoder);
                    }
                    String body = in.getString(decoder);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Decoding HL7 from byte stream to String");
                    }
                    out.write(body);
                } finally {
                    in.clear();
                }
            }

            public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
            }

            public void dispose(IoSession session) throws Exception {
                session.removeAttribute(CHARSET_DECODER);
            }
        };
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setCharset(String charsetName) {
        this.charset = Charset.forName(charsetName);
    }

    public boolean isConvertLFtoCR() {
        return convertLFtoCR;
    }

    public void setConvertLFtoCR(boolean convertLFtoCR) {
        this.convertLFtoCR = convertLFtoCR;
    }

    public char getStartByte() {
        return startByte;
    }

    public void setStartByte(char startByte) {
        this.startByte = startByte;
    }

    public char getEndByte1() {
        return endByte1;
    }

    public void setEndByte1(char endByte1) {
        this.endByte1 = endByte1;
    }

    public char getEndByte2() {
        return endByte2;
    }

    public void setEndByte2(char endByte2) {
        this.endByte2 = endByte2;
    }
}

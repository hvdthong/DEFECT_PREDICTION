package org.apache.camel.component.log;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.converter.stream.StreamCache;
import org.apache.camel.processor.interceptor.ExchangeFormatter;
import org.apache.camel.util.ObjectHelper;

/**
 * Log formatter to format the logging output.
 */
public class LogFormatter implements ExchangeFormatter {

    private boolean showExchangeId;
    private boolean showProperties;
    private boolean showHeaders;
    private boolean showBodyType = true;
    private boolean showBody = true;
    private boolean showOut;
    private boolean showAll;
    private boolean multiline;

    public Object format(Exchange exchange) {
        Message in = exchange.getIn();

        StringBuilder sb = new StringBuilder("");
        if (showAll || showExchangeId) {
            if (multiline) {
                sb.append('\n');
            }
            sb.append(", Id:").append(exchange.getExchangeId());
        }
        if (showAll || showProperties) {
            if (multiline) {
                sb.append('\n');
            }
            sb.append(", Properties:").append(exchange.getProperties());
        }
        if (showAll || showHeaders) {
            if (multiline) {
                sb.append('\n');
            }
            sb.append(", Headers:").append(in.getHeaders());
        }
        if (showAll || showBodyType) {
            if (multiline) {
                sb.append('\n');
            }
            sb.append(", BodyType:").append(getBodyTypeAsString(in));
        }
        if (showAll || showBody) {
            if (multiline) {
                sb.append('\n');
            }
            sb.append(", Body:").append(getBodyAsString(in));
        }

        Message out = exchange.getOut(false);
        if (showAll || showOut) {
            if (out != null) {
                if (showAll || showHeaders) {
                    if (multiline) {
                        sb.append('\n');
                    }
                    sb.append(", OutHeaders:").append(out.getHeaders());
                }
                if (showAll || showBodyType) {
                    if (multiline) {
                        sb.append('\n');
                    }
                    sb.append(", OutBodyType:").append(getBodyTypeAsString(out));
                }
                if (showAll || showBody) {
                    if (multiline) {
                        sb.append('\n');
                    }
                    sb.append(", OutBody:").append(getBodyAsString(out));
                }
            } else {
                if (multiline) {
                    sb.append('\n');
                }
                sb.append(", Out: null");
            }
        }

        return "Exchange[" + (multiline ? sb.append(']').toString() : sb.toString().substring(2) + "]");
    }

    public boolean isShowExchangeId() {
        return showExchangeId;
    }

    public void setShowExchangeId(boolean showExchangeId) {
        this.showExchangeId = showExchangeId;
    }

    public boolean isShowProperties() {
        return showProperties;
    }

    public void setShowProperties(boolean showProperties) {
        this.showProperties = showProperties;
    }

    public boolean isShowHeaders() {
        return showHeaders;
    }

    public void setShowHeaders(boolean showHeaders) {
        this.showHeaders = showHeaders;
    }

    public boolean isShowBodyType() {
        return showBodyType;
    }

    public void setShowBodyType(boolean showBodyType) {
        this.showBodyType = showBodyType;
    }

    public boolean isShowBody() {
        return showBody;
    }

    public void setShowBody(boolean showBody) {
        this.showBody = showBody;
    }

    public boolean isShowOut() {
        return showOut;
    }

    public void setShowOut(boolean showOut) {
        this.showOut = showOut;
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public boolean isMultiline() {
        return multiline;
    }

    /**
     * If enabled then each information is outputted on a newline.
     */
    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    protected Object getBodyAsString(Message message) {
        StreamCache newBody = null;
        try {
            newBody = message.getBody(StreamCache.class);
            if (newBody != null) {
                message.setBody(newBody);
            }
        } catch (NoTypeConversionAvailableException ex) {
        }
        Object answer = null;
        try {
            answer = message.getBody(String.class);
        } catch (NoTypeConversionAvailableException ex) {
            answer = message.getBody();
        }

        if (newBody != null) {
            newBody.reset();
        }
        return answer;
    }

    protected Object getBodyTypeAsString(Message message) {
        String answer = ObjectHelper.classCanonicalName(message.getBody());
        if (answer != null && answer.startsWith("java.lang.")) {
            return answer.substring(10);
        }
        return answer;
    }

}

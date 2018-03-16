package org.apache.camel.language.simple;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.language.IllegalSyntaxException;
import org.apache.camel.language.constant.ConstantLanguage;

/**
 * on files.
 * <p/>
 * This expression expects the headers from the {@link FileLanguage} on the <b>IN</b> message.
 *
 * @see org.apache.camel.language.simple.FileLanguage
 */
public final class FileExpressionBuilder {

    private FileExpressionBuilder() {
    }

    public static <E extends Exchange> Expression<E> fileNameExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return exchange.getIn().getHeader("CamelFileName", String.class);
            }

            @Override
            public String toString() {
                return "file:name";
            }
        };
    }

    public static <E extends Exchange> Expression<E> fileNameNoExtensionExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                String name = exchange.getIn().getHeader("CamelFileName", String.class);
                if (name.lastIndexOf(".") != -1) {
                    return name.substring(0, name.lastIndexOf('.'));
                } else {
                    return name;
                }
            }

            @Override
            public String toString() {
                return "file:name.noext";
            }
        };
    }

    public static <E extends Exchange> Expression<E> fileParentExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return exchange.getIn().getHeader("CamelFileParent", String.class);
            }

            @Override
            public String toString() {
                return "file:parent";
            }
        };
    }

    public static <E extends Exchange> Expression<E> filePathExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return exchange.getIn().getHeader("CamelFilePath", String.class);
            }

            @Override
            public String toString() {
                return "file:path";
            }
        };
    }

    public static <E extends Exchange> Expression<E> fileAbsolutePathExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return exchange.getIn().getHeader("CamelFileAbsolutePath", String.class);
            }

            @Override
            public String toString() {
                return "file:absolute.path";
            }
        };
    }

    public static <E extends Exchange> Expression<E> fileCanoicalPathExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return exchange.getIn().getHeader("CamelFileCanonicalPath", String.class);
            }

            @Override
            public String toString() {
                return "file:canonical.path";
            }
        };
    }

    public static <E extends Exchange> Expression<E> fileSizeExpression() {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                return exchange.getIn().getHeader("CamelFileLength", Long.class);
            }

            @Override
            public String toString() {
                return "file:length";
            }
        };
    }

    public static <E extends Exchange> Expression<E> dateExpression(final String command, final String pattern) {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                if ("file".equals(command)) {
                    Date date = exchange.getIn().getHeader("CamelFileLastModified", Date.class);
                    if (date != null) {
                        SimpleDateFormat df = new SimpleDateFormat(pattern);
                        return df.format(date);
                    } else {
                        return null;
                    }
                }
                return ExpressionBuilder.dateExpression(command, pattern).evaluate(exchange);
            }

            @Override
            public String toString() {
                return "date(" + command + ":" + pattern + ")";
            }
        };
    }

    public static <E extends Exchange> Expression<E> simpleExpression(final String simple) {
        return new Expression<E>() {
            public Object evaluate(E exchange) {
                try {
                    return SimpleLanguage.simple(simple).evaluate(exchange);
                } catch (IllegalSyntaxException e) {
                    return ConstantLanguage.constant(simple).evaluate(exchange);
                }
            }

            @Override
            public String toString() {
                return "simple(" + simple + ")";
            }
        };
    }

}

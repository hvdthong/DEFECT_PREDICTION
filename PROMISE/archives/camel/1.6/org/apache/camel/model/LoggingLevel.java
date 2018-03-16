package org.apache.camel.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Used to configure the logging levels
 *
 * @version $Revision: 724293 $
 */
@XmlEnum
public enum LoggingLevel {
    DEBUG, ERROR, FATAL, INFO, TRACE, WARN, OFF
}

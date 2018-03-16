package org.apache.synapse.config.xml;

import org.apache.synapse.SynapseConstants;
import javax.xml.namespace.QName;

/**
 * Constants used in the processing of XML configuration language
 */
public class XMLConfigConstants {

    public static final String SYNAPSE_NAMESPACE = SynapseConstants.SYNAPSE_NAMESPACE;

    /** The scope name for synapse message context properties */
    public static final String SCOPE_DEFAULT = "default";
    /** The scope name for axis2 message context properties */
    public static final String SCOPE_AXIS2 = "axis2";
    /** The scope name for axis2 message context client options properties */
    public static final String SCOPE_CLIENT = "axis2-client";
    /** The scope name for transport header properties */
    public static final String SCOPE_TRANSPORT = "transport";

    /** WS-RM version 1.0*/
    public static final String SEQUENCE_VERSION_1_0 = "1.0";
    /** WS-RM version 1.1*/
    public static final String SEQUENCE_VERSION_1_1 = "1.1";

    public static final QName DEFINITIONS_ELT = new QName(SYNAPSE_NAMESPACE, "definitions");
    public static final QName SEQUENCE_ELT    = new QName(SYNAPSE_NAMESPACE, "sequence");
    public static final QName ENDPOINT_ELT    = new QName(SYNAPSE_NAMESPACE, "endpoint");
    public static final QName ENTRY_ELT       = new QName(SYNAPSE_NAMESPACE, "localEntry");
    public static final QName REGISTRY_ELT    = new QName(SYNAPSE_NAMESPACE, "registry");
    public static final QName TASK_ELT        = new QName(SYNAPSE_NAMESPACE, "task");
    public static final QName PROXY_ELT       = new QName(SYNAPSE_NAMESPACE, "proxy");
    public static final String NULL_NAMESPACE = "";
    public static final Object QUARTZ_QNAME   =

	/** The Trace attribute name, for proxy services, sequences */
	public static final String TRACE_ATTRIB_NAME = "trace";
	/** The Trace value 'enable' */
	public static final String TRACE_ENABLE = "enable";
	/** The Trace value 'disable' */
	public static final String TRACE_DISABLE = "disable";

	/** The statistics attribute name */
	public static final String STATISTICS_ATTRIB_NAME = "statistics";
	/** The statistics value 'enable' */
	public static final String STATISTICS_ENABLE = "enable";
	/** The statistics value 'disable' */
	public static final String STATISTICS_DISABLE = "disable";

	public static final String SUSPEND_DURATION_ON_FAILURE = "suspendDurationOnFailure";
	public static final String ALGORITHM_NAME = "policy";

    public static final String ONREJECT = "onReject";
	public static final String ONACCEPT = "onAccept";
}

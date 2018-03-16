import java.util.Iterator;

import javax.activation.DataHandler;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP11Version;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPVersion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.util.SimpleMap;

public class PayloadHelper {
	

	public final static QName BINARYELT = new QName(AXIOMPAYLOADNS, "binary",
			"ax");

	public final static QName TEXTELT = new QName(AXIOMPAYLOADNS, "text", "ax");

	public final static QName MAPELT = new QName(AXIOMPAYLOADNS, "map", "ax");

	public final static int XMLPAYLOADTYPE = 0, BINARYPAYLOADTYPE = 1,
			TEXTPAYLOADTYPE = 2, MAPPAYLOADTYPE = 3;

	public static final Log log = LogFactory.getLog(PayloadHelper.class);

	public static int getPayloadType(SOAPEnvelope envelope) {
		OMElement el = getXMLPayload(envelope);
		if (el.getQName().equals(BINARYELT))
			return BINARYPAYLOADTYPE;
		else if (el.getQName().equals(TEXTELT))
			return TEXTPAYLOADTYPE;
		else if (el.getQName().equals(MAPELT))
			return MAPPAYLOADTYPE;
		else
	}

	public static int getPayloadType(MessageContext mc) {
		if (mc.getEnvelope() == null)
			return 0;
		return getPayloadType(mc.getEnvelope());
	}

	public static OMElement getXMLPayload(SOAPEnvelope envelope) {
		SOAPBody body = envelope.getBody();
		if (body == null) {
			log.error("No body found");
			return null;
		}
		OMElement bodyEl = body.getFirstElement();
		if (bodyEl == null) {
			log.error("No body child found");
			return null;
		}
		return bodyEl;
	}

	public static void setXMLPayload(SOAPEnvelope envelope, OMElement element) {
		SOAPBody body = envelope.getBody();
		if (body == null) {

			SOAPVersion version = envelope.getVersion();
			if (version.getEnvelopeURI().equals(
					SOAP11Version.SOAP_ENVELOPE_NAMESPACE_URI)) {
				body = OMAbstractFactory.getSOAP11Factory().createSOAPBody();
			} else {
				body = OMAbstractFactory.getSOAP12Factory().createSOAPBody();
			}
			if (envelope.getHeader() != null) {
				envelope.getHeader().insertSiblingAfter(body);
			} else {
				envelope.addChild(body);
			}
		} else {
			for (Iterator it = body.getChildren(); it.hasNext();) {
				OMNode node = (OMNode) it.next();
				node.discard();
			}
		}
		body.addChild(element);
	}

	public static void setXMLPayload(MessageContext mc, OMElement element) {
		if (mc.getEnvelope() == null) {
			try {
				mc.setEnvelope(OMAbstractFactory.getSOAP12Factory()
						.createSOAPEnvelope());
			} catch (Exception e) {
				throw new SynapseException(e);
			}
		}
		setXMLPayload(mc.getEnvelope(), element);
	}

	public static DataHandler getBinaryPayload(SOAPEnvelope envelope) {
		OMElement el = getXMLPayload(envelope);
		if (el == null)
			return null;
		if (!el.getQName().equals(BINARYELT)) {
			log.error("Wrong QName" + el.getQName());
			return null;
		}
		OMNode textNode = el.getFirstOMChild();
		if (textNode.getType() != OMNode.TEXT_NODE) {
			log.error("Text Node not found");
			return null;
		}
		OMText text = (OMText) textNode;
		DataHandler dh = null;
		try {
			dh = (DataHandler) text.getDataHandler();
		} catch (ClassCastException ce) {
			log.error("cannot get DataHandler" + ce.getMessage());
			return null;
		}
		return dh;

	}

	public static DataHandler getBinaryPayload(MessageContext mc) {
		if (mc.getEnvelope() == null) {
			log.error("null envelope");
			return null;
		}
		return getBinaryPayload(mc.getEnvelope());
	}

	public static void setBinaryPayload(SOAPEnvelope envelope, DataHandler dh) {
		OMFactory fac = envelope.getOMFactory();
		OMElement binaryElt = envelope.getOMFactory()
				.createOMElement(BINARYELT);
		OMText text = fac.createOMText(dh, true);
		binaryElt.addChild(text);
		setXMLPayload(envelope, binaryElt);
	}

	public static void setBinaryPayload(MessageContext mc, DataHandler dh) {
		if (mc.getEnvelope() == null) {
			try {
				mc.setEnvelope(OMAbstractFactory.getSOAP12Factory()
						.createSOAPEnvelope());
			} catch (Exception e) {
				throw new SynapseException(e);
			}
		}
		setBinaryPayload(mc.getEnvelope(), dh);

	}

	public static String getTextPayload(SOAPEnvelope envelope) {
		OMElement el = getXMLPayload(envelope);
		if (el == null)
			return null;
		if (!el.getQName().equals(TEXTELT)) {
			log.error("Wrong QName" + el.getQName());
			return null;
		}
		OMNode textNode = el.getFirstOMChild();
		if (textNode.getType() != OMNode.TEXT_NODE) {
			log.error("Text Node not found");
			return null;
		}
		OMText text = (OMText) textNode;
		return text.getText();
	}

	public static String getTextPayload(MessageContext mc) {
		if (mc.getEnvelope() == null) {
			log.error("null envelope");
			return null;
		}
		return getTextPayload(mc.getEnvelope());
	}

	public static void setTextPayload(SOAPEnvelope envelope, String text) {
		OMFactory fac = envelope.getOMFactory();
		OMElement textElt = envelope.getOMFactory().createOMElement(TEXTELT);
		OMText textNode = fac.createOMText(text);
		textElt.addChild(textNode);
		setXMLPayload(envelope, textElt);
	}

	public static void setTextPayload(MessageContext mc, String text) {
		if (mc.getEnvelope() == null) {
			try {
				mc.setEnvelope(OMAbstractFactory.getSOAP12Factory()
						.createSOAPEnvelope());
			} catch (Exception e) {
				throw new SynapseException(e);
			}
		}
		setTextPayload(mc.getEnvelope(), text);
	}

	public static SimpleMap getMapPayload(SOAPEnvelope envelope) {
		OMElement el = getXMLPayload(envelope);
		if (el == null)
			return null;
		if (!el.getQName().equals(MAPELT)) {
			log.error("Wrong QName" + el.getQName());
			return null;
		}
		SimpleMap map = new SimpleMapImpl(el);
		return map;
	}

	public static SimpleMap getMapPayload(MessageContext mc) {
		if (mc.getEnvelope() == null) {
			log.error("null envelope");
			return null;
		}
		return getMapPayload(mc.getEnvelope());
	}

	public static void setMapPayload(SOAPEnvelope envelope, SimpleMap map) {

		if (map instanceof SimpleMapImpl) {
			SimpleMapImpl impl = (SimpleMapImpl) map;
			OMElement mapElt = impl.getOMElement(envelope.getOMFactory());
			if (mapElt == null) {
				log.debug("null map element returned");
				return;
			}
			setXMLPayload(envelope, mapElt);
		} else {
			throw new SynapseException("cannot handle any other instance of SimpleMap at this point TODO");
		}
	}

	public static void setMapPayload(MessageContext mc, SimpleMap map) {
		if (mc.getEnvelope() == null) {
			try {
				mc.setEnvelope(OMAbstractFactory.getSOAP12Factory()
						.createSOAPEnvelope());
			} catch (Exception e) {
				throw new SynapseException(e);
			}
		}
		setMapPayload(mc.getEnvelope(), map);
	}
	
	public static XMLStreamReader getStAXPayload(SOAPEnvelope envelope) {
		 
		OMElement el = getXMLPayload(envelope);
		if (el==null) {
			return null;
		}
		return el.getXMLStreamReader();
	}
	public static XMLStreamReader getStAXPayload(MessageContext mc) {
		if (mc.getEnvelope() == null) {
			log.error("null envelope");
			return null;
		}
		return getStAXPayload(mc.getEnvelope());
	}
	public static void setStAXPayload(SOAPEnvelope envelope, XMLStreamReader streamReader) {
		StAXOMBuilder builder = new StAXOMBuilder(envelope.getOMFactory(), streamReader);
		OMElement el = builder.getDocumentElement();
		setXMLPayload(envelope, el);
	}
	public static void setStAXPayload(MessageContext mc, XMLStreamReader streamReader) {
		if (mc.getEnvelope() == null) {
			try {
				mc.setEnvelope(OMAbstractFactory.getSOAP12Factory()
						.createSOAPEnvelope());
			} catch (Exception e) {
				throw new SynapseException(e);
			}
			setStAXPayload(mc.getEnvelope(), streamReader);
		}
	
	}
	
	
}

package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.XSLTMediatorFactory;
import org.apache.synapse.config.xml.ValidateMediatorFactory;
import org.apache.synapse.config.XMLToObjectMapper;
import sun.misc.Service;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * 
 * This class is based on J2SE Service Provider model
 */

public  class MediatorFactoryFinder implements XMLToObjectMapper {

	private static final Log log = LogFactory.getLog(MediatorFactoryFinder.class);

	private static final Class[] mediatorFactories = {
        SequenceMediatorFactory.class,
        LogMediatorFactory.class,
        SendMediatorFactory.class,         
        FilterMediatorFactory.class,
        SynapseMediatorFactory.class,
        DropMediatorFactory.class,
        HeaderMediatorFactory.class,
        FaultMediatorFactory.class,
        PropertyMediatorFactory.class,
        SwitchMediatorFactory.class,
        InMediatorFactory.class,
        OutMediatorFactory.class,
        RMSequenceMediatorFactory.class,          
        ClassMediatorFactory.class,
        ValidateMediatorFactory.class,
        XSLTMediatorFactory.class
    };

    private static MediatorFactoryFinder instance = null;

    /**
     * A map of mediator QNames to implementation class
     */
    private static Map factoryMap = new HashMap();

    public static synchronized MediatorFactoryFinder getInstance() {
        if (instance == null) {
            instance = new MediatorFactoryFinder();
        }
        return instance;
    }

    /**
     * Force re initialization next time
     */
    public synchronized void reset() {
        factoryMap.clear();
        instance = null;
    }

    private MediatorFactoryFinder() {

        factoryMap = new HashMap();

        for (int i = 0; i < mediatorFactories.length; i++) {
			Class c = mediatorFactories[i];
			try {
                MediatorFactory fac = (MediatorFactory) c.newInstance();
                factoryMap.put(fac.getTagQName(), c);
            } catch (Exception e) {
				throw new SynapseException("Error instantiating " + c.getName(), e);
			}
		}
        registerExtensions();
    }

    private void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new SynapseException(msg, e);
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }

    /**
     * Register pluggable mediator factories from the classpath
     *
     * This looks for JAR files containing a META-INF/services that adheres to the following
     */
    private void registerExtensions() {


        Iterator it = Service.providers(MediatorFactory.class);
        while (it.hasNext()) {
            MediatorFactory mf = (MediatorFactory) it.next();
            QName tag = mf.getTagQName();
            factoryMap.put(tag, mf.getClass());
            log.debug("Added MediatorFactory " + mf.getClass() + " to handle " + tag);
        }
    }

    /**
	 * This method returns a Processor given an OMElement. This will be used
	 * recursively by the elements which contain processor elements themselves
	 * (e.g. rules)
	 * 
	 * @param element
     * @return Processor
	 */
	public Mediator getMediator(OMElement element) {

        String localName = element.getLocalName();
        QName qName = null;
        if (element.getNamespace() != null) {
            qName = new QName(element.getNamespace().getNamespaceURI(), localName);
        } else {
            qName = new QName(localName);
        }
        log.debug("getMediator(" + qName + ")");
        Class cls = (Class) factoryMap.get(qName);

        if (cls == null && localName.indexOf('.') > -1) {
            String newLocalName = localName.substring(0, localName.indexOf('.'));
            qName = new QName(element.getNamespace().getNamespaceURI(), newLocalName);
            log.debug("getMediator.2(" + qName + ")");
            cls = (Class) factoryMap.get(qName);
        }

        if (cls == null) {
            String msg = "Unknown mediator referenced by configuration element : " + qName;
            log.error(msg);
            throw new SynapseException(msg);
        }

        try {
			MediatorFactory mf = (MediatorFactory) cls.newInstance();
			return mf.createMediator(element);

        } catch (InstantiationException e) {
            String msg = "Error initializing mediator factory : " + cls;
            log.error(msg);
            throw new SynapseException(msg, e);

        } catch (IllegalAccessException e) {
            String msg = "Error initializing mediator factory : " + cls;
            log.error(msg);
            throw new SynapseException(msg, e);
		}
	}
    /*
    This method exposes all the MediatorFactories and its Extensions 
    */
    public Map getFactoryMap() {
        return factoryMap;
    }

    /**
     * Allow the mediator factory finder to act as an XMLToObjectMapper for Mediators
     * (i.e. Sequence Mediator) loaded dynamically from a Registry 
     * @param om
     * @return
     */
    public Object getObjectFromOMNode(OMNode om) {
        if (om instanceof OMElement) {
            return getMediator((OMElement) om);
        } else {
            handleException("Invalid mediator configuration XML : " + om);
        }
        return null;
    }
}

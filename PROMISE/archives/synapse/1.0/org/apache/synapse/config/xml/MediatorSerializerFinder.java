package org.apache.synapse.config.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.ValidateMediatorSerializer;
import org.apache.synapse.config.xml.XSLTMediatorSerializer;
import sun.misc.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MediatorSerializerFinder {

    private static final Log log = LogFactory.getLog(MediatorSerializerFinder.class);

    private static final Class[] mediatorSerializers = {
        SequenceMediatorSerializer.class,
        LogMediatorSerializer.class,
        SendMediatorSerializer.class,
        FilterMediatorSerializer.class,
        SynapseMediatorSerializer.class,
        DropMediatorSerializer.class,
        HeaderMediatorSerializer.class,
        FaultMediatorSerializer.class,
        PropertyMediatorSerializer.class,
        SwitchMediatorSerializer.class,
        InMediatorSerializer.class,
        OutMediatorSerializer.class,
        RMSequenceMediatorSerializer.class,     
        ClassMediatorSerializer.class,
        ValidateMediatorSerializer.class,
        XSLTMediatorSerializer.class        
    };

    private static MediatorSerializerFinder instance = null;

    /**
     * A map of mediator QNames to implementation class
     */
    private static Map serializerMap = new HashMap();

    public static synchronized MediatorSerializerFinder getInstance() {
        if (instance == null) {
            instance = new MediatorSerializerFinder();
        }
        return instance;
    }

    public MediatorSerializer getSerializer(Mediator mediator) {
        return (MediatorSerializer) serializerMap.get(mediator.getClass().getName());
    }

    private MediatorSerializerFinder() {

        serializerMap = new HashMap();

        for (int i = 0; i < mediatorSerializers.length; i++) {
            Class c = mediatorSerializers[i];
            try {
                MediatorSerializer ser = (MediatorSerializer) c.newInstance();
                serializerMap.put(ser.getMediatorClassName(), ser);
            } catch (Exception e) {
                throw new SynapseException("Error instantiating " + c.getName(), e);
            }
        }
        registerExtensions();
    }

    /**
     * Register pluggable mediator serializers from the classpath
     *
     * This looks for JAR files containing a META-INF/services that adheres to the following
     */
    private void registerExtensions() {

        log.debug("Registering mediator extensions found in the classpath : " + System.getProperty("java.class.path"));

        Iterator it = Service.providers(MediatorSerializer.class);
        while (it.hasNext()) {
            MediatorSerializer ms = (MediatorSerializer) it.next();
            String name = ms.getMediatorClassName();
            try {
                serializerMap.put(name, ms.getClass().newInstance());
            } catch (InstantiationException e) {
                handleException("Error instantiating mediator serializer : " + ms);
            } catch (IllegalAccessException e) {
                handleException("Error instantiating mediator serializer : " + ms);
            }
            log.debug("Added MediatorSerializer " + ms.getClass().getName() + " to handle " + name);
        }
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}

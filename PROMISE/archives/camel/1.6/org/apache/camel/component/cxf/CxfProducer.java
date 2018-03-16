package org.apache.camel.component.cxf;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.cxf.feature.MessageDataFormatFeature;
import org.apache.camel.component.cxf.feature.PayLoadDataFormatFeature;
import org.apache.camel.component.cxf.invoker.CxfClient;
import org.apache.camel.component.cxf.invoker.CxfClientFactoryBean;
import org.apache.camel.component.cxf.invoker.InvokingContext;
import org.apache.camel.component.cxf.invoker.InvokingContextFactory;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.component.cxf.util.CxfEndpointUtils;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ObjectHelper;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.configuration.Configurer;
import org.apache.cxf.configuration.spring.ConfigurerImpl;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.service.model.BindingOperationInfo;

/**
 * Sends messages from Camel into the CXF endpoint
 *
 * @version $Revision: 735926 $
 */
public class CxfProducer extends DefaultProducer<CxfExchange> {
    private CxfEndpoint endpoint;
    private Client client;
    private DataFormat dataFormat;

    public CxfProducer(CxfEndpoint endpoint) throws Exception {
        super(endpoint);
        this.endpoint = endpoint;
        dataFormat = CxfEndpointUtils.getDataFormat(endpoint);
        if (dataFormat.equals(DataFormat.POJO)) {
            client = createClientFromClientFactoryBean(null);
        } else {
            client = createClientForStreamMessage();
        }
    }

    private Client createClientForStreamMessage() throws Exception {
        CxfClientFactoryBean cfb = new CxfClientFactoryBean();
        Class serviceClass = null;
        try {
            serviceClass = CxfEndpointUtils.getServiceClass(endpoint);
        } catch (ClassNotFoundException e) {
            throw new CamelException(e);
        }       
        
        boolean jsr181Enabled = CxfEndpointUtils.hasWebServiceAnnotation(serviceClass);
        cfb.setJSR181Enabled(jsr181Enabled);
       
        return createClientFromClientFactoryBean(jsr181Enabled ? new JaxWsProxyFactoryBean(cfb)
            : new ClientProxyFactoryBean(cfb));
    }

    private Client createClientFromClientFactoryBean(ClientProxyFactoryBean cfb) throws Exception {
        Bus bus = null;
        if (endpoint.getApplicationContext() != null) {            
            if (endpoint.getCxfEndpointBean() != null) {
                bus = endpoint.getCxfEndpointBean().getBus();                
            } else {
                SpringBusFactory busFactory = new SpringBusFactory(endpoint.getApplicationContext());
                bus = busFactory.createBus();
            }
            if (CxfEndpointUtils.getSetDefaultBus(endpoint)) {
                BusFactory.setDefaultBus(bus);
            }
        } else {
            bus = BusFactory.getDefaultBus();
        }
        
        Class serviceClass = CxfEndpointUtils.getServiceClass(endpoint);
        if (cfb == null) {
            cfb = CxfEndpointUtils.getClientFactoryBean(serviceClass);
        }
        
        if (endpoint.isSpringContextEndpoint()) {            
            endpoint.configure(cfb);
            cfb.setAddress(endpoint.getAddress());
            if (null != endpoint.getServiceClass()) {
                cfb.setServiceClass(ObjectHelper.loadClass(endpoint.getServiceClass()));
            }
            if (null != endpoint.getWsdlURL()) {
                cfb.setWsdlURL(endpoint.getWsdlURL());
            }

            if (endpoint.getWsdlURL() != null) {
                cfb.setWsdlURL(endpoint.getWsdlURL());
            }
        }
        cfb.setServiceClass(serviceClass);
        
        if (CxfEndpointUtils.getServiceName(endpoint) != null) {
            cfb.setServiceName(CxfEndpointUtils.getServiceName(endpoint));
        }
        if (CxfEndpointUtils.getPortName(endpoint) != null) {
            cfb.setEndpointName(CxfEndpointUtils.getPortName(endpoint));
        }
        
        if (dataFormat.equals(DataFormat.MESSAGE)) {
            cfb.getFeatures().add(new MessageDataFormatFeature());
        } else if (dataFormat.equals(DataFormat.PAYLOAD)) {
            cfb.getFeatures().add(new PayLoadDataFormatFeature());
        }
        
        cfb.setBus(bus);
        return ((ClientProxy)Proxy.getInvocationHandler(cfb.create())).getClient();
    }

    public void process(Exchange exchange) throws Exception {
        CxfExchange cxfExchange = endpoint.createExchange(exchange);
        process(cxfExchange);
        exchange.copyFrom(cxfExchange);

    }

    public void process(CxfExchange exchange) throws Exception {
        Message inMessage = CxfBinding.createCxfMessage(endpoint.getHeaderFilterStrategy(), exchange);
        exchange.setProperty(CxfExchange.DATA_FORMAT, dataFormat);

        if (dataFormat.equals(DataFormat.POJO)) {
            List parameters = inMessage.getContent(List.class);
            if (parameters == null) {
                parameters = new ArrayList();
            }
            String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
            String operationNameSpace = exchange.getIn().getHeader(CxfConstants.OPERATION_NAMESPACE,
                                                                   String.class);
            Map<String, Object> context = new HashMap<String, Object>();
            Map<String, Object> responseContext = CxfBinding.propogateContext(inMessage, context);
            Message response = new MessageImpl();
            if (operationName != null) {
                Object[] result = null;
                result = invokeClient(operationNameSpace, operationName, parameters, context);
                if (result != null) {
                    response.setContent(List.class, new MessageContentsList(result));
                } else {
                    response.setContent(List.class, new MessageContentsList());
                }
                CxfBinding.storeCXfResponseContext(response, responseContext);
                CxfBinding.storeCxfResponse(endpoint.getHeaderFilterStrategy(), exchange, response);

            } else {
                throw new RuntimeCamelException("Can't find the operation name in the message!");
            }
        } else {
            org.apache.cxf.message.Exchange ex = exchange.getExchange();
            if (ex == null) {
                ex = (org.apache.cxf.message.Exchange)exchange.getProperty(CxfConstants.CXF_EXCHANGE);
                exchange.setExchange(ex);
            }
            if (ex == null) {
                ex = new ExchangeImpl();
                exchange.setExchange(ex);
            }
            ObjectHelper.notNull(ex, "exchange");
            InvokingContext invokingContext = ex.get(InvokingContext.class);
            if (invokingContext == null) {
                invokingContext = InvokingContextFactory.createContext(dataFormat);
                ex.put(InvokingContext.class, invokingContext);
            }
            Map<Class, Object> params = invokingContext.getRequestContent(inMessage);
            CxfClient cxfClient = (CxfClient)client;
            BindingOperationInfo boi = ex.get(BindingOperationInfo.class);
            Message response = null;
            if (boi == null) {
                response = new MessageImpl();
            } else {
                Endpoint ep = ex.get(Endpoint.class);
                response = ep.getBinding().createMessage();
            }
            response.setExchange(ex);
            Map<String, Object> context = new HashMap<String, Object>();
            Map<String, Object> responseContext = CxfBinding.propogateContext(inMessage, context);

            Object result = cxfClient.dispatch(params, context, ex);
            ex.setOutMessage(response);
            invokingContext.setResponseContent(response, result);
            CxfBinding.storeCXfResponseContext(response, responseContext);
            CxfBinding.storeCxfResponse(endpoint.getHeaderFilterStrategy(), exchange, response);


        }

    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }

    private Object[] invokeClient(String operationNameSpace, String operationName, List parameters,
                                  Map<String, Object> context) throws Exception {

        QName operationQName = null;
        if (operationNameSpace == null) {
            operationQName = new QName(client.getEndpoint().getService().getName().getNamespaceURI(),
                                       operationName);
        } else {
            operationQName = new QName(operationNameSpace, operationName);
        }
        BindingOperationInfo op = client.getEndpoint().getEndpointInfo().getBinding()
            .getOperation(operationQName);
        if (op == null) {
            throw new RuntimeCamelException("No operation found in the CXF client, the operation is "
                                            + operationQName);
        }
        if (!endpoint.isWrapped()) {
            if (op.isUnwrappedCapable()) {
                op = op.getUnwrappedOperation();
            }
        }
        Object[] result = client.invoke(op, parameters.toArray(), context);

        return result;
    }
    
    public Client getClient() {
        return client;
    }

}

package org.apache.camel.component.cxf.util;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceProvider;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelException;
import org.apache.camel.component.cxf.CxfConstants;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.util.ObjectHelper;
import org.apache.cxf.Bus;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.ClassHelper;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.factory.AbstractServiceFactoryBean;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.wsdl11.WSDLServiceFactory;


public final class CxfEndpointUtils {
    public static final String PROP_NAME_PORT = "port";
    public static final String PROP_NAME_SERVICE = "service";
    public static final String PROP_NAME_SERVICECLASS = "serviceClass";
    public static final String PROP_NAME_DATAFORMAT = "dataFormat";
    public static final String DATAFORMAT_POJO = "pojo";
    public static final String DATAFORMAT_MESSAGE = "message";
    public static final String DATAFORMAT_PAYLOAD = "payload";
    private static final Logger LOG = LogUtils.getL7dLogger(CxfEndpointUtils.class);

    private CxfEndpointUtils() {
    }

    static QName getQName(final String name) {
        QName qName = null;
        if (name != null) {
            try {
                qName =  QName.valueOf(name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qName;
    }
    
    public static Class getServiceClass(CxfEndpoint cxfEndpoint) throws ClassNotFoundException {
        Class<?> answer = null;
        if (cxfEndpoint.isSpringContextEndpoint()) {
            answer = cxfEndpoint.getCxfEndpointBean().getServiceClass();
            if (answer != null) {
                return answer;
            }
        }
        if (cxfEndpoint.getServiceClassInstance() != null) {        
            Object bean = cxfEndpoint.getCamelContext().getRegistry().lookup(cxfEndpoint.getServiceClassInstance());
            if (bean != null) {
                answer = ClassHelper.getRealClass(bean);
            } else {
                throw new ClassNotFoundException("Can't find serviceClass instace with name" + cxfEndpoint.getServiceClassInstance() + " from CamelContext registry.");
            }
        } else {
            if (ObjectHelper.isNotEmpty(cxfEndpoint.getServiceClass())) {
                answer = ClassLoaderUtils.loadClass(cxfEndpoint.getServiceClass(), CxfEndpointUtils.class);
            } else {
                throw new ClassNotFoundException("Can't find serviceClass from uri, please check the cxf endpoint configuration");
            }
        }
        return answer;
    }

    public static QName getPortName(final CxfEndpoint endpoint) {
        if (endpoint.getPortName() != null) {
            return getQName(endpoint.getPortName());
        } else {
            String portLocalName = getCxfEndpointPropertyValue(endpoint, CxfConstants.PORT_LOCALNAME);
            String portNamespace = getCxfEndpointPropertyValue(endpoint, CxfConstants.PORT_NAMESPACE);
            if (portLocalName != null) {
                return new QName(portNamespace, portLocalName);
            } else {
                return null;
            }           
        }
    }

    public static QName getServiceName(final CxfEndpoint endpoint) {
        if (endpoint.getServiceName() != null) {
            return getQName(endpoint.getServiceName());
        } else {
            String serviceLocalName = getCxfEndpointPropertyValue(endpoint, CxfConstants.SERVICE_LOCALNAME);
            String serviceNamespace = getCxfEndpointPropertyValue(endpoint, CxfConstants.SERVICE_NAMESPACE);
            if (serviceLocalName != null) {
                return new QName(serviceNamespace, serviceLocalName);
            } else {
                return null;
            }
        }
    }

    public static EndpointInfo getEndpointInfo(final Service service, final CxfEndpoint endpoint) {
        EndpointInfo endpointInfo = null;
        final java.util.Collection<EndpointInfo> endpoints = service.getServiceInfos().get(0).getEndpoints();
        if (endpoints.size() == 1) {
            endpointInfo = endpoints.iterator().next();
        } else {
            final String port = endpoint.getPortName();
            if (port != null) {
                final QName endpointName = QName.valueOf(port);
                endpointInfo = service.getServiceInfos().get(0).getEndpoint(endpointName);
            }
        }

        return endpointInfo;
    }   

    public static boolean hasWebServiceAnnotation(Class<?> cls) {
        return hasAnnotation(cls, WebService.class) || hasAnnotation(cls, WebServiceProvider.class);
    }

    public static boolean hasAnnotation(Class<?> cls, Class<? extends Annotation> annotation) {
        if (cls == null || cls == Object.class) {
            return false;
        }

        if (null != cls.getAnnotation(annotation)) {
            return true;
        }

        for (Class<?> interfaceClass : cls.getInterfaces()) {
            if (null != interfaceClass.getAnnotation(annotation)) {
                return true;
            }
        }
        return hasAnnotation(cls.getSuperclass(), annotation);
    }


    public static ServerFactoryBean getServerFactoryBean(Class<?> cls) throws CamelException {
        ServerFactoryBean serverFactory  = null;
        try {
            if (cls == null) {
                serverFactory = new ServerFactoryBean();
                serverFactory.setServiceFactory(new WSDLSoapServiceFactoryBean());

            } else {
                boolean isJSR181SEnabled = CxfEndpointUtils.hasWebServiceAnnotation(cls);
                serverFactory = isJSR181SEnabled ? new JaxWsServerFactoryBean()
                            : new ServerFactoryBean();
            }
            return serverFactory;
        } catch (Exception e) {
            throw new CamelException(e);
        }

    }

    public static ClientProxyFactoryBean getClientFactoryBean(Class<?> cls) throws CamelException {
        ClientProxyFactoryBean clientFactory = null;
        try {
            if (cls == null) {
                clientFactory = new ClientProxyFactoryBean();
                clientFactory.setServiceFactory(new WSDLSoapServiceFactoryBean());
            } else {
                boolean isJSR181SEnabled = CxfEndpointUtils.hasWebServiceAnnotation(cls);
                clientFactory = isJSR181SEnabled ? new JaxWsProxyFactoryBean()
                        : new ClientProxyFactoryBean();
            }
            return clientFactory;
        } catch (Exception e) {
            throw new CamelException(e);
        }
    }

    public static void checkEndpiontIntegration(CxfEndpoint endpoint, Bus bus) throws CamelException {

        String wsdlLocation = endpoint.getWsdlURL();
        QName serviceQName = CxfEndpointUtils.getQName(endpoint.getServiceName());
        String serviceClassName = endpoint.getServiceClass();
        DataFormat dataFormat = CxfEndpointUtils.getDataFormat(endpoint);
        URL wsdlUrl = null;
        if (wsdlLocation != null) {
            try {
                wsdlUrl = UriUtils.getWsdlUrl(new URI(wsdlLocation));
            } catch (Exception e) {
                throw new CamelException(e);
            }
        }
        if (serviceQName == null) {
            throw new CamelException(new Message("SVC_QNAME_NOT_FOUND_X", LOG, endpoint.getServiceName()).toString());
        }

        if (serviceClassName == null && dataFormat == DataFormat.POJO) {
            throw new CamelException(new Message("SVC_CLASS_PROP_IS_REQUIRED_X", LOG).toString());
        }
        AbstractServiceFactoryBean serviceFactory = null;
        try {

            if (serviceClassName != null) {
                Class<?> cls = ClassLoaderUtils.loadClass(serviceClassName, CxfEndpointUtils.class);

                boolean isJSR181SEnabled = CxfEndpointUtils.hasWebServiceAnnotation(cls);

                serviceFactory = isJSR181SEnabled
                    ? new JaxWsServiceFactoryBean() : new ReflectionServiceFactoryBean();
                serviceFactory.setBus(bus);
                if (wsdlUrl != null) {
                    ((ReflectionServiceFactoryBean)serviceFactory).setWsdlURL(wsdlUrl);
                }
                if (serviceQName != null) {
                    ((ReflectionServiceFactoryBean)serviceFactory).setServiceName(serviceQName);
                }
                ((ReflectionServiceFactoryBean)serviceFactory).setServiceClass(cls);

            } else {
                if (wsdlUrl == null) {
                    throw new CamelException(new Message("SVC_WSDL_URL_IS_NULL_X", LOG, wsdlLocation).toString());
                }
                serviceFactory = new WSDLServiceFactory(bus, wsdlUrl, serviceQName);
            }

        } catch (ClassNotFoundException cnfe) {
            throw new CamelException(new Message("CLASS_X_NOT_FOUND ", LOG, serviceClassName).toString(), cnfe);
        } catch (Exception e) {
            throw new CamelException(e);
        }
    }

    public static boolean getSetDefaultBus(CxfEndpoint endpoint) {
        Boolean isSetDefaultBus = null;
        CxfEndpointBean cxfEndpointBean = endpoint.getCxfEndpointBean();
        if (cxfEndpointBean != null && cxfEndpointBean.getProperties() != null) {
            String value =  (String)cxfEndpointBean.getProperties().get(CxfConstants.SET_DEFAULT_BUS);
            isSetDefaultBus = Boolean.valueOf(value);
        }
        if (isSetDefaultBus != null && endpoint.isSetDefaultBus() == null) {
            return isSetDefaultBus.booleanValue();
        } else if (endpoint.isSetDefaultBus() != null) {
            return endpoint.isSetDefaultBus().booleanValue();
            return false;
        }
    }   

    
    public static String getCxfEndpointPropertyValue(CxfEndpoint endpoint, String property) {
        String result = null;
        CxfEndpointBean cxfEndpointBean = endpoint.getCxfEndpointBean();
        if (cxfEndpointBean != null && cxfEndpointBean.getProperties() != null) {
            result = (String) cxfEndpointBean.getProperties().get(property);
        }
        return result;
    }
    
    public static DataFormat getDataFormat(CxfEndpoint endpoint) throws CamelException {
        String dataFormatString = endpoint.getDataFormat();
        if (dataFormatString == null) {
            dataFormatString = getCxfEndpointPropertyValue(endpoint, CxfConstants.DATA_FORMAT);           
        }

        if (dataFormatString == null) {
            return DataFormat.POJO;
        }

        DataFormat retval = DataFormat.asEnum(dataFormatString);

        if (retval == DataFormat.UNKNOWN) {
            throw new CamelException(new Message("INVALID_MESSAGE_FORMAT_XXXX", LOG, dataFormatString).toString());
        }

        return retval;
    }
}




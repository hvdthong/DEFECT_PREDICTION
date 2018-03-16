package org.apache.camel.component.cxf.util;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.AbstractBindingFactory;
import org.apache.cxf.binding.soap.interceptor.CheckFaultInterceptor;
import org.apache.cxf.binding.soap.interceptor.MustUnderstandInterceptor;
import org.apache.cxf.binding.soap.interceptor.ReadHeadersInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapActionInInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapActionOutInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderOutFilterInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapOutInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapPreProtocolOutInterceptor;
import org.apache.cxf.interceptor.AttachmentInInterceptor;
import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.interceptor.StaxInInterceptor;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.interceptor.URIMappingInterceptor;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.cxf.wsdl11.WSDLServiceFactory;

public class WSDLSoapServiceFactoryBean extends ReflectionServiceFactoryBean {
    private QName serviceName;
    private QName endpointName;

    @Override
    public Service create() {

        WSDLServiceFactory factory = new WSDLServiceFactory(getBus(), getWsdlURL(), getServiceQName());

        setService(factory.create());
        initializeSoapInterceptors();
        updateEndpointInfors();
        createEndpoints();

        return getService();
    }


    private void updateEndpointInfors() {
        Service service = getService();

        for (ServiceInfo inf : service.getServiceInfos()) {
            for (EndpointInfo ei : inf.getEndpoints()) {
                ei.getBinding().setProperty(AbstractBindingFactory.DATABINDING_DISABLED, Boolean.TRUE);
            }
        }

    }


    private void initializeSoapInterceptors() {
        getService().getInInterceptors().add(new DataInInterceptor());
        getService().getInInterceptors().add(new ReadHeadersInterceptor(getBus()));
        getService().getInInterceptors().add(new MustUnderstandInterceptor());
        getService().getInInterceptors().add(new AttachmentInInterceptor());
        getService().getInInterceptors().add(new SoapHeaderInterceptor());
        getService().getInInterceptors().add(new CheckFaultInterceptor());
        getService().getInInterceptors().add(new URIMappingInterceptor());

        getService().getInInterceptors().add(new StaxInInterceptor());
        getService().getInInterceptors().add(new SoapActionInInterceptor());

        getService().getOutInterceptors().add(new DataOutInterceptor());
        getService().getOutInterceptors().add(new SoapActionOutInterceptor());
        getService().getOutInterceptors().add(new AttachmentOutInterceptor());
        getService().getOutInterceptors().add(new StaxOutInterceptor());
        getService().getOutInterceptors().add(new SoapHeaderOutFilterInterceptor());

        getService().getOutInterceptors().add(new SoapPreProtocolOutInterceptor());
        getService().getOutInterceptors().add(new SoapOutInterceptor(getBus()));
        getService().getOutFaultInterceptors().add(new SoapOutInterceptor(getBus()));
    }

    public void setServiceName(QName name) {
        serviceName = name;
    }

    public String getServiceName() {
        return serviceName.toString();
    }

    public QName getServiceQName() {
        return serviceName;
    }

    public QName getEndpointName() {
        if (endpointName == null) {
            endpointName = getService().getEndpoints().keySet().iterator().next();
        }
        return endpointName;
    }

    public void setEndpointName(QName name) {
        endpointName = name;
    }
}

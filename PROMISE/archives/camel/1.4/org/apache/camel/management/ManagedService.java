package org.apache.camel.management;

import java.io.IOException;

import org.apache.camel.Service;
import org.apache.camel.impl.ServiceSupport;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Managed Service", currencyTimeLimit = 15)
public class ManagedService {

    private ServiceSupport service;

    public ManagedService(ServiceSupport service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    @ManagedAttribute(description = "Service running state")
    public boolean isStarted() throws IOException {
        return service.isStarted();
    }

    @ManagedOperation(description = "Start Service")
    public void start() throws IOException {
        try {
            service.start();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    @ManagedOperation(description = "Stop Service")
    public void stop() throws IOException {
        try {
            service.stop();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}

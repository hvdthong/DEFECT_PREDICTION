package org.apache.camel.component.spring.integration;

public final class SpringIntegrationHelper {
    private SpringIntegrationHelper() {
    }

    public static void checkSpringBeanInstance(Object bean, String name) {
        if (bean == null) {
            throw new IllegalArgumentException("Can't find the bean: " + name + " from the Spring context");
        }
    }
}

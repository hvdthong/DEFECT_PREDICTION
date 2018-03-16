package org.apache.camel.spring.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.util.ObjectHelper;

/**
 * A simple {@link RouteBuilder} which can be configured directly with one or more from URIs, zero or more to URIs
 * and an optional bean processing step.
 *
 * @version $Revision: 641676 $
 */
public class SimpleRouteBuilder extends RouteBuilder {
    private List<String> fromUris = new ArrayList<String>();
    private List<String> toUris = new ArrayList<String>();
    private Class beanType;
    private String beanClass;
    private String beanRef;
    private String beanMethod;

    public void configure() throws Exception {
        if (fromUris.isEmpty()) {
            throw new IllegalArgumentException("the fromUris property must contain at least one valid URI");
        }
        for (String fromUri : fromUris) {
            ProcessorType route = from(fromUri);

            addBeanCall(route);
            for (String toUri : toUris) {
                route = route.to(toUri);
            }
        }
    }

    public void setFromUri(String uri) {
        setFromUris(singletonList(uri));
    }

    public void setToUri(String uri) {
        setToUris(singletonList(uri));
    }

    public List<String> getFromUris() {
        return fromUris;
    }

    public void setFromUris(List<String> fromUris) {
        this.fromUris = fromUris;
    }

    public List<String> getToUris() {
        return toUris;
    }

    public void setToUris(List<String> toUris) {
        this.toUris = toUris;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanRef() {
        return beanRef;
    }

    public void setBeanRef(String beanRef) {
        this.beanRef = beanRef;
    }

    public Class getBeanType() {
        if (beanType == null) {
            if (beanClass != null) {
                beanType = ObjectHelper.loadClass(beanClass, getClass().getClassLoader());
            }
        }
        return beanType;
    }

    public void setBeanType(Class beanType) {
        this.beanType = beanType;
    }

    public String getBeanMethod() {
        return beanMethod;
    }

    public void setBeanMethod(String beanMethod) {
        this.beanMethod = beanMethod;
    }


    protected void addBeanCall(ProcessorType route) {
        Class type = getBeanType();
        if (type != null) {
            if (beanMethod != null) {
                route = route.bean(type, beanMethod);
            } else {
                route = route.bean(type);
            }
        } else if (beanRef != null) {
            if (beanMethod != null) {
                route = route.beanRef(beanRef, beanMethod);
            } else {
                route = route.beanRef(beanRef);
            }
        }
    }

    protected List<String> singletonList(String value) {
        List<String> uris = new ArrayList<String>();
        uris.add(value);
        return uris;
    }
}

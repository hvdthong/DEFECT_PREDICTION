package org.apache.ivy.plugins.resolver.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.ivy.util.url.ApacheURLLister;

public class ApacheHttpURLLister implements URLLister {
    private ApacheURLLister lister = new ApacheURLLister();

    public boolean accept(String pattern) {
        return pattern.startsWith("http");
    }

    public List listAll(URL url) throws IOException {
        return lister.listAll(url);
    }

    public String toString() {
        return "apache http lister";
    }
}

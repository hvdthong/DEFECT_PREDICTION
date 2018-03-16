package org.apache.ivy.core.module.descriptor;

public class License {
    private String name;

    private String url;

    public License(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}

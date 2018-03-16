package org.apache.ivy.util.filter;

public final class NoFilter implements Filter {
    public static final Filter INSTANCE = new NoFilter();

    private NoFilter() {
    }

    public boolean accept(Object o) {
        return true;
    }

    public String toString() {
        return "NoFilter";
    }
}

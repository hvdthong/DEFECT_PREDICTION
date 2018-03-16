package org.apache.ivy.util.filter;

public class NotFilter implements Filter {
    private Filter op;

    public NotFilter(Filter op) {
        this.op = op;
    }

    public Filter getOp() {
        return op;
    }

    public boolean accept(Object o) {
        return !op.accept(o);
    }
}

package org.apache.ivy.util.filter;

public class OrFilter implements Filter {
    private Filter op1;

    private Filter op2;

    public OrFilter(Filter op1, Filter op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    public Filter getOp1() {
        return op1;
    }

    public Filter getOp2() {
        return op2;
    }

    public boolean accept(Object o) {
        return op1.accept(o) || op2.accept(o);
    }
}

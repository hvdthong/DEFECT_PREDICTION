package org.apache.camel.model;

/**
 * A simple factory used to create new child nodes which allows pluggable extension points
 * such as to add extra DSL helper methods such as for the Groovy or Ruby DSLs
 *
 * @version $Revision: 698396 $
 */
public class NodeFactory {
    public FilterType createFilter() {
        return new FilterType();
    }

    public LoopType createLoop() {
        return new LoopType();
    }

    public RouteType createRoute() {
        return new RouteType();
    }
}

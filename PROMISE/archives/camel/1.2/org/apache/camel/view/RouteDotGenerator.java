package org.apache.camel.view;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.model.FromType;
import org.apache.camel.model.MulticastType;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.RouteType;
import static org.apache.camel.util.ObjectHelper.isNotNullAndNonEmpty;

/**
 * creates a DOT file showing the current routes
 *
 * @version $Revision: 523881 $
 */
public class RouteDotGenerator extends GraphGeneratorSupport {
    public RouteDotGenerator(String dir) {
        super(dir, ".dot");
    }


    protected void printRoutes(PrintWriter writer, Map<String, List<RouteType>> map) {
        Set<Map.Entry<String, List<RouteType>>> entries = map.entrySet();
        for (Map.Entry<String, List<RouteType>> entry : entries) {
            String group = entry.getKey();
            printRoutes(writer, group, entry.getValue());
        }
    }

    protected void printRoutes(PrintWriter writer, String group, List<RouteType> routes) {
        if (group != null) {
            writer.println("subgraph cluster_" + (clusterCounter++) + " {");
            writer.println("label = \"" + group + "\";");
            writer.println("color = grey;");
            writer.println("style = \"dashed\";");
            writer.println("URL = \"" + group + ".html\";");
            writer.println();
        }
        for (RouteType route : routes) {
            List<FromType> inputs = route.getInputs();
            for (FromType input : inputs) {
                printRoute(writer, route, input);
            }
            writer.println();
        }
        if (group != null) {
            writer.println("}");
            writer.println();
        }
    }

    protected String escapeNodeId(String text) {
        return text.replace('.', '_').replace("$", "_");
    }

    protected void printRoute(PrintWriter writer, final RouteType route, FromType input) {
        NodeData nodeData = getNodeData(input);

        printNode(writer, nodeData);


        List<ProcessorType> outputs = route.getOutputs();
        NodeData from = nodeData;
        for (ProcessorType output : outputs) {
            NodeData newData = printNode(writer, from, output);
            from = newData;
        }
    }

    protected NodeData printNode(PrintWriter writer, NodeData fromData, ProcessorType node) {
        if (node instanceof MulticastType) {
            List<ProcessorType> outputs = node.getOutputs();
            for (ProcessorType output : outputs) {
                printNode(writer, fromData, output);
            }
            return fromData;
        }
        NodeData toData = getNodeData(node);

        printNode(writer, toData);

        if (fromData != null) {
            writer.print(fromData.id);
            writer.print(" -> ");
            writer.print(toData.id);
            writer.println(" [");

            String label = fromData.edgeLabel;
            if (isNotNullAndNonEmpty(label)) {
                writer.println("label = \"" + label + "\"");
            }
            writer.println("];");
        }

        List<ProcessorType> outputs = toData.outputs;
        if (outputs != null) {
            for (ProcessorType output : outputs) {
                NodeData newData = printNode(writer, toData, output);
                if (!isMulticastNode(node)) {
                    toData = newData;
                }
            }
        }
        return toData;
    }

    protected void printNode(PrintWriter writer, NodeData data) {
        if (!data.nodeWritten) {
            data.nodeWritten = true;

            writer.println();
            writer.print(data.id);
            writer.println(" [");
            writer.println("label = \"" + data.label + "\"");
            writer.println("tooltip = \"" + data.tooltop + "\"");
            if (data.url != null) {
                writer.println("URL = \"" + data.url + "\"");
            }

            String image = data.image;
            if (image != null) {
                writer.println("shapefile = \"" + image + "\"");
                writer.println("peripheries=0");
            }
            String shape = data.shape;
            if (shape == null && image != null) {
                shape = "custom";
            }
            if (shape != null) {
                writer.println("shape = \"" + shape + "\"");
            }
            writer.println("];");
            writer.println();
        }
    }

    protected void generateFile(PrintWriter writer, Map<String, List<RouteType>> map) {
        writer.println("digraph CamelRoutes {");
        writer.println();

        writer.println("node [style = \"rounded,filled\", fillcolor = yellow, "
                + "fontname=\"Helvetica-Oblique\"];");
        writer.println();
        printRoutes(writer, map);

        writer.println("}");
    }
}

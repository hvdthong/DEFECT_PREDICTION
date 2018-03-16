package org.apache.camel.view;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.model.FromType;
import org.apache.camel.model.MulticastType;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.RouteType;
import static org.apache.camel.util.ObjectHelper.isNullOrBlank;

/**
 * @version $Revision: 640438 $
 */
public class XmlGraphGenerator extends GraphGeneratorSupport {
    private boolean addUrl = true;

    public XmlGraphGenerator(String dir) {
        super(dir, ".xml");
    }

    protected void generateFile(PrintWriter writer, Map<String, List<RouteType>> map) {
        writer.println("<?xml version='1.0' encoding='UTF-8'?>");
        writer.println("<Graph>");
        writer.println();

        if (map.size() > 0) {
            writer.println("<Node id='root' name='Camel Routes' description='Collection of Camel Routes' nodeType='root'/>");
        }
        printRoutes(writer, map);

        writer.println();
        writer.println("</Graph>");
    }

    protected void printRoutes(PrintWriter writer, Map<String, List<RouteType>> map) {
        Set<Map.Entry<String, List<RouteType>>> entries = map.entrySet();
        for (Map.Entry<String, List<RouteType>> entry : entries) {
            String group = entry.getKey();
            printRoutes(writer, group, entry.getValue());
        }
    }

    protected void printRoutes(PrintWriter writer, String group, List<RouteType> routes) {
        group = encode(group);
        if (group != null) {
            int idx = group.lastIndexOf('.');
            String name = group;
            if (idx > 0 && idx < group.length() - 1) {
                name = group.substring(idx + 1);
            }
            writer.println("<Node id='" + group + "' name='" + name + "' description='" + group + "' nodeType='group'/>");
            writer.println("<Edge fromID='root' toID='" + group + "'/>");
        }
        for (RouteType route : routes) {
            List<FromType> inputs = route.getInputs();
            boolean first = true;
            for (FromType input : inputs) {
                NodeData nodeData = getNodeData(input);
                if (first) {
                    first = false;
                    if (group != null) {
                        writer.println("<Edge fromID='" + group + "' toID='" + encode(nodeData.id) + "'/>");
                    }
                }
                printRoute(writer, route, nodeData);
            }
            writer.println();
        }
    }

    protected void printRoute(PrintWriter writer, final RouteType route, NodeData nodeData) {
        printNode(writer, nodeData);


        NodeData from = nodeData;
        for (ProcessorType output : route.getOutputs()) {
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
            writer.print("<Edge fromID=\"");
            writer.print(encode(fromData.id));
            writer.print("\" toID=\"");
            writer.print(encode(toData.id));
            String association = toData.edgeLabel;
            if (isNullOrBlank(association)) {
                writer.print("\" association=\"");
                writer.print(encode(association));
            }
            writer.println("\"/>");
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
            writer.print("<Node id=\"");
            writer.print(encode(data.id));
            writer.print("\" name=\"");
            String name = data.label;
            if (isNullOrBlank(name)) {
                name = data.tooltop;
            }
            writer.print(encode(name));
            writer.print("\" nodeType=\"");
            String nodeType = data.image;
            if (isNullOrBlank(nodeType)) {
                nodeType = data.shape;
                if (isNullOrBlank(nodeType)) {
                    nodeType = "node";
                }
            }
            writer.print(encode(nodeType));
            writer.print("\" description=\"");
            writer.print(encode(data.tooltop));
            if (addUrl) {
                writer.print("\" url=\"");
                writer.print(encode(data.url));
            }
            writer.println("\"/>");
        }
    }

    protected String encode(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").
                replaceAll(">", "&gt;").replaceAll("&", "&amp;");
    }
}

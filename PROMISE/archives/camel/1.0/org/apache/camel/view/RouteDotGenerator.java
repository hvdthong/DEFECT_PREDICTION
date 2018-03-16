package org.apache.camel.view;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.impl.EventDrivenConsumerRoute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * creates a DOT file showing the current routes
 *
 * @version $Revision: 523881 $
 */
public class RouteDotGenerator {
    private static final transient Log log = LogFactory.getLog(RouteDotGenerator.class);
    private String file = "CamelRoutes.dot";

    public String getFile() {
        return file;
    }

    /**
     * Sets the destination file name to create the destination diagram
     */
    public void setFile(String file) {
        this.file = file;
    }

    public void drawRoutes(CamelContext context) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        generateFile(writer, context);
    }

    protected void generateFile(PrintWriter writer, CamelContext context) {
        writer.println("digraph \"Camel Routes\" {");
        writer.println();
        writer.println("label=\"Camel Container: " + context + "\"];");
        writer.println();
        writer.println("node [style = \"rounded,filled\", fillcolor = yellow, fontname=\"Helvetica-Oblique\"];");
        writer.println();
        printRoutes(writer, context.getRoutes());
    }

    protected void printRoutes(PrintWriter writer, List<Route> routes) {
        for (Route r : routes) {
            Endpoint end = r.getEndpoint();
            writer.print(end.getEndpointUri());
            writer.print(" -> ");
            writer.print(r);
            writer.print(" -> ");
            if (r instanceof EventDrivenConsumerRoute) {
                EventDrivenConsumerRoute consumerRoute = (EventDrivenConsumerRoute) r;
                Processor p = consumerRoute.getProcessor();
                writer.println(p);
            }
        }
    }
}

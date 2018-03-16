package org.apache.camel.bam;

import java.util.Date;

import org.apache.camel.Endpoint;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.bam.model.ActivityState;
import org.apache.camel.bam.model.ProcessInstance;
import org.apache.camel.bam.rules.ActivityRules;
import org.apache.camel.builder.ProcessorFactory;
import org.apache.camel.impl.EventDrivenConsumerRoute;

/**
 * @version $Revision: $
 */
public class ActivityBuilder implements ProcessorFactory {
    private ProcessBuilder processBuilder;
    private Endpoint endpoint;
    private ActivityRules activityRules;
    private Expression correlationExpression;

    public ActivityBuilder(ProcessBuilder processBuilder, Endpoint endpoint) {
        this.processBuilder = processBuilder;
        this.endpoint = endpoint;
        this.activityRules = new ActivityRules(processBuilder);
        this.activityRules.setActivityName(endpoint.getEndpointUri());
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public Processor createProcessor() throws Exception {
        return processBuilder.createActivityProcessor(this);
    }

    public Route createRoute() throws Exception {
        Processor processor = createProcessor();
        if (processor == null) {
            throw new IllegalArgumentException("No processor created for ActivityBuilder: " + this);
        }
        return new EventDrivenConsumerRoute(getEndpoint(), processor);
    }

    public ActivityBuilder correlate(Expression correlationExpression) {
        this.correlationExpression = correlationExpression;
        return this;
    }

    public ActivityBuilder name(String name) {
        activityRules.setActivityName(name);
        return this;
    }

    /**
     * Create a temporal rule for when this step starts
     */
    public TimeExpression starts() {
        return new TimeExpression(this, ActivityLifecycle.Started) {
            public Date evaluate(ProcessInstance instance, ActivityState state) {
                return state.getTimeStarted();
            }
        };
    }

    /**
     * Create a temporal rule for when this step completes
     */
    public TimeExpression completes() {
        return new TimeExpression(this, ActivityLifecycle.Completed) {
            public Date evaluate(ProcessInstance instance, ActivityState state) {
                return state.getTimeCompleted();
            }
        };
    }

    public Expression getCorrelationExpression() {
        return correlationExpression;
    }

    public ActivityRules getActivityRules() {
        return activityRules;
    }

    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }
}

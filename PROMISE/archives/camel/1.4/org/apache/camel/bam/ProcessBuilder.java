package org.apache.camel.bam;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.bam.model.ActivityDefinition;
import org.apache.camel.bam.model.ProcessDefinition;
import org.apache.camel.bam.model.ProcessInstance;
import org.apache.camel.bam.processor.ActivityMonitorEngine;
import org.apache.camel.bam.processor.JpaBamProcessor;
import org.apache.camel.bam.rules.ProcessRules;
import org.apache.camel.builder.RouteBuilder;

import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * A builder of a process definition
 * 
 * @version $Revision: 630591 $
 */
public abstract class ProcessBuilder extends RouteBuilder {
    private static int processCounter;
    private JpaTemplate jpaTemplate;
    private final TransactionTemplate transactionTemplate;
    private final String processName;
    private List<ActivityBuilder> activityBuilders = new ArrayList<ActivityBuilder>();
    private Class entityType = ProcessInstance.class;
    private ProcessRules processRules = new ProcessRules();
    private ProcessDefinition processDefinition;

    protected ProcessBuilder(JpaTemplate jpaTemplate, TransactionTemplate transactionTemplate) {
        this(jpaTemplate, transactionTemplate, createProcessName());
    }

    protected ProcessBuilder(JpaTemplate jpaTemplate, TransactionTemplate transactionTemplate, String processName) {
        this.jpaTemplate = jpaTemplate;
        this.transactionTemplate = transactionTemplate;
        this.processName = processName;
    }

    protected static synchronized String createProcessName() {
        return "Process-" + (++processCounter);
    }

    public ActivityBuilder activity(String endpointUri) {
        return activity(endpoint(endpointUri));
    }

    public ActivityBuilder activity(Endpoint endpoint) {
        ActivityBuilder answer = new ActivityBuilder(this, endpoint);
        activityBuilders.add(answer);
        return answer;
    }

    /**
     * Sets the process entity type used to perform state management
     */
    public ProcessBuilder entityType(Class entityType) {
        this.entityType = entityType;
        return this;
    }

    public Processor createActivityProcessor(ActivityBuilder activityBuilder) {
        notNull(jpaTemplate, "jpaTemplate");
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                processRules.setProcessDefinition(getProcessDefinition());
            }
        });
        return new JpaBamProcessor(getTransactionTemplate(), getJpaTemplate(), activityBuilder.getCorrelationExpression(), activityBuilder.getActivityRules(), getEntityType());
    }

    public List<ActivityBuilder> getActivityBuilders() {
        return activityBuilders;
    }

    public Class getEntityType() {
        return entityType;
    }

    public JpaTemplate getJpaTemplate() {
        return jpaTemplate;
    }

    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public ProcessRules getProcessRules() {
        return processRules;
    }

    public String getProcessName() {
        return processName;
    }

    public ProcessDefinition getProcessDefinition() {
        if (processDefinition == null) {
            processDefinition = findOrCreateProcessDefinition();
        }
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    protected void populateRoutes(List<Route> routes) throws Exception {
        boolean first = true;
        for (ActivityBuilder builder : activityBuilders) {
            Route route = builder.createRoute();
            if (first) {
                route.getServices().add(new ActivityMonitorEngine(getJpaTemplate(), getTransactionTemplate(), getProcessRules()));
                first = false;
            }
            routes.add(route);
        }
    }

    public ActivityDefinition findOrCreateActivityDefinition(String activityName) {
        ProcessDefinition definition = getProcessDefinition();
        List<ActivityDefinition> list = jpaTemplate.find("select x from " + ActivityDefinition.class.getName() + " x where x.processDefinition = ?1 and x.name = ?2", definition, activityName);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            ActivityDefinition answer = new ActivityDefinition();
            answer.setName(activityName);
            answer.setProcessDefinition(ProcessDefinition.getRefreshedProcessDefinition(jpaTemplate, definition));
            jpaTemplate.persist(answer);
            return answer;
        }
    }

    protected ProcessDefinition findOrCreateProcessDefinition() {
        List<ProcessDefinition> list = jpaTemplate.find("select x from " + ProcessDefinition.class.getName() + " x where x.name = ?1", processName);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            ProcessDefinition answer = new ProcessDefinition();
            answer.setName(processName);
            jpaTemplate.persist(answer);
            return answer;
        }
    }
}

package org.apache.camel.bam.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.bam.model.ActivityState;
import org.apache.camel.bam.model.ProcessInstance;
import org.apache.camel.bam.rules.ActivityRules;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A concrete {@link Processor} for working on <a
 * the persistence and uses the {@link ProcessInstance} entity to store the
 * process information.
 * 
 * @version $Revision: $
 */
public class JpaBamProcessor extends JpaBamProcessorSupport<ProcessInstance> {
    private static final transient Log LOG = LogFactory.getLog(JpaBamProcessor.class);

    public JpaBamProcessor(TransactionTemplate transactionTemplate, JpaTemplate template, Expression<Exchange> correlationKeyExpression, ActivityRules activityRules) {
        super(transactionTemplate, template, correlationKeyExpression, activityRules);
    }

    public JpaBamProcessor(TransactionTemplate transactionTemplate, JpaTemplate template, Expression<Exchange> correlationKeyExpression, 
                           ActivityRules activityRules, Class<ProcessInstance> entitytype) {
        super(transactionTemplate, template, correlationKeyExpression, activityRules, entitytype);
    }

    protected void processEntity(Exchange exchange, ProcessInstance process) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing process instance: " + process);
        }

        ActivityRules rules = getActivityRules();
        ActivityState state = process.getOrCreateActivityState(rules);

        state.processExchange(rules, new ProcessContext(exchange, rules, state));

        rules.getProcessRules().processExchange(exchange, process);
    }
}

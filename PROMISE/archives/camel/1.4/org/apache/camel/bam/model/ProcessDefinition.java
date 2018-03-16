package org.apache.camel.bam.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.orm.jpa.JpaTemplate;

/**
 * @version $Revision: 630574 $
 */
@Entity
@UniqueConstraint(columnNames = {"name" })
public class ProcessDefinition extends EntitySupport {
    private static final transient Log LOG = LogFactory.getLog(ProcessDefinition.class);
    private String name;

    @Override
    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ProcessDefinition getRefreshedProcessDefinition(JpaTemplate template, ProcessDefinition definition) {

        ObjectHelper.notNull(definition, "definition");
        Long id = definition.getId();
        if (id == null) {
            LOG.warn("No primary key is available!");
            return findOrCreateProcessDefinition(template, definition.getName());
        }
        definition = template.find(ProcessDefinition.class, id);
        return definition;
    }

    public static ProcessDefinition findOrCreateProcessDefinition(JpaTemplate template, String processName) {
        List<ProcessDefinition> list = template.find("select x from " + ProcessDefinition.class.getName() + " x where x.name = ?1", processName);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            ProcessDefinition answer = new ProcessDefinition();
            answer.setName(processName);
            template.persist(answer);
            return answer;
        }
    }
}

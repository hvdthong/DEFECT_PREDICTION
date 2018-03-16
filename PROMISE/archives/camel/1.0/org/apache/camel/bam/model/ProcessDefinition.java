package org.apache.camel.bam.model;

import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * @version $Revision: 1.1 $
 */
@Entity
public class ProcessDefinition extends EntitySupport {
    private static final transient Log log = LogFactory.getLog(ProcessDefinition.class);
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
            log.warn("No primary key is available!");
            return findOrCreateProcessDefinition(template, definition.getName());
        }
        definition = template.find(ProcessDefinition.class, id);
        return definition;
    }

    public static ProcessDefinition findOrCreateProcessDefinition(JpaTemplate template, String processName) {
        List<ProcessDefinition> list = template.find("select x from " + ProcessDefinition.class.getName() + " x where x.name = ?1", processName);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        else {
            ProcessDefinition answer = new ProcessDefinition();
            answer.setName(processName);
            template.persist(answer);
            return answer;
        }
    }
}

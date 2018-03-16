package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Predicate;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;completedPredicate/&gt; element
 *
 * @version $Revision: 660266 $
 */
@XmlRootElement(name = "completedPredicate")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompletedPredicate {
    @XmlElementRef
    private ExpressionType completePredicate;
    @XmlTransient
    private Predicate predicate;

    public CompletedPredicate() {
    }

    public CompletedPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public ExpressionType getCompletePredicate() {
        return completePredicate;
    }

    public void setCompletePredicate(ExpressionType completePredicate) {
        this.completePredicate = completePredicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public Predicate createPredicate(RouteContext routeContext) {
        ExpressionType predicateType = getCompletePredicate();
        if (predicateType != null && predicate == null) {
            predicate = predicateType.createPredicate(routeContext);
        }
        return predicate;
    }
}

package org.apache.tools.ant.types.resources.selectors;

import java.util.Stack;
import java.util.Iterator;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.Quantifier;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
import org.apache.tools.ant.types.resources.comparators.DelegatedResourceComparator;

/**
 * ResourceSelector that compares against "control" Resource(s)
 * using ResourceComparators.
 * @since Ant 1.7
 */
public class Compare extends DataType implements ResourceSelector {

    private static final String ONE_CONTROL_MESSAGE
        = " the <control> element should be specified exactly once.";

    private DelegatedResourceComparator comp = new DelegatedResourceComparator();
    private Quantifier against = Quantifier.ALL;

    private Comparison when = Comparison.EQUAL;

    private Union control;

    /**
     * Add a ResourceComparator to this Compare selector.
     * If multiple ResourceComparators are added, they will be processed in LIFO order.
     * @param c the ResourceComparator to add.
     */
    public synchronized void add(ResourceComparator c) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        comp.add(c);
    }

    /**
     * Set the quantifier to be used. Default "all".
     * @param against the Quantifier EnumeratedAttribute to use.
     */
    public synchronized void setAgainst(Quantifier against) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.against = against;
    }

    /**
     * Set the comparison to be used. Default "equal".
     * @param when the Comparison EnumeratedAttribute to use.
     */
    public synchronized void setWhen(Comparison when) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.when = when;
    }

    /**
     * Create the nested control element. These are the
     * resources to compare against.
     * @return ResourceCollection.
     */
    public synchronized ResourceCollection createControl() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (control != null) {
            throw oneControl();
        }
        control = new Union();
        return control;
    }

    /** {@inheritDoc} */
    public synchronized boolean isSelected(Resource r) {
        if (isReference()) {
            return ((ResourceSelector) getCheckedRef()).isSelected(r);
        }
        if (control == null) {
            throw oneControl();
        }
        int t = 0, f = 0;
        for (Iterator it = control.iterator(); it.hasNext();) {
            if (when.evaluate(comp.compare(r, (Resource) it.next()))) {
                t++;
            } else {
                f++;
            }
        }
        return against.evaluate(t, f);
    }

    /**
     * Overrides the version from DataType
     * to recurse on nested ResourceComparators.
     * @param stk the stack of data types to use (recursively).
     * @param p   the project to use to dereference the references.
     * @throws BuildException on error.
     */
    protected synchronized void dieOnCircularReference(Stack stk, Project p)
        throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
        } else {
            if (control != null) {
                DataType.invokeCircularReferenceCheck(control, stk, p);
            }
            DataType.invokeCircularReferenceCheck(comp, stk, p);
            setChecked(true);
        }
    }

    private BuildException oneControl() {
        return new BuildException(super.toString() + ONE_CONTROL_MESSAGE);
    }
}

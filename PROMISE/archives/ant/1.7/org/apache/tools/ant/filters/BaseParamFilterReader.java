package org.apache.tools.ant.filters;

import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Parameterizable;

/**
 * Parameterized base class for core filter readers.
 *
 */
public abstract class BaseParamFilterReader
    extends BaseFilterReader
    implements Parameterizable {
    /** The passed in parameter array. */
    private Parameter[] parameters;

    /**
     * Constructor for "dummy" instances.
     *
     * @see BaseFilterReader#BaseFilterReader()
     */
    public BaseParamFilterReader() {
        super();
    }

    /**
     * Creates a new filtered reader.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public BaseParamFilterReader(final Reader in) {
        super(in);
    }

    /**
     * Sets the parameters used by this filter, and sets
     * the filter to an uninitialized status.
     *
     * @param parameters The parameters to be used by this filter.
     *                   Should not be <code>null</code>.
     */
    public final void setParameters(final Parameter[] parameters) {
        this.parameters = parameters;
        setInitialized(false);
    }

    /**
     * Returns the parameters to be used by this filter.
     *
     * @return the parameters to be used by this filter
     */
    protected final Parameter[] getParameters() {
        return parameters;
    }
}

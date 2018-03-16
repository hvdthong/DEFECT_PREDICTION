package org.apache.ivy.core.settings;

/**
 * Implemented by settings element which need to perform validation when settings are loaded.
 */
public interface Validatable {
    /**
     * Validates the Validatable, throwing an {@link IllegalStateException} if the current state is
     * not valid.
     * 
     * @throws IllegalStateException
     *             if the state of the {@link Validatable} is not valid.
     */
    public void validate();
}

package org.apache.camel.impl.converter;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;
import org.apache.camel.util.ObjectHelper;

/**
 * Uses the {@link java.beans.PropertyEditor} conversion system to convert Objects to
 * and from String values.
 *
 * @version $Revision: 687545 $
 */
public class PropertyEditorTypeConverter implements TypeConverter {

    public <T> T convertTo(Class<T> toType, Object value) {
        if (value == null) {
            return null;
        }

        if (value.getClass() == String.class) {
            if (toType == String.class) {
                return ObjectHelper.cast(toType, value);
            }

            PropertyEditor editor = PropertyEditorManager.findEditor(toType);
            if (editor != null) {
                editor.setAsText(value.toString());
                return ObjectHelper.cast(toType, editor.getValue());
            }
        } else if (toType == String.class) {
            PropertyEditor editor = PropertyEditorManager.findEditor(value.getClass());
            if (editor != null) {
                editor.setValue(value);
                return ObjectHelper.cast(toType, editor.getAsText());
            }
        }

        return null;
    }

    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) {
        return convertTo(type, value);
    }
}

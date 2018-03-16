package org.apache.camel.component.uface;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.list.ListEndpoint;
import org.apache.camel.util.ObjectHelper;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;

/**
 * @version $Revision: 655516 $
 */
public class UFaceEndpoint extends ListEndpoint {
    public UFaceEndpoint(String uri, UFaceComponent component) {
        super(uri, component);
    }

    public UFaceEndpoint(String endpointUri) {
        super(endpointUri);
    }

    @Override
    protected List<Exchange> createExchangeList() {
        Realm realm = Realm.getDefault();
        ObjectHelper.notNull(realm, "DataBinding Realm");
        return new WritableList(realm);
    }
}

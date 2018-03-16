package org.apache.synapse.config.xml;

import org.apache.synapse.mediators.AbstractListMediator;
import org.apache.synapse.MessageContext;

/**
 * This mediator represents an unnamed list mediator
 */

public class AnonymousListMediator extends AbstractListMediator {

     public boolean mediate(MessageContext synCtx) {
         return super.mediate(synCtx);
     }
}

package org.apache.ivy.core.event.retrieve;

import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.retrieve.RetrieveOptions;

public class StartRetrieveEvent extends RetrieveEvent {
    public static final String NAME = "pre-retrieve";

    public StartRetrieveEvent(ModuleRevisionId mrid, String[] confs, RetrieveOptions options) {
        super(NAME, mrid, confs, options);
    }
}

package org.apache.ivy.core.event.retrieve;

import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.retrieve.RetrieveOptions;

public class RetrieveEvent extends IvyEvent {
    private ModuleRevisionId mrid;
    private RetrieveOptions options;

    protected RetrieveEvent(String name, ModuleRevisionId mrid, 
            String[] confs, RetrieveOptions options) {
        super(name);
        this.mrid = mrid;
        addMridAttributes(mrid);
        addConfsAttribute(confs);
        addAttribute("symlink", String.valueOf(options.isMakeSymlinks()));
        addAttribute("sync", String.valueOf(options.isSync()));
        this.options = options;
    }

    public ModuleRevisionId getModuleRevisionId() {
        return mrid;
    }
    
    public RetrieveOptions getOptions() {
        return options;
    }
}

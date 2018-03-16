package org.apache.ivy.plugins.namespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ivy.core.module.id.ModuleRevisionId;

public class Namespace {
    public static final Namespace SYSTEM_NAMESPACE;
    static {
        SYSTEM_NAMESPACE = new Namespace();
    }

    private List rules = new ArrayList();

    private String name;

    private boolean chainRules = false;

    private NamespaceTransformer fromSystemTransformer = new NamespaceTransformer() {
        public ModuleRevisionId transform(ModuleRevisionId mrid) {
            if (mrid == null) {
                return null;
            }
            for (Iterator iter = rules.iterator(); iter.hasNext();) {
                NamespaceRule rule = (NamespaceRule) iter.next();
                ModuleRevisionId nmrid = rule.getFromSystem().transform(mrid);
                if (chainRules) {
                    mrid = nmrid;
                } else if (!nmrid.equals(mrid)) {
                    return nmrid;
                }
            }
            return mrid;
        }

        public boolean isIdentity() {
            return rules.isEmpty();
        }
    };

    private NamespaceTransformer toSystemTransformer = new NamespaceTransformer() {
        public ModuleRevisionId transform(ModuleRevisionId mrid) {
            if (mrid == null) {
                return null;
            }
            for (Iterator iter = rules.iterator(); iter.hasNext();) {
                NamespaceRule rule = (NamespaceRule) iter.next();
                ModuleRevisionId nmrid = rule.getToSystem().transform(mrid);
                if (chainRules) {
                    mrid = nmrid;
                } else if (!nmrid.equals(mrid)) {
                    return nmrid;
                }
            }
            return mrid;
        }

        public boolean isIdentity() {
            return rules.isEmpty();
        }
    };

    public void addRule(NamespaceRule rule) {
        rules.add(rule);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NamespaceTransformer getFromSystemTransformer() {
        return fromSystemTransformer;
    }

    public NamespaceTransformer getToSystemTransformer() {
        return toSystemTransformer;
    }

    public boolean isChainrules() {
        return chainRules;
    }

    public void setChainrules(boolean chainRules) {
        this.chainRules = chainRules;
    }
}

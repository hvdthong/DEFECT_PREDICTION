package fr.jayasoft.ivy;


public class DefaultPublishingDRResolver implements PublishingDependencyRevisionResolver {
    public String resolve(ModuleDescriptor published, String publishedStatus, ModuleRevisionId depMrid, String status) {
        return depMrid.getRevision();
    }
}

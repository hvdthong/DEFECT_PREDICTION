import fr.jayasoft.ivy.ModuleRevisionId;

public interface CircularDependencyStrategy {
	String getName();

	void handleCircularDependency(ModuleRevisionId[] mrids);

}

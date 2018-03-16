package fr.jayasoft.ivy.filter;

public class NoFilter implements Filter {
	public static final Filter INSTANCE = new NoFilter();
	
	private NoFilter() {
	}
	
    public boolean accept(Object o) {
        return true;
    }

}

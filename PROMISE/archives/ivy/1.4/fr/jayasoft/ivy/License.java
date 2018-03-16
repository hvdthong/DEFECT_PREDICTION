package fr.jayasoft.ivy;

public class License {
    private String _name;
    private String _url;
    public License(String name, String url) {
        _name = name;
        _url = url;
    }

    public String getName() {
        return _name;
    }
    
    public String getUrl() {
        return _url;
    }
    
}

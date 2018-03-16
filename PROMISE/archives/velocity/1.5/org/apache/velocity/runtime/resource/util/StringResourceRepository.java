public interface StringResourceRepository
{
    /**
     * get the string resource that is stored with given key
     * @param name String name to retrieve from the repository.
     * @return A StringResource containing the template.
     */
    StringResource getStringResource(String name);

    /**
     * add a string resource with given key.
     * @param name The String name to store the template under.
     * @param body A String containing a template.
     */
    void putStringResource(String name, String body);

    /**
     * delete a string resource with given key.
     * @param name The string name to remove from the repository.
     */
    void removeStringResource(String name);
    
    /**
     * Sets the encoding of the repository. The encoding should be stored per
     * template string so that multiple encodings can be stored in a single repository.
     * The default implementation does this correctly.
     * 
     * @param encoding The encoding to use.
     */
    void setEncoding(String encoding);
    
    /**
     * Returns the current encoding of this repository.
     * 
     * @return The current encoding of this repository.
     */
    String getEncoding();
}

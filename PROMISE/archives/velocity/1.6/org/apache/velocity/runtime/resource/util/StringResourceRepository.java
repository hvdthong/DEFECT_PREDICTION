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
     * add a string resource with given key.
     * @param name The String name to store the template under.
     * @param body A String containing a template.
     * @param encoding The encoding of this string template
     * @since 1.6
     */
    void putStringResource(String name, String body, String encoding);

    /**
     * delete a string resource with given key.
     * @param name The string name to remove from the repository.
     */
    void removeStringResource(String name);
    
    /**
     * Sets the default encoding of the repository. Encodings can also be stored per
     * template string. The default implementation does this correctly.
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

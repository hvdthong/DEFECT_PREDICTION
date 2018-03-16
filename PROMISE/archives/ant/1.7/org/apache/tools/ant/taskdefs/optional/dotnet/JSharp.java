package org.apache.tools.ant.taskdefs.optional.dotnet;

import org.apache.tools.ant.BuildException;


/**
 * Compile J# source down to a managed .NET application.
 * <p>
 * J# is not Java. But it is the language closest to Java in the .NET framework.
 * This task compiles jsharp source (.java files), and
 * generates a .NET managed exe or dll.
 * <p>
 *
 * <p>For historical reasons the pattern
 * <code>**</code><code>/*.java</code> is preset as includes list and
 * you can not override it with an explicit includes attribute.  Use
 * nested <code>&lt;src&gt;</code> elements instead of the basedir
 * attribute if you need more control.</p>
 *
 * @see <a ref=
 * >Visual J++ online documentation</a>
 *
 * @since ant1.6
 * @ant.task category="dotnet" name="jsharpc"
 */
public class JSharp extends DotnetCompile {
    /**
     * hex base address
     */
    String baseAddress;

    /** /x option to disable J++ and J# lang extensions
     *
     */
    boolean pureJava = true;

    /**
     * whether to make package scoped stuff public or assembly scoped
     */
    boolean secureScoping = false;


    /** No arg constructor. */
    public JSharp() {
        setExecutable("vjc");
    }


    /**
     * Set the base address attribute.
     * @param baseAddress the value to use.
     */
    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    /**
     * do we want pure java (default, true) or corrupted J#?
     * @param pureJava a <code>boolean</code> value.
     */
    public void setPureJava(boolean pureJava) {
        this.pureJava = pureJava;
    }

    /**
     * Make package scoped code visible to the current assembly only (default: false)
     * .NET does not have package scoping. Instead it has assembly, private and public.
     * By default, package content is public to all.
     * @param secureScoping a <code>boolean</code> value.
     */
    public void setSecureScoping(boolean secureScoping) {
        this.secureScoping = secureScoping;
    }

    /**
     * Get the delimiter that the compiler uses between references.
     * For example, c# will return ";"; VB.NET will return ","
     * @return The string delimiter for the reference string.
     */
    public String getReferenceDelimiter() {
        return ";";
    }

    /**
     * Get the extension of filenames to compile.
     * @return The string extension of files to compile.
     */
    public String getFileExtension() {
        return ".java";
    }

    /**
     * add jvc specific commands
     * @param command the command to add to.
     */
    protected void addCompilerSpecificOptions(NetCommand command) {
        if (pureJava) {
            command.addArgument("/x:all");
        }
        if (secureScoping) {
            command.addArgument("/securescoping");
        }
    }

    /** {@inheritDoc} */
    protected void createResourceParameter(NetCommand command, DotnetResource resource) {
        resource.getParameters(getProject(), command, true);
    }

    /**
     * validation code
     * @throws  org.apache.tools.ant.BuildException  if validation failed
     */
    protected void validate()
            throws BuildException {
        super.validate();
        if (getDestFile() == null) {
            throw new BuildException("DestFile was not specified");
        }
    }
}

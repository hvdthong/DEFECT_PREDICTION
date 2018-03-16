package org.apache.camel.spring.xml;

import org.apache.camel.util.ObjectHelper;

/**
 * A {@link BeanDefinitionParser} which lazy loads the type on which it creates to allow the schema to be loosly coupled
 * with the camel jars.
 *
 * @version $Revision: 1.1 $
 */
public class LazyLoadingBeanDefinitionParser extends BeanDefinitionParser {
    private String className;
    private String moduleName;

    public LazyLoadingBeanDefinitionParser(String className, String moduleName) {
        this.className = className;
        this.moduleName = moduleName;
    }

    @Override
    protected Class loadType() {
        Class<?> answer = ObjectHelper.loadClass(className, getClass().getClassLoader());
        if (answer == null) {
            throw new IllegalArgumentException("Class: " + className + " could not be found. You need to add Camel module: " + moduleName + " to your classpath");
        }
        return answer;
    }
}

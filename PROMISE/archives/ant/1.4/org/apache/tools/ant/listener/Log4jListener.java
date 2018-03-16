package org.apache.tools.ant.listener;

import org.apache.tools.ant.*;

import org.apache.log4j.Category;
import org.apache.log4j.helpers.NullEnumeration;

  
/**
 *  Listener which sends events to Log4j logging system
 *
 * @author <a href="mailto:conor@apache.org>Conor MacNeill </a>
 */
public class Log4jListener implements BuildListener {
    static final String LOG4J_CONFIG_PROPERTY = "log4j.configuration";
    
    private boolean initialized = false;
    
    public Log4jListener() {
        initialized = false;
        Category cat = Category.getInstance("org.apache.tools.ant");
        Category rootCat = Category.getRoot();
        if (!(rootCat.getAllAppenders() instanceof NullEnumeration)) {
            initialized = true;
        }
        else {
            cat.error("No log4j.properties in build area");
        }
    }
    
    public void buildStarted(BuildEvent event) {
        if (initialized) {
            Category cat = Category.getInstance(Project.class.getName());
            cat.info("Build started.");
        }
    }
    
    public void buildFinished(BuildEvent event) {
        if (initialized) {
            Category cat = Category.getInstance(Project.class.getName());
            if (event.getException() == null) {
                cat.info("Build finished.");
            }
            else {
                cat.error("Build finished with error.", event.getException());
            }
        }   
    }
    
    public void targetStarted(BuildEvent event) {
        if (initialized) {
            Category cat = Category.getInstance(Target.class.getName());
            cat.info("Target \"" + event.getTarget().getName() + "\" started.");
        }
    }
    
    public void targetFinished(BuildEvent event) {
        if (initialized) {
            String targetName = event.getTarget().getName();
            Category cat = Category.getInstance(Target.class.getName());
            if (event.getException() == null) {
                cat.info("Target \"" + event.getTarget().getName() + "\" finished.");
            }
            else {
                cat.error("Target \"" + event.getTarget().getName() + "\" finished with error.", event.getException());
            }
        } 
    }
    
    public void taskStarted(BuildEvent event) {
        if (initialized) {
            Task task = event.getTask();
            Category cat = Category.getInstance(task.getClass().getName());
            cat.info("Task \"" + task.getTaskName() + "\" started.");
        }
    }
    
    public void taskFinished(BuildEvent event) {
        if (initialized) {
            Task task = event.getTask();
            Category cat = Category.getInstance(task.getClass().getName());
            if (event.getException() == null) {
                cat.info("Task \"" + task.getTaskName() + "\" finished.");
            }
            else {
                cat.error("Task \"" + task.getTaskName() + "\" finished with error.", event.getException());
            }
        }
    }
    
    public void messageLogged(BuildEvent event) {
        if (initialized) {
            Object categoryObject = event.getTask();
            if (categoryObject == null) {
                categoryObject = event.getTarget();
                if (categoryObject == null) {
                    categoryObject = event.getProject();
                }
            }
            
            Category cat = Category.getInstance(categoryObject.getClass().getName());
            switch (event.getPriority()) {
                case Project.MSG_ERR:
                    cat.error(event.getMessage());
                    break;
                case Project.MSG_WARN:
                    cat.warn(event.getMessage());
                    break;
                case Project.MSG_INFO:
                    cat.info(event.getMessage());
                    break;
                case Project.MSG_VERBOSE:
                    cat.debug(event.getMessage());
                    break;
                case Project.MSG_DEBUG:
                    cat.debug(event.getMessage());
                    break;
                default:                        
                    cat.error(event.getMessage());
                    break;
            }
        }
    }
}

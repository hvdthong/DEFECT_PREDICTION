package org.apache.camel.spring.xml;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;

/**
 * A {@link RouteBuilder} which is given a list of {@link BuilderStatement} objects
 * to use to create the routes. This is used by the Spring 2 XML parsing code in particular
 * the {@link RouteBuilderFactoryBean}
 *
 * @version $Revision: 1.1 $
*/
public class StatementRouteBuilder extends RouteBuilder  {
    private ArrayList<BuilderStatement> routes;
    private BeanFactory beanFactory;

    @Override
    public void configure() {
        for (BuilderStatement routeFactory : routes) {
            routeFactory.create(beanFactory, this);
        }
    }

    public ArrayList<BuilderStatement> getRoutes() {
        return routes;
    }
    public void setRoutes(ArrayList<BuilderStatement> routes) {
        this.routes = routes;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}

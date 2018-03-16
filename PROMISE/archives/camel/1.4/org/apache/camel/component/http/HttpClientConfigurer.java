package org.apache.camel.component.http;

import org.apache.commons.httpclient.HttpClient;

/**
 * A plugable strategy for configuring the HttpClient used by this component
 *
 * @version $Revision: 640731 $
 */
public interface HttpClientConfigurer {
    /**
     * Configure the HttpClient such as setting the authentication or proxying details
     *
     * @param client
     */
    void configureHttpClient(HttpClient client);
}

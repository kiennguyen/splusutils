package com.kiennguyen.commons;

import com.sun.jersey.api.client.Client;
import se.bonnier.api.model.ApiClientFactory;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public abstract class ServicePlusAPI {
    protected String frameworkId;
    protected String accessToken;
    public ServicePlusAPI(String frameworkId) {
        ApiClientFactory.initialiseClients(frameworkId);
    }
    protected Client client = Client.create();

    public abstract void process() throws Exception;
}

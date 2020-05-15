package de.mhus.osgi.dev.k8s.impl;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;

public class MyController {

    public static void main(String[] parameters) throws IOException {
        
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        

    }

}

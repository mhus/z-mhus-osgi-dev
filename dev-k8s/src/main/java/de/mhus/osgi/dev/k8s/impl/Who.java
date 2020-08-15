package de.mhus.osgi.dev.k8s.impl;

import java.io.IOException;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.auth.ApiKeyAuth;
import io.kubernetes.client.util.Config;

public class Who {

    public static void main(String[] parameters) throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        CoreV1Api api = new CoreV1Api();

        System.out.println(client.getAuthentications());
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentications().get("BearerToken");
        System.out.println("key:" + auth.getApiKey());
    }
}

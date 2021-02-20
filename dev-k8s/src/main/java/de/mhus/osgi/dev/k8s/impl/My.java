/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.osgi.dev.k8s.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.generic.GenericKubernetesApi;
import io.kubernetes.client.util.generic.KubernetesApiResponse;

public class My {

    public static void main(String[] parameters) throws IOException, ApiException {
        main1(parameters);
    }

    public static void main1(String[] parameters) throws IOException, ApiException {
        MProperties attr = IProperties.explodeToMProperties(parameters);

        ApiClient client = Config.defaultClient();
        client.setDebugging(true);
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        // check if exists
        boolean exists = false;
        try {
            @SuppressWarnings("unused")
            V1Service existing =
                    api.readNamespacedService("dev-dynamic", "dev-test", null, null, null);
            if (attr.getBoolean("recreate", false)) {
                @SuppressWarnings("unused")
                V1Status resDelete =
                        api.deleteNamespacedService(
                                "dev-dynamic", "dev-test", null, null, 5, null, null, null);
            } else exists = true;
        } catch (ApiException e) {
            e.printStackTrace();
        }

        V1Service service = new V1Service();
        V1ObjectMeta metadata = new V1ObjectMeta();
        Map<String, String> labels = new HashMap<String, String>();
        labels.put("app", "dev");
        metadata.setLabels(labels);
        metadata.setName("dev-dynamic");
        metadata.setNamespace("dev-test");
        service.setApiVersion("v1");
        service.setMetadata(metadata);
        V1ServiceSpec spec = new V1ServiceSpec();
        List<V1ServicePort> ports = new LinkedList<V1ServicePort>();
        {
            V1ServicePort port = new V1ServicePort();
            port.setName("port");
            port.setPort(8080);
            port.setTargetPort(new IntOrString(8181));
            ports.add(port);
        }
        spec.setPorts(ports);
        Map<String, String> selector = new HashMap<>();
        selector.put("app", "dev-server");
        spec.setSelector(selector);
        spec.setType("LoadBalancer");
        service.setSpec(spec);
        service.setKind("Service");

        GenericKubernetesApi<V1Service, V1ServiceList> serviceClient =
                new GenericKubernetesApi<>(
                        V1Service.class,
                        V1ServiceList.class,
                        "",
                        "v1",
                        "services",
                        api.getApiClient());

        KubernetesApiResponse<V1Service> response = null;
        if (exists) response = serviceClient.update(service);
        else response = serviceClient.create(service);
        if (!response.isSuccess()) throw new RuntimeException(response.getStatus().toString());

        // api.createNamespacedService("dev-test", service, null, null, "");

    }

    public static void main2(String[] args) throws IOException {
        //        ApiClient defaultClient = Configuration.getDefaultApiClient();
        //        defaultClient.setBasePath("http://localhost");
        //
        //        // Configure API key authorization: BearerToken
        //        ApiKeyAuth BearerToken = (ApiKeyAuth)
        // defaultClient.getAuthentication("BearerToken");
        //        BearerToken.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to
        // null)
        // BearerToken.setApiKeyPrefix("Token");

        ApiClient defaultClient = ClientBuilder.cluster().build();
        Configuration.setDefaultApiClient(defaultClient);

        CoreV1Api apiInstance = new CoreV1Api(defaultClient);

        String namespace =
                "dev-test"; // String | object name and auth scope, such as for teams and projects
        V1Service body = new V1Service(); // V1Service |
        String pretty = "pretty_example"; // String | If 'true', then the output is pretty printed.
        String dryRun =
                "dryRun_example"; // String | When present, indicates that modifications should not
        // be persisted. An invalid or unrecognized dryRun directive will
        // result in an error response and no further processing of the
        // request. Valid values are: - All: all dry run stages will be
        // processed
        String fieldManager =
                "fieldManager_example"; // String | fieldManager is a name associated with the actor
        // or entity that is making these changes. The value must be
        // less than or 128 characters long, and only contain
        // printable characters, as defined by
        // https://golang.org/pkg/unicode/#IsPrint.
        try {
            V1Service result =
                    apiInstance.createNamespacedService(
                            namespace, body, pretty, dryRun, fieldManager);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespacedService");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}

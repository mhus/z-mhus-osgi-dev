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
/*
Copyright 2017 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package de.mhus.osgi.dev.k8s.impl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.gson.reflect.TypeToken;

import de.mhus.lib.core.MThread;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import okhttp3.OkHttpClient;

// https://github.com/kubernetes-client/java/blob/master/examples/src/main/java/io/kubernetes/client/examples/WatchExample.java
/** A simple example of how to use Watch API to watch changes in Namespace list. */
public class WatchExample {

    private Watch<V1Node> watchNode;
    private Watch<V1Pod> watchPod;
    private Watch<V1Service> watchService;

    public WatchExample(String[] args) throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        // infinite timeout
        OkHttpClient httpClient =
                client.getHttpClient().newBuilder().readTimeout(0, TimeUnit.SECONDS).build();
        client.setHttpClient(httpClient);
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        //    Watch<V1Namespace> watch =
        //        Watch.createWatch(
        //            client,
        //            api.listNamespaceCall(null, null, null, null, null, 5, null, null,
        // Boolean.TRUE, null),
        //            new TypeToken<Watch.Response<V1Namespace>>() {}.getType());
        //
        //    try {
        //      for (Watch.Response<V1Namespace> item : watch) {
        //        System.out.printf("%s : %s%n", item.type, item.object.getMetadata().getName());
        //      }
        //    } finally {
        //      watch.close();
        //    }

        watchNode =
                Watch.createWatch(
                        client,
                        api.listNodeCall(
                                null, true, null, null, null, 5, null, null, Boolean.TRUE, null),
                        new TypeToken<Watch.Response<V1Node>>() {}.getType());

        MThread.run(
                new Runnable() {

                    @Override
                    public void run() {
                        for (Watch.Response<V1Node> item : watchNode) {
                            System.out.println(
                                    "WatchNode: "
                                            + item.type
                                            + " "
                                            + item.object.getKind()
                                            + " "
                                            + item.object.getMetadata().getName());
                            System.out.println(item.object.getStatus());
                        }
                    }
                });

        watchPod =
                Watch.createWatch(
                        client,
                        api.listPodForAllNamespacesCall(
                                true, null, null, null, 5, null, null, null, Boolean.TRUE, null),
                        new TypeToken<Watch.Response<V1Pod>>() {}.getType());

        MThread.run(
                new Runnable() {

                    @Override
                    public void run() {
                        for (Watch.Response<V1Pod> item : watchPod) {
                            System.out.println(
                                    "WatchPod: "
                                            + item.type
                                            + " "
                                            + item.object.getKind()
                                            + " "
                                            + item.object.getMetadata().getName());
                            System.out.println(item.object.getStatus());
                        }
                    }
                });

        watchService =
                Watch.createWatch(
                        client,
                        api.listServiceForAllNamespacesCall(
                                true, null, null, null, 5, null, null, null, Boolean.TRUE, null),
                        new TypeToken<Watch.Response<V1Service>>() {}.getType());

        MThread.run(
                new Runnable() {

                    @Override
                    public void run() {
                        for (Watch.Response<V1Service> item : watchService) {
                            System.out.println(
                                    "Watchervice: "
                                            + item.type
                                            + " "
                                            + item.object.getKind()
                                            + " "
                                            + item.object.getMetadata().getName());
                            System.out.println(item.object.getStatus());
                        }
                    }
                });
    }

    public void close() throws IOException {
        if (watchNode != null) watchNode.close();
        watchNode = null;
        if (watchPod != null) watchPod.close();
        watchPod = null;
        if (watchService != null) watchService.close();
        watchService = null;
    }
}

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

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;

// https://medium.com/@trstringer/create-kubernetes-controllers-for-core-and-custom-resources-62fc35ad64a3
// https://blog.frankel.ch/your-own-kubernetes-controller/2/
// https://github.com/fluent/fluentd-kubernetes-daemonset/issues/14

public class WithFabric8 {
    private DefaultKubernetesClient client;

    public WithFabric8() throws IOException {

        client = new DefaultKubernetesClient();

        client.pods().inAnyNamespace().watch(new DummyWatcher());
    }

    public void close() {
        client.close();
    }

    public static class DummyWatcher implements Watcher<Pod> {

        @Override
        public void eventReceived(Action action, Pod pod) {
            switch (action) {
                case ADDED:
                    System.out.println(
                            "DummyWatcher::Added "
                                    + pod.getMetadata().getNamespace()
                                    + "/"
                                    + pod.getMetadata().getName());
                    break;
                case MODIFIED:
                    System.out.println(
                            "DummyWatcher::Modified "
                                    + pod.getMetadata().getNamespace()
                                    + "/"
                                    + pod.getMetadata().getName());
                    break;
                case DELETED:
                    System.out.println(
                            "DummyWatcher::Deleted "
                                    + pod.getMetadata().getNamespace()
                                    + "/"
                                    + pod.getMetadata().getName());
                    break;
                case ERROR:
                    System.out.println(
                            "DummyWatcher::Error "
                                    + pod.getMetadata().getNamespace()
                                    + "/"
                                    + pod.getMetadata().getName());
                    break;
            }
        }

        @Override
        public void onClose(KubernetesClientException cause) {
            System.out.println("DummyWatcher::Close");
        }
    }
}

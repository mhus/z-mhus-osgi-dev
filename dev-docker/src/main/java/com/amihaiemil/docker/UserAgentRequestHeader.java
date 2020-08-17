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
package com.amihaiemil.docker;

import java.util.Collections;
import org.apache.http.HttpHeaders;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.message.BasicHeader;

/**
 * User Agent Request Header Interceptor.
 *
 * @author Boris Kuzmic (boris.kuzmic@gmail.com)
 * @since 0.0.7
 */
final class UserAgentRequestHeader extends RequestDefaultHeaders {

    /** Config properties file. */
    private static final String CONFIG_FILE = "config.properties";

    /** Version property key. */
    private static final String VERSION_KEY = "build.version";

    /** Ctor. */
    UserAgentRequestHeader() {
        super(
                Collections.singletonList(
                        new BasicHeader(
                                HttpHeaders.USER_AGENT,
                                String.join(
                                        " ",
                                        "docker-java-api /",
                                        version(),
                                        "See https://github.com/amihaiemil/docker-java-api"))));
    }

    /**
     * Read current version from property file.
     *
     * @return Build version.
     */
    private static String version() {
        return "0.0.12";
    }
}

/**
 * Copyright (C) 2012 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesscomputing.service.discovery.testing.server;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.nesscomputing.service.discovery.client.DiscoveryClient;
import com.nesscomputing.service.discovery.client.DiscoveryClientModule;
import com.nesscomputing.service.discovery.client.ReadOnlyDiscoveryClient;
import com.nesscomputing.service.discovery.testing.client.MemoryDiscoveryClient;
import com.nesscomputing.testing.MockedService;

public class MockedDiscoveryService implements MockedService {

    private final DiscoveryClient mockedClient = new MemoryDiscoveryClient();

    @Override
    public Module getServiceModule(String serviceName) {
        return Modules.combine(getTestCaseModule(), new HttpServerAnnouncerModule());
    }

    @Override
    public Module getTestCaseModule() {

        return Modules.override(new DiscoveryClientModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind (DiscoveryClient.class).toInstance(mockedClient);
                bind (ReadOnlyDiscoveryClient.class).toInstance(mockedClient);
            }
        });
    }

    @Override
    public Map<String, String> getServiceConfigTweaks(String serviceName) {
        return getTestCaseConfigTweaks();
    }

    @Override
    public Map<String, String> getTestCaseConfigTweaks() {
        return ImmutableMap.of("ness.discovery.enabled", "true");
    }
}

package org.acme;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {

    private static WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();

        String wiremockUrl = wireMockServer.baseUrl();
        log.info("✅ WireMock started at " + wiremockUrl);
        Map<String, String> config = new HashMap<>();
        config.put("wiremock.url", wiremockUrl);

        return config;
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    public static WireMockServer getWireMockServer() {
        if (wireMockServer == null) {
            throw new IllegalStateException("❌ WireMockServer wasn't initialized!!");
        }
        return wireMockServer;
    }
}

package net.whydah.sso.helidon.internal;

import io.helidon.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void dynamicConfig() {
        // assertEquals("https://sso-mocroprofile.whydah.org/", getConfigValue("whydah_uri")); // will never happen because microprofile config is overridden
        Config configs = ConfigUtils.dynamicConfig();
        Config value = configs.get("whydah_uri");
        assertEquals("https://sso.whydah.org/", value.asString().get());

    }

    @Test
    void hasClasspathFile() {
        assertTrue(ConfigUtils.hasClasspathFile("application.yaml"));
        assertTrue(ConfigUtils.hasClasspathFile("application.properties"));
        assertTrue(ConfigUtils.hasClasspathFile("META-INF/microprofile-config.properties"));
    }

    @Test
    void hasConfigFile() {
        assertTrue(ConfigUtils.hasConfigFile("src/test/resources/application.yaml"));
    }
}
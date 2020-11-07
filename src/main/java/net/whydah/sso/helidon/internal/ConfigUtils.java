package net.whydah.sso.helidon.internal;

import io.helidon.config.Config;
import io.helidon.config.ConfigValue;
import org.slf4j.Logger;

import java.io.File;

import static io.helidon.config.ConfigSources.classpath;
import static io.helidon.config.ConfigSources.file;
import static org.slf4j.LoggerFactory.getLogger;

public class ConfigUtils {
    private static final Logger log = getLogger(ConfigUtils.class);
    public static String getConfigValue(String key) {
        Config configurations = buildConfig();
        Config parameter = configurations.get(key);
        String value = null;
        if (parameter.hasValue()) {
            ConfigValue<String> parameterString = parameter.asString();
            value = parameterString.get();
        }
        return value;
    }

    static Config buildConfig() {
        Config config = null;
        boolean hasLocalConfigFile = false;
        try {
            File f = new File("local_config.properties");
            if (f.exists()) {
                hasLocalConfigFile = true;
            }
        } catch (Exception e) {
            log.trace("local_config.properties is not found on path");
        }
        if (hasLocalConfigFile) {

            config = Config.builder()
                    .sources(
                            file("local_config.properties"),
                            classpath("application.yaml"),
                            classpath("META-INF/microprofile-config.properties")
                    )
                    .build();
        } else {
            config = Config.builder()
                    .sources(
                            classpath("application.yaml"),
                            classpath("META-INF/microprofile-config.properties")
                    )
                    .build();
        }
        return config;
    }
}

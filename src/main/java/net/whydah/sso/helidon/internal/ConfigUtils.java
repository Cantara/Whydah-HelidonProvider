package net.whydah.sso.helidon.internal;

import io.helidon.config.Config;
import io.helidon.config.ConfigValue;
import org.slf4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    static Config dynamicConfig() {
        Config.Builder configBuilder = Config.builder();

        if (hasConfigFile("local_config.properties")) {
            configBuilder.addSource(file("local_config.properties"));
        }
        if (hasClasspathFile("application.properties")) {
            configBuilder.addSource(classpath("application.properties"));
        }
        if (hasClasspathFile("application.yaml")) {
            configBuilder.addSource(classpath("application.yaml"));
        }
        if (hasClasspathFile("META-INF/microprofile-config.properties")) {
            configBuilder.addSource(classpath("META-INF/microprofile-config.properties"));
        }

        Config config = configBuilder.build();
        return config;
    }

    static boolean hasClasspathFile(String filePath) {
        boolean hasClasspathFile = false;
        try {
            URL fileResource = new ConfigUtils().getClass().getResource("/" + filePath);
            if (fileResource != null) {
                final Path file = Paths.get(fileResource.toURI());
                hasClasspathFile = Files.exists(file);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return hasClasspathFile;
    }

    static boolean hasConfigFile(String filePath) {
        boolean hasLocalConfigFile = false;
        try {
            File f = new File(filePath);
            if (f.exists()) {
                hasLocalConfigFile = true;
            }
        } catch (Exception e) {
            log.trace("local_config.properties is not found on path");
        }
        return hasLocalConfigFile;
    }
}

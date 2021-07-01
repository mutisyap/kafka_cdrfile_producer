package io.pmutisya.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class ConfigurationUtil {
    private static final String filename = "application.yml";
    private static final String configDir = "/apps/kcp/configs";

    private static final Logger logger = LogManager.getLogger(ConfigurationUtil.class);

    public static ApplicationConfiguration readConfigurations() throws IOException {
        URL fileUrl = getDefaultConfigURL().orElse(null);

        if (fileUrl == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            fileUrl = classLoader.getResource(filename);
        }

        return readConfigurationsFromYaml(fileUrl);
    }

    private static ApplicationConfiguration readConfigurationsFromYaml(URL fileUrl) throws IOException {
        if (fileUrl == null) {
            throw new FileNotFoundException("File " + filename + " Does not exist");
        }

        InputStream inputStream = fileUrl.openStream();


        Yaml yaml = new Yaml(new Constructor(ApplicationConfiguration.class));

        return yaml.load(inputStream);
    }

    private static Optional<URL> getDefaultConfigURL() {

        File etcConfigFile = new File(configDir + "/" + filename);

        if (etcConfigFile.isFile()) {
            try {
                return Optional.of(etcConfigFile.toURI().toURL());
            } catch (MalformedURLException e) {
                logger.warn("Error reading default config file", e);
            }
        }
        return Optional.empty();
    }
}

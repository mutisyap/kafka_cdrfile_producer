package io.pmutisya.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ConfigurationUtil {
    private static final String filename = "application.yml";

    public static ApplicationConfiguration readConfigurations() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL fileUrl = classLoader.getResource(filename);

        if (fileUrl == null) {
            throw new FileNotFoundException("File " + filename + " Does not exist");
        }

        InputStream inputStream = fileUrl.openStream();


        Yaml yaml = new Yaml(new Constructor(ApplicationConfiguration.class));

        return yaml.load(inputStream);
    }
}

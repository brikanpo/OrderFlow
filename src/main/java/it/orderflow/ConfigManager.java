package it.orderflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final ConfigManager instance = new ConfigManager();
    private final Properties properties = new Properties();

    private ConfigManager() {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("File config.properties not found", e);
        }
    }

    public static ConfigManager getInstance() {
        return ConfigManager.instance;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public String getProperty(String key) {
        return this.getProperties().getProperty(key);
    }
}

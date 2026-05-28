package com.periplus.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPS = new Properties();
    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .ignoreIfMalformed()
            .load();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        return PROPS.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String v = get(key);
        return (v == null || v.isBlank()) ? defaultValue : v;
    }

    public static int getInt(String key, int defaultValue) {
        String v = get(key);
        if (v == null || v.isBlank()) return defaultValue;
        return Integer.parseInt(v.trim());
    }

    public static String env(String envVar) {
        String v = DOTENV.get(envVar);
        if (v == null || v.isBlank()) {
            v = System.getenv(envVar);
        }
        return (v == null) ? "" : v;
    }
}

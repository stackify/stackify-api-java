package com.stackify.api.common.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Darin Howard
 */
@Slf4j
public class PropertyUtil {

    public static Map<String, String> readAndMerge(@NonNull final String... paths) {

        Map<String, String> mergedMap = new HashMap<String, String>();

        for (String path : paths) {
            Map<String, String> map = read(path);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!mergedMap.containsKey(entry.getKey())) {
                    mergedMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return mergedMap;
    }

    /**
     * Loads properties with given path - will load as file is able or classpath resource
     */
    public static Properties loadProperties(final String path) {

        if (path != null) {
            // try as file
            try {
                File file = new File(path);
                if (file.exists()) {
                    try {
                        Properties p = new Properties();
                        p.load(new FileInputStream(file));
                        return p;
                    } catch (Exception e) {
                        log.error("Error loading properties from file: " + path);
                    }
                }
            } catch (Throwable e) {
                log.debug(e.getMessage(), e);
            }

            // try as resource
            InputStream inputStream = null;
            try {
                inputStream = PropertyUtil.class.getResourceAsStream(path);
                if (inputStream != null) {
                    try {
                        Properties p = new Properties();
                        p.load(inputStream);
                        return p;
                    } catch (Exception e) {
                        log.error("Error loading properties from resource: " + path);
                    }
                }
            } catch (Exception e) {
                log.error("Error loading properties from resource: " + path);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable t) {
                        log.debug("Error closing: " + path, t);
                    }
                }
            }
        }


        // return empty Properties by default
        return new Properties();
    }

    /**
     * Reads properties from file path or classpath
     */
    public static Map<String, String> read(final String path) {

        Map<String, String> map = new HashMap<String, String>();

        if (path != null) {
            try {
                Properties p = loadProperties(path);

                for (Object key : p.keySet()) {

                    String value = p.getProperty(String.valueOf(key));

                    // remove single/double quotes
                    if ((value.startsWith("\"") && value.endsWith("\"")) ||
                            (value.startsWith("\'") && value.endsWith("\'"))) {
                        value = value.substring(1, value.length() - 1);
                    }

                    value = value.trim();

                    if (!value.equals("")) {
                        map.put(String.valueOf(key), value);
                    }
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        return map;
    }

}

package com.stackify.api.common.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Darin Howard
 */
@Slf4j
public class PropertyUtil {

    public static Map<String, String> readAndMerge(@NonNull String... files) {

        Map<String, String> mergedMap = new HashMap<String, String>();

        for (String file : files) {
            Map<String, String> map = read(file);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!mergedMap.containsKey(entry.getKey())) {
                    mergedMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return mergedMap;
    }

    public static Map<String, String> read(@NonNull String file) {

        Map<String, String> map = new HashMap<String, String>();

        if (new File(file).exists()) {
            try {
                Properties p = new Properties();
                p.load(new FileInputStream(new File(file)));

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
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        return map;
    }

}

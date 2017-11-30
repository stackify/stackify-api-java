package com.stackify.api.common.util;

import lombok.experimental.UtilityClass;

/**
 * Utility to append ' #SKIPJSON' to a string message if JSON is detected.
 *
 * @author Darin Howard
 */
@UtilityClass
public class SkipJsonUtil {

    public static String execute(final String source) {
        // check if message contains JSON
        if (source != null &&
                source.contains("{") &&
                source.contains("}")) {
            return source + " #SKIPJSON";
        }

        return source;
    }

}
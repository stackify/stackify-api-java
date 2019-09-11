package com.stackify.api.common.log;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.slf4j.helpers.SubstituteLogger;

@UtilityClass
public class StackTraceUtil {

    private final Class[] skipClasses = new Class[]{SubstituteLogger.class, Throwable.class};

    /**
     * Helper function get top level stack trace element from array (skips some wrapper classes)
     */
    public static StackTraceElement getStackTraceElement(final StackTraceElement[] stackTraceElements) {
        if (stackTraceElements != null) {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (stackTraceElement != null) {
                    String classname = stackTraceElement.getClassName();
                    if (!skipClass(classname)) {
                        return stackTraceElement;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Helper function get top level class name from array (skips some wrapper classes)
     */
    public static String getClassName(final StackTraceElement[] stackTraceElements) {
        StackTraceElement stackTraceElement = getStackTraceElement(stackTraceElements);

        if (stackTraceElement != null) {
            return stackTraceElement.getClassName();
        }

        return null;
    }

    /**
     * indicates if a class should be skipped (avoid using a wrapping class for log identification)
     */
    private static boolean skipClass(@NonNull final String classname) {
        for (Class skipClass : skipClasses) {
            if (skipClass.getName().equalsIgnoreCase(classname)) {
                return true;
            }
        }
        return false;
    }

}

package com.stackify.api.common.mask;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Handles masking string values based on regex matching.
 * Class is Thread Safe.
 *
 * @author Darin Howard
 */
@NoArgsConstructor
@Slf4j
public class Masker {

    public static final String MASK_CREDITCARD = "CREDITCARD";
    public static final String MASK_SSN = "SSN";
    public static final String MASK_IP = "IP";
    public static final String[] MASKS = new String[]{MASK_CREDITCARD, MASK_SSN, MASK_IP};

    protected static final String MASK_CC_VISA_REGEX = "4[0-9]{12}(?:[0-9]{3})";
    protected static final String MASK_CC_DISCOVER_REGEX = "6(?:011|5[0-9]{2})[0-9]{12}";
    protected static final String MASK_CC_MASTERCARD_REGEX = "5[1-5][0-9]{14}";
    protected static final String MASK_CC_AMEX_REGEX = "3[47][0-9]{13}";
    protected static final String MASK_CC_DINERS_REGEX = "3(?:0[0-5]|[68][0-9])?[0-9]{11}";
    protected static final String MASK_SSN_REGEX = "[0-9]{3}-[0-9]{2}-[0-9]{4}";
    protected static final String MASK_IPV4_REGEX = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    @Getter
    private final Map<String, Pattern> maskPatterns = new ConcurrentHashMap<String, Pattern>();

    public void clearMasks() {
        maskPatterns.clear();
    }

    private final Object lock = new Object();

    public void removeMask(@NonNull final String mask) {
        synchronized (lock) {
            if (mask.equals(MASK_CREDITCARD)) {
                removeMaskPattern(MASK_CC_VISA_REGEX);
                removeMaskPattern(MASK_CC_DISCOVER_REGEX);
                removeMaskPattern(MASK_CC_MASTERCARD_REGEX);
                removeMaskPattern(MASK_CC_AMEX_REGEX);
                removeMaskPattern(MASK_CC_DINERS_REGEX);
                return;
            }

            if (mask.equals(MASK_SSN)) {
                removeMaskPattern(MASK_SSN_REGEX);
                return;
            }

            if (mask.equals(MASK_IP)) {
                removeMaskPattern(MASK_IPV4_REGEX);
                return;
            }

            removeMaskPattern(mask);
        }
    }

    public void addMask(@NonNull final String mask) {
        synchronized (lock) {
            if (mask.equals(MASK_CREDITCARD)) {
                addMaskPattern(MASK_CC_VISA_REGEX);
                addMaskPattern(MASK_CC_DISCOVER_REGEX);
                addMaskPattern(MASK_CC_MASTERCARD_REGEX);
                addMaskPattern(MASK_CC_AMEX_REGEX);
                addMaskPattern(MASK_CC_DINERS_REGEX);
                return;
            }

            if (mask.equals(MASK_SSN)) {
                addMaskPattern(MASK_SSN_REGEX);
                return;
            }

            if (mask.equals(MASK_IP)) {
                addMaskPattern(MASK_IPV4_REGEX);
                return;
            }

            addMaskPattern(mask);
        }
    }

    private synchronized void addMaskPattern(final String regex) {
        if (regex != null) {
            try {
                maskPatterns.put(regex, Pattern.compile(regex));
            } catch (PatternSyntaxException e) {
                log.error(String.format("Error Adding Mask: %s: '%s'", e.getMessage(), regex));
            }
        }
    }

    private synchronized void removeMaskPattern(final String regex) {
        if (regex != null) {
            try {
                maskPatterns.remove(regex);
            } catch (PatternSyntaxException e) {
                log.error(String.format("%s: '%s'", e.getMessage(), regex));
            }
        }
    }

    /**
     * Returns string with '*' replacing any value matching a configured mask.
     */
    public String mask(final String value) {

        try {

            if (hasMasks()) {

                if (value == null) return null;

                String maskedValue = value;

                for (Map.Entry<String, Pattern> entry : maskPatterns.entrySet()) {
                    Matcher matcher = entry.getValue().matcher(maskedValue);
                    while (matcher.find()) {
                        String match = matcher.group();
                        char[] symbols = new char[match.length()];
                        Arrays.fill(symbols, '*');
                        maskedValue = maskedValue.replace(match, new String(symbols));
                    }
                }

                return maskedValue;
            }

        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }

        return value;
    }

    public boolean hasMasks() {
        return maskPatterns.size() > 0;
    }

}

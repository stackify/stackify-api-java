package com.stackify.api.common.mask;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Darin Howard
 */
@Slf4j
public class MaskerTest {

    @Test
    public void maskCreditCards() {

        Masker masker = new Masker();
        masker.addMask(Masker.MASK_CREDITCARD);
        Assert.assertEquals("Visa Valid", "****************", masker.mask("4111111111111111"));
        Assert.assertEquals("Visa Valid Mixed", "****************2", masker.mask("41111111111111112"));
        Assert.assertEquals("Visa Invalid", "411111111111111", masker.mask("411111111111111"));

        masker = new Masker();
        masker.addMask(Masker.MASK_CC_DISCOVER_REGEX);
        Assert.assertEquals("Discover Valid", "****************", masker.mask("6011000000000004"));
        Assert.assertEquals("Discover Valid Mixed", "****************2", masker.mask("60110000000000042"));
        Assert.assertEquals("Discover Invalid", "601100000000000", masker.mask("601100000000000"));

        masker = new Masker();
        masker.addMask(Masker.MASK_CC_MASTERCARD_REGEX);
        Assert.assertEquals("Mastercard Valid", "****************", masker.mask("5105105105105100"));
        Assert.assertEquals("Mastercard Valid Mixed", "****************2", masker.mask("51051051051051002"));
        Assert.assertEquals("Mastercard Invalid", "510510510510510", masker.mask("510510510510510"));

        masker = new Masker();
        masker.addMask(Masker.MASK_CC_AMEX_REGEX);
        Assert.assertEquals("Amex Valid", "***************", masker.mask("371449635398431"));
        Assert.assertEquals("Amex Valid Mixed", "***************2", masker.mask("3714496353984312"));
        Assert.assertEquals("Amex Invalid", "37144963539", masker.mask("37144963539"));

        masker = new Masker();
        masker.addMask(Masker.MASK_CC_DINERS_REGEX);
        Assert.assertEquals("Diners Valid", "**************", masker.mask("38520000023237"));
        Assert.assertEquals("Diners Valid Mixed", "**************2", masker.mask("385200000232372"));
        Assert.assertEquals("Diners Invalid", "38520000023", masker.mask("38520000023"));
    }

    @Test
    public void maskSSN() {

        Masker masker = new Masker();
        masker.addMask(Masker.MASK_SSN);
        Assert.assertEquals("SSN Valid", "***********", masker.mask("555-44-3333"));
        Assert.assertEquals("SSN Invalid", "55-44-3333", masker.mask("55-44-3333"));
        Assert.assertEquals("SSN Invalid (no dashes)", "555443333", masker.mask("555443333"));
    }

    @Test
    public void maskIPV4() {

        Masker masker = new Masker();
        masker.addMask(Masker.MASK_IPV4_REGEX);

        Assert.assertEquals("IPV4 Valid Max", "***************", masker.mask("255.255.255.255"));
        Assert.assertEquals("IPV4 Valid Min", "*******", masker.mask("1.1.1.1"));
        Assert.assertEquals("IPV4 Valid Mixed", "************", masker.mask("133.51.6.233"));
        Assert.assertEquals("IPV4 Valid Multiple", "a ************ b ******* c", masker.mask("a 133.51.6.233 b 1.1.1.1 c"));

        Assert.assertEquals("IPV4 Partial Match", "2********", masker.mask("256.1.1.1"));
        Assert.assertEquals("IPV4 Partial Match", "********6", masker.mask("1.1.1.256"));

        Assert.assertEquals("IPV4 No Match", "255.446.45.256", masker.mask("255.446.45.256"));
        Assert.assertEquals("IPV4 No Match", "3a3.44b.45.256", masker.mask("3a3.44b.45.256"));
        Assert.assertEquals("IPV4 Multiple No Match", "a 3a3.44b.45.256 b 452.33.25 c", masker.mask("a 3a3.44b.45.256 b 452.33.25 c"));
    }


    @Test
    public void maskPerformance() {

        int count = 1000000;

        {
            Masker masker = new Masker();
            masker.removeMask(Masker.MASK_CREDITCARD);
            masker.removeMask(Masker.MASK_SSN);
            masker.removeMask(Masker.MASK_IP);
            long start = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                masker.mask("This has a single match 502-34-2355.");
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("Processed " + count + " lines with NO masking matches in " + elapsed + "ms");
        }

        {
            Masker masker = new Masker();
            long start = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                masker.mask("This has a single match 502-34-2355.");
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("Processed " + count + " lines with 1 masking matches in " + elapsed + "ms");
        }
    }

}

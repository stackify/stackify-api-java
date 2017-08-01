package com.stackify.api.common.mask;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Darin Howard
 */
public class MaskerConfigurationTest {

    @Test
    public void testFromProperties() {
        Masker masker = MaskerConfiguration.fromProperties();
        Assert.assertNotNull(masker);
        Assert.assertEquals(8, masker.getMaskPatterns().size());
        Assert.assertTrue(masker.hasMasks());
    }

}

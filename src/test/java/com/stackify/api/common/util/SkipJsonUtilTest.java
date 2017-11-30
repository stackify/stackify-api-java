package com.stackify.api.common.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Darin Howard
 */
public class SkipJsonUtilTest {

    @Test
    public void execute() {
        Assert.assertEquals("No JSON", "TEST", SkipJsonUtil.execute("TEST"));
        Assert.assertEquals("Partial JSON", "JSON {", SkipJsonUtil.execute("JSON {"));
        Assert.assertEquals("Partial JSON", "JSON }", SkipJsonUtil.execute("JSON }"));
        Assert.assertEquals("Complete JSON", "JSON {} #SKIPJSON", SkipJsonUtil.execute("JSON {}"));
    }
}
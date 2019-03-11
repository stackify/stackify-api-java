package com.stackify.api.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Map;

public class PropertyUtilTest {

    @Test
    public void readResource() {

        // test via resource success
        Map<String, String> mapSuccess = PropertyUtil.read("/stackify-api.properties");
        Assert.assertEquals("url", mapSuccess.get("stackify.apiUrl"));
        Assert.assertEquals("key", mapSuccess.get("stackify.apiKey"));
        Assert.assertEquals("app", mapSuccess.get("stackify.application"));
        Assert.assertEquals("env", mapSuccess.get("stackify.environment"));

        // test via resource failure
        Map<String, String> mapFailure = PropertyUtil.read("/stackify-api-failure.properties");
        Assert.assertEquals(0, mapFailure.size());
    }

    @Test
    public void readFile() throws Exception {

        // test via file success
        Map<String, String> mapSuccess = PropertyUtil.read(new File(PropertyUtilTest.class.getResource("/stackify-api.properties").toURI().getPath()).getPath());
        Assert.assertEquals("url", mapSuccess.get("stackify.apiUrl"));
        Assert.assertEquals("key", mapSuccess.get("stackify.apiKey"));
        Assert.assertEquals("app", mapSuccess.get("stackify.application"));
        Assert.assertEquals("env", mapSuccess.get("stackify.environment"));

        // test via file failure
        Map<String, String> mapFailure = PropertyUtil.read("/root/file/does/not/exist");
        Assert.assertEquals(0, mapFailure.size());
    }

    @Test
    public void readNull() throws Exception {
        Map<String, String> mapFailure = PropertyUtil.read(null);
        Assert.assertEquals(0, mapFailure.size());
    }
}

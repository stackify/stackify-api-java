/*
 * AppIdentityTest.java
 * Copyright 2014 Stackify
 */
package com.stackify.api;

import org.junit.Assert;
import org.junit.Test;

/**
 * AppIdentity JUnit Test
 * @author Eric Martin
 */
public class AppIdentityTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		Integer deviceId = Integer.valueOf(14);
		Integer deviceAppId = Integer.valueOf(22);
		String appNameId = "appNameId";
		Integer envId = Integer.valueOf(8);
		String env = "env";
		String appName = "appName";
		String appEnvId = "appEnvId";
		String deviceAlias = "deviceAlias";
		
		AppIdentity.Builder builder = AppIdentity.newBuilder();
		builder.deviceId(deviceId);
		builder.deviceAppId(deviceAppId);
		builder.appNameId(appNameId);
		builder.envId(envId);
		builder.env(env);
		builder.appName(appName);
		builder.appEnvId(appEnvId);
		builder.deviceAlias(deviceAlias);
		
		AppIdentity appIdentity = builder.build();
		
		Assert.assertNotNull(appIdentity);
		
		Assert.assertEquals(deviceId, appIdentity.getDeviceId());
		Assert.assertEquals(deviceAppId, appIdentity.getDeviceAppId());
		Assert.assertEquals(appNameId, appIdentity.getAppNameId());
		Assert.assertEquals(envId, appIdentity.getEnvId());
		Assert.assertEquals(env, appIdentity.getEnv());
		Assert.assertEquals(appName, appIdentity.getAppName());
		Assert.assertEquals(appEnvId, appIdentity.getAppEnvId());
		Assert.assertEquals(deviceAlias, appIdentity.getDeviceAlias());
		
		Assert.assertNotNull(appIdentity.toString());
	}
}

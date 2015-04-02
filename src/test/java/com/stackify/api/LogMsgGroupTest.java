/*
 * Copyright 2014 Stackify
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stackify.api;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * LogMsgGroup JUnit Test
 * @author Eric Martin
 */
public class LogMsgGroupTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		Integer cdId = Integer.valueOf(14);
		Integer cdAppId = Integer.valueOf(15);
		String appNameId = "appNameId";
		String appEnvId = "appEnvId";
		Integer envId = Integer.valueOf(16);
		String env = "env";
		String serverName = "serverName";
		String appName = "appName";
		String appLoc = "appLoc";
		String logger = "logger";
		String platform = "platform";
		List<LogMsg> msgs = Mockito.mock(List.class);

		LogMsgGroup.Builder builder = LogMsgGroup.newBuilder();
		builder.cdId(cdId);
		builder.cdAppId(cdAppId);
		builder.appNameId(appNameId);
		builder.appEnvId(appEnvId);
		builder.envId(envId);
		builder.env(env);
		builder.serverName(serverName);
		builder.appName(appName);
		builder.appLoc(appLoc);
		builder.logger(logger);
		builder.platform(platform);
		builder.msgs(msgs);

		LogMsgGroup logMsgGroup = builder.build();

		Assert.assertNotNull(logMsgGroup);

		Assert.assertEquals(cdId, logMsgGroup.getCdId());
		Assert.assertEquals(cdAppId, logMsgGroup.getCdAppId());
		Assert.assertEquals(appNameId, logMsgGroup.getAppNameId());
		Assert.assertEquals(appEnvId, logMsgGroup.getAppEnvId());
		Assert.assertEquals(envId, logMsgGroup.getEnvId());
		Assert.assertEquals(env, logMsgGroup.getEnv());
		Assert.assertEquals(serverName, logMsgGroup.getServerName());
		Assert.assertEquals(appName, logMsgGroup.getAppName());
		Assert.assertEquals(appLoc, logMsgGroup.getAppLoc());
		Assert.assertEquals(logger, logMsgGroup.getLogger());
		Assert.assertEquals(platform, logMsgGroup.getPlatform());
		Assert.assertEquals(msgs, logMsgGroup.getMsgs());
		
		LogMsgGroup logMsgGroupCopy = logMsgGroup.toBuilder().build();
		
		Assert.assertNotNull(logMsgGroupCopy);

		Assert.assertEquals(cdId, logMsgGroupCopy.getCdId());
		Assert.assertEquals(cdAppId, logMsgGroupCopy.getCdAppId());
		Assert.assertEquals(appNameId, logMsgGroupCopy.getAppNameId());
		Assert.assertEquals(appEnvId, logMsgGroupCopy.getAppEnvId());
		Assert.assertEquals(envId, logMsgGroupCopy.getEnvId());
		Assert.assertEquals(env, logMsgGroupCopy.getEnv());
		Assert.assertEquals(serverName, logMsgGroupCopy.getServerName());
		Assert.assertEquals(appName, logMsgGroupCopy.getAppName());
		Assert.assertEquals(appLoc, logMsgGroupCopy.getAppLoc());
		Assert.assertEquals(logger, logMsgGroupCopy.getLogger());
		Assert.assertEquals(platform, logMsgGroupCopy.getPlatform());
		Assert.assertEquals(msgs, logMsgGroupCopy.getMsgs());
	}
}

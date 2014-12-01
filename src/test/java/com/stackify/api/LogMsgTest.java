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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * LogMsg JUnit Test
 * @author Eric Martin
 */
public class LogMsgTest {

	/**
	 * testBuilder
	 */
	@Test
	public void testBuilder() {
		String msg = "msg";
		String data = "data";
		StackifyError ex = Mockito.mock(StackifyError.class);
		String th = "th";
		Long epochMs = Long.valueOf(System.currentTimeMillis());
		String level = "level";
		String transId = "transId";
		String srcMethod = "srcMethod";
		Integer srcLine = Integer.valueOf(14);

		LogMsg.Builder builder = LogMsg.newBuilder();
		builder.msg(msg);
		builder.data(data);
		builder.ex(ex);
		builder.th(th);
		builder.epochMs(epochMs);
		builder.level(level);
		builder.transId(transId);
		builder.srcMethod(srcMethod);
		builder.srcLine(srcLine);

		LogMsg logMsg = builder.build();

		Assert.assertNotNull(logMsg);

		Assert.assertEquals(msg, logMsg.getMsg());
		Assert.assertEquals(data, logMsg.getData());
		Assert.assertEquals(ex, logMsg.getEx());
		Assert.assertEquals(th, logMsg.getTh());
		Assert.assertEquals(epochMs, logMsg.getEpochMs());
		Assert.assertEquals(level, logMsg.getLevel());
		Assert.assertEquals(transId, logMsg.getTransId());
		Assert.assertEquals(srcMethod, logMsg.getSrcMethod());
		Assert.assertEquals(srcLine, logMsg.getSrcLine());
	}
}

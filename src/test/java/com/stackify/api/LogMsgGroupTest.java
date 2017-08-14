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

		List<LogMsg> msgs = Mockito.mock(List.class);

		LogMsgGroup.Builder builder = LogMsgGroup.newBuilder();
		builder.msgs(msgs);

		LogMsgGroup logMsgGroup = builder.build();

		Assert.assertNotNull(logMsgGroup);

		Assert.assertEquals(msgs, logMsgGroup.getMsgs());
		
		LogMsgGroup logMsgGroupCopy = logMsgGroup.toBuilder().build();
		
		Assert.assertNotNull(logMsgGroupCopy);

		Assert.assertEquals(msgs, logMsgGroupCopy.getMsgs());
	}
}

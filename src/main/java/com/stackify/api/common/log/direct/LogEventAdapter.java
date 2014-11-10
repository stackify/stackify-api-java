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
package com.stackify.api.common.log.direct;

import java.util.Date;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.LogMsg;
import com.stackify.api.StackifyError;
import com.stackify.api.common.lang.Throwables;
import com.stackify.api.common.log.EventAdapter;

/**
 * LogEvent
 * @author Eric Martin
 */
public class LogEventAdapter implements EventAdapter<LogEvent>  {

	/**
	 * Environment detail
	 */
	private final EnvironmentDetail envDetail;
		
	/**
	 * Constructor
	 * @param envDetail Environment detail
	 */
	public LogEventAdapter(final EnvironmentDetail envDetail) {
		Preconditions.checkNotNull(envDetail);
		this.envDetail = envDetail;
	}
	
	/**
	 * @see com.stackify.api.common.log.EventAdapter#getThrowable(java.lang.Object)
	 */
	@Override
	public Optional<Throwable> getThrowable(final LogEvent event) {
		return Optional.fromNullable(event.getException());
	}

	/**
	 * @see com.stackify.api.common.log.EventAdapter#getStackifyError(java.lang.Object, java.lang.Throwable)
	 */
	@Override
	public StackifyError getStackifyError(final LogEvent event, final Throwable exception) {
		StackifyError.Builder builder = StackifyError.newBuilder();
		builder.environmentDetail(envDetail);		
		builder.occurredEpochMillis(new Date(event.getTimestamp()));
		
		if (exception != null) {
			builder.error(Throwables.toErrorItem(event.getMessage(), exception));
		} else {
			builder.error(Throwables.toErrorItem(event.getMessage(), event.getClassName(), event.getMethodName(), event.getLineNumber()));
		}
		
		builder.serverVariables(Maps.fromProperties(System.getProperties()));
		
		return builder.build();
	}

	/**
	 * @see com.stackify.api.common.log.EventAdapter#getLogMsg(java.lang.Object, com.google.common.base.Optional)
	 */
	@Override
	public LogMsg getLogMsg(final LogEvent event, final Optional<StackifyError> error) {
		LogMsg.Builder builder = LogMsg.newBuilder();
		builder.msg(event.getMessage());
		builder.ex(error.orNull());
		builder.epochMs(event.getTimestamp());
		
		if (event.getLevel() != null) {
			builder.level(event.getLevel().toLowerCase());
		}
						
		return builder.build();
	}

	/**
	 * @see com.stackify.api.common.log.EventAdapter#isErrorLevel(java.lang.Object)
	 */
	@Override
	public boolean isErrorLevel(final LogEvent event) {
		if (event.getLevel() != null) {
			return "ERROR".equals(event.getLevel().toUpperCase());
		}
		
		return false;
	}
}

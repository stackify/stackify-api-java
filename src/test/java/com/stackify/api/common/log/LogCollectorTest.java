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
package com.stackify.api.common.log;

import com.stackify.api.EnvironmentDetail;
import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.ApiConfiguration;
import com.stackify.api.common.http.HttpException;
import com.stackify.api.common.oauth.OAuth2Service;
import com.stackify.api.common.oauth.OAuth2Token;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

/**
 * LogCollector JUnit Test
 *
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OAuth2Service.class, LogCollector.class})
public class LogCollectorTest {

    @Test
    public void testFlushEmpty() throws IOException, HttpException {
        LogSender sender = Mockito.mock(LogSender.class);
        LogCollector collector = new LogCollector("logger", Mockito.mock(ApiConfiguration.class), Mockito.mock(OAuth2Service.class));

        collector.flush(sender);

        Mockito.verifyZeroInteractions(sender);
    }

    @Test
    public void testAddAndFlushWithoutAppIdentity() throws IOException, HttpException {
        LogSender sender = Mockito.mock(LogSender.class);

        OAuth2Service oAuth2Service = Mockito.mock(OAuth2Service.class);
        Mockito.when(oAuth2Service.getAccessToken((EnvironmentDetail) Mockito.any())).thenReturn(Mockito.mock(OAuth2Token.class));

        LogCollector collector = new LogCollector("logger", Mockito.mock(ApiConfiguration.class), oAuth2Service);

        collector.addLogMsg(Mockito.mock(LogMsg.class));
        collector.addLogMsg(Mockito.mock(LogMsg.class));
        collector.addLogMsg(Mockito.mock(LogMsg.class));

        collector.flush(sender);

        Mockito.verify(sender).send(Mockito.any(LogMsgGroup.class));
    }

    @Test
    public void testAddAndFlushWithAppIdentity() throws IOException, HttpException {
        LogSender sender = Mockito.mock(LogSender.class);

        OAuth2Service oAuth2Service = Mockito.mock(OAuth2Service.class);
        Mockito.when(oAuth2Service.getAccessToken((EnvironmentDetail) Mockito.any())).thenReturn(Mockito.mock(OAuth2Token.class));

        LogCollector collector = new LogCollector("logger", Mockito.mock(ApiConfiguration.class), oAuth2Service);

        collector.addLogMsg(Mockito.mock(LogMsg.class));
        collector.addLogMsg(Mockito.mock(LogMsg.class));
        collector.addLogMsg(Mockito.mock(LogMsg.class));

        collector.flush(sender);

        Mockito.verify(sender).send(Mockito.any(LogMsgGroup.class));
    }

    @Test
    public void testOffer() {

        LogCollector collector = new LogCollector("logger", Mockito.mock(ApiConfiguration.class), Mockito.mock(OAuth2Service.class));
        Assert.assertEquals(0, collector.retryQueue.size());

        LogMsgGroup logMsgGroup = Mockito.mock(LogMsgGroup.class);
        collector.retryQueue.offer(new LogCollector.RetryQueueItem<LogMsgGroup>(logMsgGroup));
        Assert.assertEquals(1, collector.retryQueue.size());
    }

    @Test
    public void testDrain() throws Exception {

        LogMsgGroup logMsgGroup1 = Mockito.mock(LogMsgGroup.class);
        LogMsgGroup logMsgGroup2 = Mockito.mock(LogMsgGroup.class);

        OAuth2Service oAuth2Service = Mockito.mock(OAuth2Service.class);
        Mockito.when(oAuth2Service.getAccessToken((EnvironmentDetail) Mockito.any())).thenReturn(Mockito.mock(OAuth2Token.class));

        LogCollector collector = new LogCollector("logger", Mockito.mock(ApiConfiguration.class), oAuth2Service);
        collector.retryQueue.offer(new LogCollector.RetryQueueItem<LogMsgGroup>(logMsgGroup1));
        collector.retryQueue.offer(new LogCollector.RetryQueueItem<LogMsgGroup>(logMsgGroup2));

        Assert.assertEquals(2, collector.retryQueue.size());

        LogSender logSender = Mockito.mock(LogSender.class);
        collector.flushRetries(logSender);

        Assert.assertEquals(0, collector.retryQueue.size());
    }

    @Test
    public void testDrainWithException() throws Exception {

        LogMsgGroup logMsgGroup1 = Mockito.mock(LogMsgGroup.class);
        LogMsgGroup logMsgGroup2 = Mockito.mock(LogMsgGroup.class);

        OAuth2Service oAuth2Service = Mockito.mock(OAuth2Service.class);
        Mockito.when(oAuth2Service.getAccessToken((EnvironmentDetail) Mockito.any())).thenReturn(Mockito.mock(OAuth2Token.class));

        LogCollector collector = new LogCollector("logger", Mockito.mock(ApiConfiguration.class), oAuth2Service);
        collector.retryQueue.offer(new LogCollector.RetryQueueItem<LogMsgGroup>(logMsgGroup1));
        collector.retryQueue.offer(new LogCollector.RetryQueueItem<LogMsgGroup>(logMsgGroup2));

        Assert.assertEquals(2, collector.retryQueue.size());

        LogSender logSender = Mockito.mock(LogSender.class);
        Mockito.when(logSender.send(logMsgGroup2)).thenThrow(new RuntimeException()).thenReturn(false);

        collector.flushRetries(logSender);

        Assert.assertEquals(1, collector.retryQueue.size());

        collector.flushRetries(logSender);

        Assert.assertEquals(0, collector.retryQueue.size());
    }

    @Test
    public void testDrainWithExceptionAndSkip() throws Exception {

        LogMsgGroup logMsgGroup1 = Mockito.mock(LogMsgGroup.class);

        OAuth2Service oAuth2Service = Mockito.mock(OAuth2Service.class);
        Mockito.when(oAuth2Service.getAccessToken((EnvironmentDetail) Mockito.any())).thenReturn(Mockito.mock(OAuth2Token.class));

        LogCollector collector = new LogCollector("logger", Mockito.mock(ApiConfiguration.class), oAuth2Service);
        collector.retryQueue.offer(new LogCollector.RetryQueueItem<LogMsgGroup>(logMsgGroup1));

        Assert.assertEquals(1, collector.retryQueue.size());

        LogSender logSender = Mockito.mock(LogSender.class);
        Mockito.when(logSender.send(logMsgGroup1)).thenThrow(new RuntimeException()).thenReturn(false);

        collector.flushRetries(logSender);

        Assert.assertEquals(1, collector.retryQueue.size());

        collector.flushRetries(logSender);

        Assert.assertEquals(0, collector.retryQueue.size());
    }

}

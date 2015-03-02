/*
 * Copyright 2015 Stackify
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
package com.stackify.api.common.http;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * HttpProxy JUnit Test
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpProxy.class})
public class HttpProxyTest {

	/**
	 * testWithoutProxy
	 */
	@Test
	public void testWithoutProxy() {
		Proxy proxy = HttpProxy.fromSystemProperties();
		Assert.assertNotNull(proxy);
		Assert.assertEquals(Proxy.NO_PROXY, proxy);
	}
	
	/**
	 * testWithProxy
	 */
	@Test
	public void testWithProxy() {
		String proxyHost = "test.proxy";
		String proxyPort = "8080";

		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getProperty("https.proxyHost")).thenReturn(proxyHost);
		PowerMockito.when(System.getProperty("https.proxyPort")).thenReturn(proxyPort);

		Proxy proxy = HttpProxy.fromSystemProperties();
		Assert.assertNotNull(proxy);
		
		Assert.assertEquals(Proxy.Type.HTTP, proxy.type());
		
		InetSocketAddress address = (InetSocketAddress) proxy.address();
		Assert.assertNotNull(address);
		Assert.assertEquals(address.getHostName(), proxyHost);
		Assert.assertEquals(address.getPort(), Integer.parseInt(proxyPort));
	}
	
	/**
	 * testWithNullProxyHost
	 */
	@Test
	public void testWithNullProxyHost() {
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getProperty("https.proxyHost")).thenReturn(null);
		PowerMockito.when(System.getProperty("https.proxyPort")).thenReturn("8080");
		
		Proxy proxy = HttpProxy.fromSystemProperties();
		Assert.assertNotNull(proxy);
		Assert.assertEquals(Proxy.NO_PROXY, proxy);
	}

	/**
	 * testWithEmptyProxyHost
	 */
	@Test
	public void testWithEmptyProxyHost() {
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getProperty("https.proxyHost")).thenReturn("");
		PowerMockito.when(System.getProperty("https.proxyPort")).thenReturn("8080");
		
		Proxy proxy = HttpProxy.fromSystemProperties();
		Assert.assertNotNull(proxy);
		Assert.assertEquals(Proxy.NO_PROXY, proxy);
	}
	
	/**
	 * testWithNullProxyPort
	 */
	@Test
	public void testWithNullProxyPort() {
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getProperty("https.proxyHost")).thenReturn("test.proxy");
		PowerMockito.when(System.getProperty("https.proxyPort")).thenReturn(null);
		
		Proxy proxy = HttpProxy.fromSystemProperties();
		Assert.assertNotNull(proxy);
		Assert.assertEquals(Proxy.NO_PROXY, proxy);
	}

	/**
	 * testWithEmptyProxyPort
	 */
	@Test
	public void testWithEmptyProxyPort() {
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getProperty("https.proxyHost")).thenReturn("test.proxy");
		PowerMockito.when(System.getProperty("https.proxyPort")).thenReturn("");
		
		Proxy proxy = HttpProxy.fromSystemProperties();
		Assert.assertNotNull(proxy);
		Assert.assertEquals(Proxy.NO_PROXY, proxy);
	}

	/**
	 * testWithNonNumericProxyPort
	 */
	@Test
	public void testWithNonNumericProxyPort() {
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getProperty("https.proxyHost")).thenReturn("test.proxy");
		PowerMockito.when(System.getProperty("https.proxyPort")).thenReturn("eightyeighty");
		
		Proxy proxy = HttpProxy.fromSystemProperties();
		Assert.assertNotNull(proxy);
		Assert.assertEquals(Proxy.NO_PROXY, proxy);
	}
}

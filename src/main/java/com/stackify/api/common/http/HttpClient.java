/*
 * HttpClient.java
 * Copyright 2014 Stackify
 */
package com.stackify.api.common.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.stackify.api.common.ApiConfiguration;

/**
 * HttpClient
 * @author Eric Martin
 */
public class HttpClient {

	/**
	 * CONNECT_TIMEOUT
	 */
	private static final int CONNECT_TIMEOUT = 5000;
	
	/**
	 * READ_TIMEOUT
	 */
	private static final int READ_TIMEOUT = 5000;
	
	/**
	 * API configuration
	 */
	private final ApiConfiguration apiConfig;
	
	/**
	 * Constructor
	 * @param config API configuration
	 */
	public HttpClient(final ApiConfiguration apiConfig) {
		Preconditions.checkNotNull(apiConfig);
		this.apiConfig = apiConfig;
	}
	
	/**
	 * Posts data to stackify
	 * @param path REST path
	 * @param jsonBytes JSON bytes
	 * @return Response string
	 * @throws IOException
	 * @throws HttpException
	 */
	public String post(final String path, final byte[] jsonBytes) throws IOException, HttpException {
		return post(path, jsonBytes, false);
	}
	
	/**
	 * Posts data to stackify
	 * @param path REST path
	 * @param jsonBytes JSON bytes
	 * @param gzip True if the post should be gzipped, false otherwise
	 * @return Response string
	 * @throws IOException
	 * @throws HttpException
	 */
	public String post(final String path, final byte[] jsonBytes, final boolean gzip) throws IOException, HttpException {
		Preconditions.checkNotNull(path);
		Preconditions.checkArgument(!path.isEmpty());
		Preconditions.checkNotNull(jsonBytes);
		Preconditions.checkArgument(0 < jsonBytes.length);
		
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(apiConfig.getApiUrl() + path);
			
			// request properties
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			
			if (gzip) {
				connection.setRequestProperty("Content-Encoding", "gzip");
			}
			
			connection.setRequestProperty("X-Stackify-Key", apiConfig.getApiKey());
			connection.setRequestProperty("X-Stackify-PV", "V1");
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			
			// write the post body
			
			OutputStream stream = null;
			
			if (gzip) {
				stream = new BufferedOutputStream(new GZIPOutputStream(connection.getOutputStream()));
			} else {
				stream = new BufferedOutputStream(connection.getOutputStream());
			}
						
			stream.write(jsonBytes);
			stream.flush();
			stream.close();
			
			// read the response
			
			int statusCode = connection.getResponseCode();
			
			if (statusCode != HttpURLConnection.HTTP_OK) {
				throw new HttpException(statusCode);
			}
			
			return readAndClose(connection.getInputStream());
			
		} finally {
			
			if (connection != null) {
				
				// read and close the input stream
				
				try {
					readAndClose(connection.getInputStream());
				} catch (Throwable t) {
					// do nothing
				}
				
				// read and close the error stream
				
				try {
					readAndClose(connection.getErrorStream());
				} catch (Throwable t) {
					// do nothing
				}
			}			
		}
	}
	
	/**
	 * Reads all remaining contents from the stream and closes it
	 * @param stream The stream
	 * @return THe contents of the stream
	 * @throws IOException
	 */
	private String readAndClose(final InputStream stream) throws IOException {
		Preconditions.checkNotNull(stream);
		
		String contents = CharStreams.toString(new InputStreamReader(new BufferedInputStream(stream), "UTF-8"));
		stream.close();
		
		return contents;
	}
}

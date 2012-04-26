package com.jonmcewen.apps.websize.internal;

import java.net.URL;

/**
 * Represents the result of a web size request.
 * Includes toString implementation that formats the result for output
 *
 */
public class WebSizeResult {
	
	private final URL url;
	
	private final Long sizeInBytes;

	private long size;
	
	/**
	 * Static helper to create a new WebSizeResult with unknown size
	 * @param url
	 * @return a new WebSizeResult
	 */
	public static WebSizeResult newUnknownSizeResult(URL url){
		return null;
	}
	
	/**
	 * @param url
	 * @param size in Bytes
	 */
	public WebSizeResult(URL url, long size){
		this.sizeInBytes = size;
		this.url = url;
	}
	
	@Override
	public String toString(){
		return null;
	}
	

}

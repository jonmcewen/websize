package com.jonmcewen.apps.websize.internal;

import java.util.List;

/**
 * Service to find the size of web pages. Does not follow links.
 * 
 */
public class WebSizeService {
	
	private long timeoutMs;

	/**
	 * Given a list of addresses, performs an HTTP GET on each address in
	 * parallel and returns a list of WebSizeResult objects.
	 * 
	 * @param addresses
	 *            A list of web addresses, e.g. www.google.com
	 * @return a list of WebSizeResult objects
	 */
	public List<WebSizeResult> measurePageSizes(List<String> addresses) {
		return null;
	}

}

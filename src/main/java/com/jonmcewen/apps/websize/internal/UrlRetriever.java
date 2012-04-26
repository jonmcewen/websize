package com.jonmcewen.apps.websize.internal;

import java.io.InputStream;
import java.net.URL;

/**
 * 
 * Retrieves data specified by a URL. Implementations may only support certain
 * schemes.
 */
public interface UrlRetriever {

	/**
	 * Returns an input stream to the data specified by a URL
	 * @param url
	 * @return an InputStream
	 */
	public InputStream getUrlStream(URL url) throws UrlRetrievalException;
	
}

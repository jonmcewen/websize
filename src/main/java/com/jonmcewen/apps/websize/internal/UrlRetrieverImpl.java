package com.jonmcewen.apps.websize.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation that uses URLConnection to retrieve the data
 * 
 */
public class UrlRetrieverImpl implements UrlRetriever {
	// TODO rename to HttpRetriever as doesn't work for other protocols
	private static final Logger logger = LoggerFactory
			.getLogger(UrlRetrieverImpl.class);

	@Override
	public InputStream getUrlStream(URL url) throws UrlRetrievalException {
		// Create a URLConnection object for a URL
		URLConnection conn;
		Object content;
		try {
			conn = url.openConnection();
			content = conn.getContent();
			// check for OK
			if (conn instanceof HttpURLConnection) {
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				if (httpConn.getResponseCode() != 200) {
					throw new HttpRetrievalException(url,
							httpConn.getResponseMessage());
				}
			}
		} catch (IOException e) {
			throw new UrlRetrievalException(e);
		}

		// the content should be a stream
		if (content instanceof InputStream) {
			return (InputStream) content;
		} else {
			throw new UrlRetrievalException(
					"Could not get InputStream for URL; got " + content);
		}

	}

}

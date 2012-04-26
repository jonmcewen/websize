package com.jonmcewen.apps.websize.internal;

import java.net.URL;

public class HttpRetrievalException extends UrlRetrievalException {

	public HttpRetrievalException(URL url, String responseMessage) {
		super("Could not retrieve from " + url + " because: " + responseMessage);
	}

}

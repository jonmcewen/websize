package com.jonmcewen.apps.websize.internal;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

/**
 * We should start up a web server using Jetty or similar, to have complete
 * control of the result, but for now we'll rely on the internet (the tests will
 * break if the behaviour of the chosen sites changes!)
 * 
 */
public class UrlRetrieverImplTest {

	@Test
	public final void testGetUrlStream() throws MalformedURLException,
			URISyntaxException, UrlRetrievalException {
		URL url = new URI("http://nothere.com").toURL();
		UrlRetriever retriever = new UrlRetrieverImpl();
		InputStream stream = retriever.getUrlStream(url);
		assertNotNull("Null stream returned!", stream);
	}

	@Test
	public final void testGetUrlStreamNoOK() throws MalformedURLException,
			URISyntaxException {
		URL url = new URI("http://google.com/notapage").toURL();
		UrlRetriever retriever = new UrlRetrieverImpl();
		try {
			InputStream stream = retriever.getUrlStream(url);
			fail("Should have thrown exception as not a 200 OK result");
		} catch (UrlRetrievalException e) {
			// pass
		}
	}

}

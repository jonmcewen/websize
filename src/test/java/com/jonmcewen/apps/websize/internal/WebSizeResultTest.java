package com.jonmcewen.apps.websize.internal;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class WebSizeResultTest {

	@Test
	public final void testNewUnknownSizeResult() throws MalformedURLException {
		WebSizeResult w = WebSizeResult.newUnknownSizeResult(new URL(
				"http://google.com"));
		assertEquals("google.com *", w.toString());
	}

	@Test
	public final void testWebSizeResult() throws MalformedURLException {
		WebSizeResult w = new WebSizeResult(new URL("http://google.com"), 123L);
		assertEquals("google.com 123", w.toString());
	}

}

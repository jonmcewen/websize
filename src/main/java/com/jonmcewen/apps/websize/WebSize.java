package com.jonmcewen.apps.websize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jonmcewen.apps.websize.internal.BytesCounter;
import com.jonmcewen.apps.websize.internal.UrlRetrievalException;
import com.jonmcewen.apps.websize.internal.UrlRetriever;
import com.jonmcewen.apps.websize.internal.UrlRetrieverImpl;
import com.jonmcewen.apps.websize.internal.WebSizeResult;
import com.jonmcewen.apps.websize.internal.WebSizeService;

/**
 * Main program class of the WebSize app. Reads web addresses (e.g.
 * "google.com") from standard input, one address per line, until "-1" is
 * supplied. Outputs one line per address containing the address followed by the
 * size of each page successfully retrieved, or an asterisk if the page could
 * not be retrieved (at all, or within the timeout). Finally prints "-1" on a
 * new line. Retrieves all addresses in parallel and will return in approx
 * 1500ms (or less).
 * 
 */
public class WebSize {

	public static final String END = "-1";

	public static void main(String[] args) throws IOException {

		// get input
		List<String> addresses = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String address = in.readLine();
		while (!END.equals(address)) {
			addresses.add(address);
			address = in.readLine();
		}

		// call service
		List<WebSizeResult> results = service().measurePageSizes(addresses);

		// output results
		for (WebSizeResult result : results) {
			System.out.println(result);
		}
		System.out.println(END);

	}

	/*
	 * Configures the service.
	 */
	private static WebSizeService service() {
		ExecutorService exec = Executors.newCachedThreadPool();
		BytesCounter counter = new BytesCounter();
		UrlRetriever retriever = new UrlRetrieverImpl();
		return new WebSizeService(exec, counter, retriever);

	}
}

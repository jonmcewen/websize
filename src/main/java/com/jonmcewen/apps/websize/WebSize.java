package com.jonmcewen.apps.websize;

/**
 * Main program class of the WebSize app. Reads web addresses (e.g.
 * "google.com") from standard input, one address per line, until "-1" is
 * supplied. Outputs one line per address containing the address followed by the
 * size of each page successfully retrieved, or an asterisk if the page could
 * not be retrieved (at all, or within the timeout). Finally prints "-1" on a new line. Retrieves all addresses
 * in parallel and will return in approx 1500ms (or less).
 * 
 */
public class WebSize {
	public static void main(String[] args) {
		System.out.println("Hello World!");
	}
}

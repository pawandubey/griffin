/*
 * Copyright 2015 Pawan Dubey pawandubey@outlook.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pawandubey.griffin;

import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.pawandubey.griffin.DirectoryCrawler.OUTPUT_DIRECTORY;

/**
 * Web Server for serving the static site for live preview,
 * on default port 9090.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class InternalServer {
	private Integer port = 9090;

	protected InternalServer(Integer p) {
		port = p;
	}

	/**
	 * Creates and starts the server to serve the contents of OUTPUT_DIRECTORY on port
	 * 9090.
	 */
	protected void startPreview() {

		InetSocketAddress cd = new InetSocketAddress(port);
		HttpServer server;
		try {
			server = HttpServer.create(cd, 8080);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		final Path root = Paths.get(OUTPUT_DIRECTORY);

		server.createContext("/", httpExchange -> {
			URI uri = httpExchange.getRequestURI();

			String pathname = root + uri.getPath();
			if (pathname.endsWith("/")) {
				pathname = pathname + "index.html";
			}
			File file = new File(pathname).getCanonicalFile();
			if (!file.toPath().startsWith(root)) {
				// Suspected path traversal attack: reject with 403 error.
				String response = "403 (Forbidden)\n";
				httpExchange.sendResponseHeaders(403, response.length());
				OutputStream os = httpExchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			} else if (!file.isFile()) {
				// Object does not exist or is not a file: reject with 404 error.
				String response = "404 (Not Found)\n";
				httpExchange.sendResponseHeaders(404, response.length());
				OutputStream os = httpExchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			} else {
				// Object exists and is a file: accept with response code 200.
				httpExchange.sendResponseHeaders(200, 0);
				try (OutputStream os = httpExchange.getResponseBody();
					 FileInputStream fs = new FileInputStream(file)) {
					// replace with InputStream.transferTo after in Java 9+
					byte[] buffer = new byte[8192];
					int read;
					while ((read = fs.read(buffer, 0, 8192)) >= 0) {
						os.write(buffer, 0, read);
					}
				}
			}
		});
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	/**
	 * Opens the system's default browser and tries to navigate to the URL at
	 * which the server is operational.
	 */
	protected void openBrowser() {
		String url = "http://localhost:" + port;

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new URI(url));
				} catch (IOException | URISyntaxException e) {
				}
			} else {
				openBrowserUsingXdg(url);
			}
		} else {
			openBrowserUsingXdg(url);
		}
	}

	private void openBrowserUsingXdg(String url) {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("xdg-open " + url);
		} catch (IOException e) {
		}
	}
}

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

import static com.pawandubey.griffin.DirectoryCrawler.OUTPUT_DIR;
import static io.undertow.Handlers.resource;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Embeds the Undertow Web Server for serving the static site for live preview,
 * on default port 9090.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Server {
    private Integer port = 9090;

    protected Server(Integer p) {
        port = p;
    }
    /**
     * Creates and starts the server to serve the contents of OUTPUT_DIR on port
 9090.
     */
    protected void startPreview() {
        Undertow server = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(resource(new PathResourceManager(Paths.get(OUTPUT_DIR), 100, true))
                        .setDirectoryListingEnabled(false))
                .build();
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
            try {
                desktop.browse(new URI(url));
            }
            catch (IOException | URISyntaxException e) {
            }
        }
        else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            }
            catch (IOException e) {
            }
        }
    }
}

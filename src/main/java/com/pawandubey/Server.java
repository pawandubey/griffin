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
package com.pawandubey;

import static com.pawandubey.DirectoryCrawler.OUTPUTDIR;
import static io.undertow.Handlers.resource;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import java.nio.file.Paths;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Server {
    protected void startPreview() {
        Undertow server = Undertow.builder()
                .addHttpListener(9090, "localhost")
                .setHandler(resource(new PathResourceManager(Paths.get(OUTPUTDIR), 100, true))
                        .setDirectoryListingEnabled(false))
                .build();
        server.start();
    }
}

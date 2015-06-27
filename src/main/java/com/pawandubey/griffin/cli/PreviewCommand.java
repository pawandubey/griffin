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
package com.pawandubey.griffin.cli;

import com.pawandubey.Server;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.IntOptionHandler;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class PreviewCommand implements GriffinCommand {
    @Option(name = "-port", handler = IntOptionHandler.class, usage = "Port on which to launch the preview. Default 9090")
    private Integer port = 9090;

    @Override
    public void execute() {
        Server server = new Server(port);
        server.startPreview();
        server.openBrowser();
    }
}

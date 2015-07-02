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

import static com.pawandubey.griffin.Data.config;
import com.pawandubey.griffin.Griffin;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.kohsuke.args4j.spi.IntOptionHandler;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class PreviewCommand implements GriffinCommand {

    @Option(name = "--port", aliases = {"-p"}, metaVar = "<port_number>", handler = IntOptionHandler.class, usage = "Port on which to launch the preview. Default to your configuredData. port.")
    private Integer port = config.getPort();

    @Option(name = "--help", aliases = {"-h"}, handler = BooleanOptionHandler.class, usage = "find help about this command")
    private boolean help = false;

    /**
     * Executes the command
     */
    @Override
    public void execute() {
        Griffin griffin = new Griffin();
        if (help) {
            System.out.println("Preview the site on the given port: default: 9090");
            System.out.println("usage: griffin preview [option]");
            System.out.println("Options: \n");
            CmdLineParser parser = new CmdLineParser(this, ParserProperties.defaults().withUsageWidth(120));
            parser.printUsage(System.out);
        }
        else {
            griffin.printAsciiGriffin();
            griffin.preview(port);
        }
    }
}

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

import static com.pawandubey.griffin.Configurator.LINE_SEPARATOR;
import com.pawandubey.griffin.Griffin;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class PublishCommand implements GriffinCommand {

    @Option(name = "--help", aliases = {"-h"}, handler = BooleanOptionHandler.class, usage = "find help about this command")
    private boolean help = false;

    @Option(name = "--quick", aliases = {"-q"}, handler = BooleanOptionHandler.class, usage = "Publish only the files which have changed since the last modification"
    )
    private Boolean fastParse = false;

    public PublishCommand() throws InterruptedException, IOException {

    }

    /**
     * Executes the command
     */
    @Override
    public void execute() {
        if (help) {
            System.out.println("Publish the content in the current Griffin directory.");
            System.out.println("usage: griffin publish [option]");
            System.out.println("Options: " + LINE_SEPARATOR);
            CmdLineParser parser = new CmdLineParser(this, ParserProperties.defaults().withUsageWidth(120));
            parser.printUsage(System.out);
            return;
        }
        try {
            Griffin griffin = new Griffin();
            griffin.printAsciiGriffin();
            griffin.publish(fastParse);
        }
        catch (IOException | InterruptedException ex) {
            Logger.getLogger(PublishCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

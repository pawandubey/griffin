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

import com.pawandubey.griffin.cli.GriffinCommand;
import com.pawandubey.griffin.cli.NewCommand;
import com.pawandubey.griffin.cli.PreviewCommand;
import com.pawandubey.griffin.cli.PublishCommand;
import com.pawandubey.model.Parsable;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Griffin {
    public final static LinkedBlockingQueue<Parsable> fileQueue = new LinkedBlockingQueue<>();
    //public final static Configurator config = new Configurator();
    @Argument(usage = "Pass commands", metaVar = "<commands>", handler = SubCommandHandler.class)
    @SubCommands({
        @SubCommand(name = "new", impl = NewCommand.class),
        @SubCommand(name = "publish", impl = PublishCommand.class),
        @SubCommand(name = "preview", impl = PreviewCommand.class)
    })
    public GriffinCommand commands;

    /**
     *
     */
    public Griffin() {

    }
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            Griffin griffin = new Griffin();
            //     System.out.println(Arrays.asList(args));
            CmdLineParser parser = new CmdLineParser(griffin, ParserProperties.defaults().withUsageWidth(120));
            parser.parseArgument(args);
            //       System.out.println(griffin.commands);
            if (args.length == 0) {
                parser.printUsage(System.out);
            }
            griffin.commands.execute();
        }
        catch (CmdLineException ex) {
            Logger.getLogger(Griffin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

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

import com.pawandubey.griffin.cli.GriffinCommand;
import com.pawandubey.griffin.cli.NewCommand;
import com.pawandubey.griffin.cli.PreviewCommand;
import com.pawandubey.griffin.cli.PublishCommand;
import com.pawandubey.griffin.model.Parsable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Griffin {

    public final static LinkedBlockingQueue<Parsable> fileQueue = new LinkedBlockingQueue<>();

    private final DirectoryCrawler crawler;
    private Parser parser;

    @Option(name = "--version", aliases = {"-v"}, handler = BooleanOptionHandler.class, usage = "print the current version")
    private boolean version = false;

    @Option(name = "--help", aliases = {"-h"}, handler = BooleanOptionHandler.class, usage = "print help message for the command")
    private boolean help = false;

    @Argument(usage = "Execute subcommands", metaVar = "<commands>", handler = SubCommandHandler.class)
    @SubCommands({
        @SubCommand(name = "new", impl = NewCommand.class),
        @SubCommand(name = "publish", impl = PublishCommand.class),
        @SubCommand(name = "preview", impl = PreviewCommand.class)
    })
    public GriffinCommand commands;

    /**
     * Creates a new instance of Griffin
     */
    public Griffin() {
        crawler = new DirectoryCrawler();
    }

    /**
     * Creates a new instance of Griffin with the root directory of the site set
     * to the given path.
     *
     * @param path The path to the root directory of the griffin site.
     */
    public Griffin(Path path) {
        crawler = new DirectoryCrawler(path.toString());
    }

    /**
     * Creates(scaffolds out) a new Griffin directory at the given path.
     *
     * @param path The path at which to scaffold.
     * @param name The name to give to the directory
     * @throws IOException
     */
    public void initialize(Path path, String name) throws IOException {
        Initializer init = new Initializer();
        init.scaffold(path, name);
    }

    /**
     * Parses the content of the site in the 'content' directory and produces
     * the output. It parses incrementally i.e it only parses the content which
     * has changed since the last parsing event if the fastParse variable is
     * true.
     *
     * @param fastParse Do a fast incremental parse
     * @throws IOException
     * @throws InterruptedException
     */
    public void publish(boolean fastParse) throws IOException, InterruptedException {
        InfoHandler info = new InfoHandler();
        if (fastParse == true) {
            crawler.fastReadIntoQueue(Paths.get(DirectoryCrawler.SOURCE_DIR).normalize());
        }
        else {
            crawler.readIntoQueue(Paths.get(DirectoryCrawler.SOURCE_DIR).normalize());
        }
        info.findLatestPosts(fileQueue);
        info.findNavigationPages(fileQueue);
        parser = new Parser();
        parser.parse(fileQueue);
        info.writeInfoFile();

    }

    /**
     * Creates the server and starts a preview at the given port
     *
     * @param port the port number for the server to run on.
     */
    public void preview(Integer port) {
        Server server = new Server(port);
        server.startPreview();
        server.openBrowser();
    }

    private void printHelpMessage() {
        String title = this.getClass().getPackage().getImplementationTitle();
        String ver = this.getClass().getPackage().getImplementationVersion();
        String author = this.getClass().getPackage().getImplementationVendor();
        String header = title + " version " + ver + " copyright " + author;
        String desc = "a simple and fast static site generator";
        String usage = "usage: " + title + " [subcommand] [options..] [arguments...]";
        String moreHelp = "run " + title + " <subcommand> " + "--help to see more help about individual subcommands";

        StringBuilder sb = new StringBuilder();
        sb.append(header).append("\n");
        if (this.version) {            
            System.out.println(sb.toString());            
        }
        else {
            sb.append(desc)
                    .append("\n")
                    .append(usage)
                    .append("\n")
                    .append(moreHelp)
                    .append("\n\n");
            System.out.println(sb.toString());
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            Griffin griffin = new Griffin();

            CmdLineParser parser = new CmdLineParser(griffin, ParserProperties.defaults().withUsageWidth(120));
            parser.parseArgument(args);

            if (griffin.help || griffin.version || args.length == 0) {
                griffin.printHelpMessage();
                parser.printUsage(System.out);
            }
            else {
                griffin.commands.execute();
            }
        }
        catch (CmdLineException ex) {
            Logger.getLogger(Griffin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

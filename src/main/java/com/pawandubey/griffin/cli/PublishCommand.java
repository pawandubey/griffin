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

import com.pawandubey.griffin.DirectoryCrawler;
import static com.pawandubey.griffin.Griffin.fileQueue;
import com.pawandubey.griffin.InfoHandler;
import com.pawandubey.griffin.Parser;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class PublishCommand implements GriffinCommand {

    @Option(name = "-fast", aliases = {"-f"}, handler = BooleanOptionHandler.class, usage = "Publish only the files which have changed since the last modification"
    )
    private Boolean fastParse = false;

    public PublishCommand() throws InterruptedException, IOException {

    }

    @Override
    public void execute() {
        try {
            DirectoryCrawler crawler = new DirectoryCrawler();
            System.out.println(Paths.get(DirectoryCrawler.SOURCEDIR).toAbsolutePath().normalize());
//        long start = System.currentTimeMillis();
            InfoHandler info = new InfoHandler();
            if (fastParse == true) {
                crawler.fastReadIntoQueue(Paths.get(DirectoryCrawler.SOURCEDIR).normalize());
            }
            else {
                crawler.readIntoQueue(Paths.get(DirectoryCrawler.SOURCEDIR).normalize());
            }
//        long endcrawl = (System.currentTimeMillis() - start) / 1000;
//        }

            info.findLatestPosts(fileQueue);
            Parser parser = new Parser();
//        long startparse = System.currentTimeMillis();

            parser.parse(fileQueue);
            info.writeInfoFile();
        }
        catch (InterruptedException | IOException ex) {
            Logger.getLogger(PublishCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

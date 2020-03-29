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

import com.pawandubey.griffin.Griffin;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
@Command(name = "publish", mixinStandardHelpOptions = true,
        description = "Publish the content in the current Griffin directory.")
public class PublishCommand implements Callable<Integer> {

    @Option(names = {"--quick", "-q"}, description = "Publish only the files which have changed since the last modification."
    )
    private Boolean fastParse = false;

    @Option(names = {"--rebuild", "-r"}, description = "Rebuild the site from scratch. This may take time for more number of posts.")
    private Boolean rebuild = false;

    @Override
    public Integer call() {
        try {
            Griffin griffin = new Griffin();
            griffin.printAsciiGriffin();
            griffin.publish(fastParse, rebuild);
            System.out.println("All done for now! I will be bach!");
        }
        catch (IOException | InterruptedException ex) {
            Logger.getLogger(PublishCommand.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return 0;
    }
}

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

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;
import static com.pawandubey.DirectoryCrawler.OUTPUTDIR;
import static com.pawandubey.DirectoryCrawler.SOURCEDIR;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Parser {

    private final Configuration config;

    public Parser() {
        config = Configuration.builder().enableSafeMode()
                .forceExtentedProfile()
                .setAllowSpacesInFencedCodeBlockDelimiters(true)
                .setEncoding("UTF-8")
                .build();
    }

    public void parse(BlockingQueue<? extends Path> collection) throws InterruptedException {
        Path p;
        String content;
        while (!collection.isEmpty()) {
            p = collection.take();
            content = readFile(p);
            writeParsedFile(p, content);
        }
    }

    private String readFile(Path p) {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    private void writeParsedFile(Path p, String content) {
        Path outputPath = Paths.get(OUTPUTDIR).resolve(Paths.get(SOURCEDIR).relativize(p));
        try (BufferedWriter bw = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            bw.write(Processor.process(content, config));
        }
        catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

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

    /**
     * creates a parser with configuration set to enable safe mode HTML with
     * extended profile from txtmark, allowing spaces in fenced code blocks and
     * encoding set to UTF-8.
     */
    public Parser() {
        config = Configuration.builder().enableSafeMode()
                .forceExtentedProfile()
                .setAllowSpacesInFencedCodeBlockDelimiters(true)
                .setEncoding("UTF-8")
                .build();
    }

    /**
     * Parses the collection of files in the queue to produce HTML output
     *
     * @param collection the queue of files to be parsed
     * @throws InterruptedException
     */
    public void parse(BlockingQueue<? extends Path> collection) throws InterruptedException {
        Path p;
        String content;
        while (!collection.isEmpty()) {
            p = collection.take();
            content = readFile(p);
            writeParsedFile(p, content);
        }
    }

    /**
     * Reads the content from the given path into a String object.
     *
     * @param p the path to the file
     * @return the String contents
     */
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

    /**
     * Writes the given string content to the path resolved from the given path
     * by replacing the file extension with .html.
     *
     * @param p the path to the file
     * @param content the content to be written
     */
    private void writeParsedFile(Path p, String content) {        
        Path outputPath = Paths.get(OUTPUTDIR).resolve(Paths.get(SOURCEDIR).relativize(p));
        Path htmlPath = Paths.get(outputPath.toString().substring(0, p.toString().lastIndexOf('.')).concat(".html"));
        try (BufferedWriter bw = Files.newBufferedWriter(htmlPath, StandardCharsets.UTF_8)) {
            bw.write(Processor.process(content, config));
        }
        catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

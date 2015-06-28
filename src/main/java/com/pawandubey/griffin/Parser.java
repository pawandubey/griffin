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

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Processor;
import static com.pawandubey.griffin.DirectoryCrawler.OUTPUT_DIR;
import static com.pawandubey.griffin.DirectoryCrawler.SOURCE_DIR;
import com.pawandubey.griffin.model.Parsable;
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
    private final Renderer renderer;
    private String parsedContent;

    /**
     * creates a parser with configuration set to enable safe mode HTML with
     * extended profile from txtmark, allowing spaces in fenced code blocks and
     * encoding set to UTF-8.
     *
     * @throws java.io.IOException the exception
     */
    public Parser() throws IOException {
        renderer = new Renderer();
        config = Configuration.builder().enableSafeMode()
                .forceExtentedProfile()
                .setAllowSpacesInFencedCodeBlockDelimiters(true)
                .setEncoding("UTF-8")
                .setCodeBlockEmitter(new CodeBlockEmitter())
                .build();
    }

    /**
     * Parses the collection of files in the queue to produce HTML output
     *
     * @param collection the queue of files to be parsed
     * @throws InterruptedException the exception
     * @throws java.io.IOException the exception
     */
    protected void parse(BlockingQueue<Parsable> collection) throws InterruptedException, IOException {
        Parsable p;
        String content;
        while (!collection.isEmpty()) {
            p = collection.take();
            writeParsedFile(p);
            //System.out.println("Wrote file:" + p.getAuthor() + " " + p.getTitle() + "\n" + p.getDate() + " " + p.getLocation());
        }
        if (Files.notExists(Paths.get(OUTPUT_DIR).resolve("index.html"))) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(OUTPUT_DIR).resolve("index.html"), StandardCharsets.UTF_8)) {
                bw.write(renderer.renderIndex());
            }
            catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
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
     * Writes the content of the Parsable to the path resolved from the slug by
     * creating a directory from the given slug and then writing the contents
     * into the index.html file inside it for pretty links.
     *
     * @param p the Parsable instance
     */
    private void writeParsedFile(Parsable p) throws IOException {
        String name = p.getSlug();
        Path parsedDirParent = Paths.get(OUTPUT_DIR).resolve(Paths.get(SOURCE_DIR).relativize(p.getLocation().getParent()));
        Path parsedDir = parsedDirParent.resolve(name);

        if (Files.notExists(parsedDir)) {
            Files.createDirectory(parsedDir);
        }
        Path htmlPath = parsedDir.resolve("index.html");

        try (BufferedWriter bw = Files.newBufferedWriter(htmlPath, StandardCharsets.UTF_8)) {
            parsedContent = Processor.process(p.getContent(), config);
            p.setContent(parsedContent);
            bw.write(renderer.renderParsable(p));
        }
        catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

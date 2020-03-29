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

import static com.pawandubey.griffin.Configurator.LINE_SEPARATOR;
import static com.pawandubey.griffin.Data.config;
import com.pawandubey.griffin.model.Page;
import com.pawandubey.griffin.model.Parsable;
import com.pawandubey.griffin.model.Post;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class to track some meta information about the parsing process, including,
 * but not limited to the last parse time and date, and the latest posts etc.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class InfoHandler {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss");
    static String LAST_PARSE_DATE;

    public InfoHandler() throws IOException {
        final Path infoFilePath = Paths.get(DirectoryCrawler.INFO_FILE);
        if (!Files.exists(infoFilePath)) {
            throw new IOException(DirectoryCrawler.INFO_FILE + " doesn't exist");
        }
        try (BufferedReader br = Files.newBufferedReader(infoFilePath,
                                                         StandardCharsets.UTF_8)) {
            LAST_PARSE_DATE = br.readLine();
        }
        catch (IOException ex) {
            Logger.getLogger(InfoHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Writes the info file with the parsing timestamp and a list of paths to
     * the latest posts.
     */
    protected void writeInfoFile() {
        Path infoFilePath = Paths.get(DirectoryCrawler.INFO_FILE);
        try (BufferedWriter bw
                            = Files.newBufferedWriter(infoFilePath,
                                                      StandardCharsets.UTF_8,
                                                      StandardOpenOption.WRITE,
                                                      StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(calculateTimeStamp());
            bw.write(LINE_SEPARATOR + String.join(LINE_SEPARATOR, Data.latestPosts.stream()
                                                  .map(p -> p.getLocation().toString())
                                        .collect(Collectors.toList())));
        }
        catch (IOException ex) {
            Logger.getLogger(InfoHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO refactor number of posts.
    /**
     * Finds the N latest posts chronologically to display in the index and
     * to write their paths in the info file.
     * Here N is the number of posts per
     * index page.
     *
     * @param collection the queue of Parsables
     */
    protected void findLatestPosts(BlockingQueue<Parsable> collection) {
        collection.stream()
                .filter(p -> p instanceof Post)
                .sorted((a, b) -> {
                    return b.getDate().compareTo(a.getDate());
                }).limit(config.getIndexPosts())
                .forEach(p -> Data.latestPosts.add(p));

    }

    /**
     * finds all pages tagged as "nav" for addition to the site's navigation.
     *
     * @param collection the queue of Parsables
     */
    protected void findNavigationPages(BlockingQueue<Parsable> collection) {
        collection.stream()
                .filter(p -> p instanceof Page)
                .filter(p -> p.getTags().contains("nav"))
                .forEach(p -> Data.navPages.add(p));
    }

    /**
     * Calculates the current time stamp according to the System's default
     * timezone and formats it as per the given formatter.
     *
     * @return the string representation of the timestamp
     */
    private String calculateTimeStamp() {
        LocalDateTime parseTime = LocalDateTime.now();
        return parseTime.format(formatter);
    }
}

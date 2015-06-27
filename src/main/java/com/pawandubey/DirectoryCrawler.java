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

import com.moandjiezana.toml.Toml;
import static com.pawandubey.Griffin.fileQueue;
import com.pawandubey.model.Parsable;
import com.pawandubey.model.Post;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class DirectoryCrawler {

    public static final String USERHOME = System.getProperty("user.home");
    public static final String FILESEPARATOR = System.getProperty("file.separator");
    //TODO remove hardcoded value
    public static final String ROOTDIR = ".";//USERHOME + FILESEPARATOR + "Desktop/gtest";
    public static final String SOURCEDIR = ROOTDIR + FILESEPARATOR + "content";
    public static final String OUTPUTDIR = ROOTDIR + FILESEPARATOR + "output";
    public static final String INFO_FILE = ROOTDIR + FILESEPARATOR + ".info";
    public static String author = null;//config.siteAuthor;

    /**
     * Crawls the whole content directory and adds the files to the main queue
     * for parsing.
     *
     * @param rootPath path to the content directory
     * @throws IOException
     */
    public void readIntoQueue(Path rootPath) throws IOException {

        cleanOutputDirectory();

        Files.walkFileTree(rootPath, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path correspondingOutputPath = Paths.get(OUTPUTDIR).resolve(rootPath.relativize(dir));
                if (Files.notExists(correspondingOutputPath)) {
                    Files.createDirectory(correspondingOutputPath);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    Path resolvedPath = Paths.get(OUTPUTDIR).resolve(rootPath.relativize(file));

                    if (Files.probeContentType(file).equals("text/x-markdown")) {
                        Parsable parsable = createParsable(file);
                        fileQueue.put(parsable);
                    }
                    else {
                        Files.copy(file, resolvedPath, StandardCopyOption.REPLACE_EXISTING);
                    }

                }
                catch (InterruptedException ex) {
                    Logger.getLogger(DirectoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    //TODO refactor this method to make use of the above method someway.
    /**
     * Checks if the file has been modified after the last parse event and only
     * then adds the file into the queue for parsing, hence saving time.
     *
     * @param rootPath
     * @throws IOException
     */
    protected void fastReadIntoQueue(Path rootPath) throws IOException {
        Files.walkFileTree(rootPath, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path correspondingOutputPath = Paths.get(OUTPUTDIR).resolve(rootPath.relativize(dir));
                if (Files.notExists(correspondingOutputPath)) {
                    Files.createDirectory(correspondingOutputPath);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    LocalDateTime fileModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(file).toInstant(), ZoneId.systemDefault());
                    LocalDateTime lastParse = LocalDateTime.parse(InfoHandler.LAST_PARSE_DATE, InfoHandler.formatter);
                    Path resolvedPath = Paths.get(OUTPUTDIR).resolve(rootPath.relativize(file));
                    if (fileModified.isAfter(lastParse)) {
                        if (Files.probeContentType(file).equals("text/x-markdown")) {
                            Parsable parsable = createParsable(file);
                            fileQueue.put(parsable);
                        }
                        else {
                            Files.copy(file, resolvedPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }

                }
                catch (InterruptedException ex) {
                    Logger.getLogger(DirectoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private Parsable createParsable(Path file) {
        Toml toml = new Toml();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            StringBuilder header = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null && !line.equals("#####")) {
                header.append(line).append("\n");
            }
            toml.parse(header.toString());
            String title = toml.getString("title");
            author = toml.getString("author") != null ? toml.getString("author") : author;
            String date = toml.getString("date");
            String slug = toml.getString("slug");
            LocalDate publishDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy MM dd"));
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            return new Post(title, author, publishDate, file, content.toString(), slug);
        }
        catch (IOException ex) {
            Logger.getLogger(DirectoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Cleans up the output directory before running the full parse.
     *
     * @throws IOException When a file visit goes wrong
     */
    private void cleanOutputDirectory() throws IOException {
        Path pathToClean = Paths.get(OUTPUTDIR);
        Files.walkFileTree(pathToClean, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!Files.isSameFile(dir, pathToClean)) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }

        });

    }
}

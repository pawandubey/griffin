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

import com.moandjiezana.toml.Toml;
import static com.pawandubey.griffin.Griffin.fileQueue;
import static com.pawandubey.griffin.Renderer.templateRoot;
import com.pawandubey.griffin.model.Page;
import com.pawandubey.griffin.model.Parsable;
import com.pawandubey.griffin.model.Post;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class DirectoryCrawler {

    public static final String USERHOME = System.getProperty("user.home");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    //TODO remove hardcoded value
    public static String ROOT_DIR = System.getProperty("user.dir");
    public static final String SOURCE_DIR = ROOT_DIR + FILE_SEPARATOR + "content";
    public static final String OUTPUT_DIR = ROOT_DIR + FILE_SEPARATOR + "output";
    public static final String INFO_FILE = ROOT_DIR + FILE_SEPARATOR + ".info";
    public static final Configurator config = new Configurator();
    public static String author = config.getSiteAuthor();
    public final String HEADER_DELIMITER = "#####";

    public DirectoryCrawler() {

    }

    public DirectoryCrawler(String path) {
        ROOT_DIR = path;
    }

    /**
     * Crawls the whole content directory and adds the files to the main queue
     * for parsing.
     *
     * @param rootPath path to the content directory
     * @throws IOException
     */
    protected void readIntoQueue(Path rootPath) throws IOException {
        long start = System.currentTimeMillis();
        cleanOutputDirectory();
        long enddel = System.currentTimeMillis();
        copyAssets();
        long endcop = System.currentTimeMillis();
        System.out.println("Deletion: " + (enddel - start));
        System.out.println("Copy: " + (endcop - enddel));
        Files.walkFileTree(rootPath, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path correspondingOutputPath = Paths.get(OUTPUT_DIR).resolve(rootPath.relativize(dir));
                if (Files.notExists(correspondingOutputPath)) {
                    Files.createDirectory(correspondingOutputPath);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    Path resolvedPath = Paths.get(OUTPUT_DIR).resolve(rootPath.relativize(file));

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

        copyAssets();

        Files.walkFileTree(rootPath, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path correspondingOutputPath = Paths.get(OUTPUT_DIR).resolve(rootPath.relativize(dir));
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
                    Path resolvedPath = Paths.get(OUTPUT_DIR).resolve(rootPath.relativize(file));
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
            while ((line = br.readLine()) != null && !line.equals(HEADER_DELIMITER)) {
                header.append(line).append("\n");
            }
            toml.parse(header.toString());
            String title = toml.getString("title");
            author = toml.getString("author") != null ? toml.getString("author") : author;
            String date = toml.getString("date");
            String slug = toml.getString("slug");
            LocalDate publishDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(config.getDateFormat()));
            String layout = toml.getString("layout");
            List<String> tag = toml.getList("tags");
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            if (layout.equals("post")) {
                return new Post(title, author, publishDate, file, content.toString(), slug, layout, tag);
            }
            else {
                return new Page(title, author, file, content.toString(), slug, layout, tag);
            }
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
        Path pathToClean = Paths.get(OUTPUT_DIR);
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

    /**
     * Copies the assets i.e images, CSS, JS etc needed by the theme to the
     * output directory.
     *
     * @throws IOException
     */
    private void copyAssets() throws IOException {
        Path assetsPath = Paths.get(templateRoot, "assets");
        Path outputAssetsPath = Paths.get(OUTPUT_DIR, "assets");
        if (Files.notExists(outputAssetsPath)) {
            Files.createDirectory(outputAssetsPath);
        }
        Files.walkFileTree(assetsPath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path correspondingOutputPath = outputAssetsPath.resolve(assetsPath.relativize(dir));
                if (Files.notExists(correspondingOutputPath)) {
                    Files.createDirectory(correspondingOutputPath);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path resolvedPath = outputAssetsPath.resolve(assetsPath.relativize(file));

                Files.copy(file, resolvedPath, StandardCopyOption.REPLACE_EXISTING);

                return FileVisitResult.CONTINUE;
            }

        });
    }
}

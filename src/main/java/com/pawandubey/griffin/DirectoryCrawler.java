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
import static com.pawandubey.griffin.Configurator.LINE_SEPARATOR;
import static com.pawandubey.griffin.Data.config;
import com.pawandubey.griffin.model.Page;
import com.pawandubey.griffin.model.Parsable;
import com.pawandubey.griffin.model.Post;
import static com.pawandubey.griffin.renderer.Renderer.templateRoot;
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
    public static String ROOT_DIRECTORY = System.getProperty("user.dir");
    public static final String SOURCE_DIRECTORY = ROOT_DIRECTORY + FILE_SEPARATOR + "content";
    public static final String OUTPUT_DIRECTORY = ROOT_DIRECTORY + FILE_SEPARATOR + "output";
    public static final String INFO_FILE = ROOT_DIRECTORY + FILE_SEPARATOR + ".info";
    public static String author = config.getSiteAuthor();
    public final String HEADER_DELIMITER = "#####";
    public static final String TAG_DIRECTORY = OUTPUT_DIRECTORY + FILE_SEPARATOR + "tags";
    private final StringBuilder header = new StringBuilder();
    private final Toml toml = new Toml();
    public static final String EXCERPT_MARKER = "##more##";

    public DirectoryCrawler() {

    }

    /**
     * Creates a new DirectoryCrawler with the root path of the site directory
     * set to path.
     *
     * @param path the path to the root of the site's directory.
     */
    public DirectoryCrawler(String path) {
        ROOT_DIRECTORY = path;
    }

    /**
     * Crawls the whole content directory and adds the files to the main queue
     * for parsing.
     *
     * @param rootPath path to the content directory
     * @throws IOException the exception
     */
    protected void readIntoQueue(Path rootPath) throws IOException, InterruptedException {

        cleanOutputDirectory();
        copyTemplateAssets();

        Files.walkFileTree(rootPath, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path correspondingOutputPath = Paths.get(OUTPUT_DIRECTORY).resolve(rootPath.relativize(dir));
                if (config.getExcludeDirs().contains(dir.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (Files.notExists(correspondingOutputPath)) {
                    Files.createDirectory(correspondingOutputPath);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    Path resolvedPath = Paths.get(OUTPUT_DIRECTORY).resolve(rootPath.relativize(file));

                    if (file.getFileName().toString().endsWith(".md")) {

                        Parsable parsable = createParsable(file);
                        Data.fileQueue.put(parsable);
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
     * @throws IOException the exception
     */
    protected void fastReadIntoQueue(Path rootPath) throws IOException, InterruptedException {
        cleanOutputDirectory();
        copyTemplateAssets();

        Files.walkFileTree(rootPath, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path correspondingOutputPath = Paths.get(OUTPUT_DIRECTORY).resolve(rootPath.relativize(dir));
                if (config.getExcludeDirs().contains(dir.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
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
                    Path resolvedPath = Paths.get(OUTPUT_DIRECTORY).resolve(rootPath.relativize(file));
                    if (Files.probeContentType(file).equals("text/x-markdown")) {
                        if (fileModified.isAfter(lastParse)) {
                            Parsable parsable = createParsable(file);
                            Data.fileQueue.removeIf(p -> p.getPermalink().equals(parsable.getPermalink()));
                            Data.fileQueue.put(parsable);
                        }
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

    /**
     * Creates an appropriate instance of a Parsable implementation depending
     * upon the header of the file.
     *
     * @param file the path of the file from which to create a Parsable.
     * @return the created Parsable.
     */
    private Parsable createParsable(Path file) {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            header.setLength(0);
            String line;
            while ((line = br.readLine()) != null && !line.equals(HEADER_DELIMITER)) {
                header.append(line).append(LINE_SEPARATOR);
            }
            toml.parse(header.toString());
            String title = toml.getString("title");
            author = toml.getString("author") != null ? toml.getString("author") : author;
            String date = toml.getString("date");
            String slug = toml.getString("slug");
            LocalDate publishDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(config.getInputDateFormat()));
            publishDate = LocalDate.parse(publishDate.format(DateTimeFormatter.ofPattern(config.getOutputDateFormat())), DateTimeFormatter.ofPattern(config.getOutputDateFormat()));
            String layout = toml.getString("layout");
            List<String> tag = toml.getList("tags");
            String img = toml.getString("image");
            StringBuilder content = new StringBuilder();
            String[] halves;
            while ((line = br.readLine()) != null) {
                content.append(line).append(LINE_SEPARATOR);
            }
            if (layout.equals("post")) {
                return new Post(title, author, publishDate, file, content.toString(), img, slug, layout, tag);
            }
            else {
                return new Page(title, author, file, content.toString(), img, slug, layout, tag);
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
    private void cleanOutputDirectory() throws IOException, InterruptedException {
        System.out.println("Cleaning up the output area...");
        Path pathToClean = Paths.get(OUTPUT_DIRECTORY).toAbsolutePath().normalize();

        Files.walkFileTree(pathToClean, new SimpleFileVisitor<Path>() {

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
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });

        Files.createDirectory(pathToClean);
        System.out.println("Cleanup done.");
    }

    /**
     * Copies the assets i.e images, CSS, JS etc needed by the theme to the
     * output directory.
     *
     * @throws IOException the exception
     */
    private void copyTemplateAssets() throws IOException {
        System.out.println("Carefully copying the assests...");
        Path assetsPath = Paths.get(templateRoot, "assets");
        Path outputAssetsPath = Paths.get(OUTPUT_DIRECTORY, "assets");
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

        System.out.println("Copying done.");
    }
}

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
import static com.pawandubey.griffin.ConfigurationKeys.DATE_FORMAT;
import static com.pawandubey.griffin.ConfigurationKeys.EXCLUDE;
import static com.pawandubey.griffin.ConfigurationKeys.OUTPUT_DIR;
import static com.pawandubey.griffin.ConfigurationKeys.PORT;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_AUTHOR;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_BASE_URL;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_NAME;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_TAGLINE;
import static com.pawandubey.griffin.ConfigurationKeys.SOURCE_DIR;
import static com.pawandubey.griffin.ConfigurationKeys.THEME;
import static com.pawandubey.griffin.DirectoryCrawler.FILE_SEPARATOR;
import static com.pawandubey.griffin.DirectoryCrawler.ROOT_DIRECTORY;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Reads and updates configuration for the site from the config.toml file.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Configurator {

    private final String CONFIG_FILE = ROOT_DIRECTORY + FILE_SEPARATOR + "config.toml";

    private String siteName = "Your Own Griffin";
    private String siteTagline = "Not just another site";
    private String siteAuthor = "Admin";
    private String siteBaseUrl;
    private String sourceDir = "content";
    private String outputDir = "output";
    private List<String> excludeDirs;
    private String dateFormat = "yyyy MM dd";
    private String theme = "wells";
    //private String template = ROOT_DIRECTORY + FILE_SEPARATOR + "assets" + FILE_SEPARATOR + "templates" + FILE_SEPARATOR + theme;
    private Integer port = 9090;

    public Configurator() {
        
        if (Files.exists(Paths.get(CONFIG_FILE))) {
            Toml toml = new Toml();
            toml.parse(new File(CONFIG_FILE));
            siteName = toml.getString(SITE_NAME.key);
            siteTagline = toml.getString(SITE_TAGLINE.key);
            siteAuthor = toml.getString(SITE_AUTHOR.key);
            siteBaseUrl = toml.getString(SITE_BASE_URL.key);
            sourceDir = toml.getString(SOURCE_DIR.key);
            outputDir = toml.getString(OUTPUT_DIR.key);
            excludeDirs = toml.getList(EXCLUDE.key);
            dateFormat = toml.getString(DATE_FORMAT.key);
            theme = toml.getString(THEME.key);
            port = Integer.valueOf(toml.getLong(PORT.key).toString());
            //template = ROOT_DIRECTORY + FILE_SEPARATOR + "assets" + FILE_SEPARATOR + "templates" + FILE_SEPARATOR + theme;
        }
        this.siteBaseUrl = "http://localhost:" + port;
    }

    protected Configurator withSiteName(String name) {
        siteName = name;
        return this;
    }

    protected Configurator withSiteTagline(String tagline) {
        siteTagline = tagline;
        return this;
    }

    protected Configurator withSiteAuthour(String author) {
        siteAuthor = author;
        return this;
    }

    //TODO: implement this elegantly
//    protected Configurator withSiteBaseUrl(String name){
//        siteName = name;
//        return this;
//    }
    protected Configurator withDateFormat(String format) {
        dateFormat = format;
        return this;
    }

    protected Configurator withPort(Integer por) {
        port = por;
        return this;
    }

    protected Configurator withSourceDir(String src) {
        sourceDir = src;
        return this;
    }

    protected Configurator withOutputDir(String out) {
        outputDir = out;
        return this;
    }

    protected void writeConfig(Path path) throws IOException {

        StringBuilder initialConfig = new StringBuilder();
        initialConfig.append("#site details\n")
                .append(SITE_NAME.key).append(" = ").append("\"").append(this.siteName).append("\"").append("\n")
                .append(SITE_TAGLINE.key).append(" = ").append("\"").append(this.siteTagline).append("\"").append("\n")
                .append(SITE_AUTHOR.key).append(" = ").append("\"").append(this.siteAuthor).append("\"").append("\n")
                .append(SITE_BASE_URL.key).append(" = ").append("\"").append(this.siteBaseUrl).append("\"").append("\n")
                .append("\n\n#parsing details\n")
                .append(SOURCE_DIR.key).append(" = ").append("\"").append(this.sourceDir).append("\"").append("\n")
                .append(OUTPUT_DIR.key).append(" = ").append("\"").append(this.outputDir).append("\"").append("\n")
                .append(EXCLUDE.key).append(" = ").append("[]").append("\n")
                .append("\n\n#styling\n")
                .append(DATE_FORMAT.key).append(" = ").append("\"").append(this.dateFormat).append("\"").append("\n")
                .append(THEME.key).append(" = ").append("\"").append(this.theme).append("\"").append("\n")
                .append("\n\n#preview\n")
                .append(PORT.key).append(" = ").append(this.port);

        try (BufferedWriter br = Files.newBufferedWriter(path.resolve("config.toml"), StandardOpenOption.TRUNCATE_EXISTING)) {
            br.write(initialConfig.toString().trim());
        }
        Files.move(path.resolve("output"), path.resolve(this.outputDir), StandardCopyOption.REPLACE_EXISTING);
        Files.move(path.resolve("content"), path.resolve(this.sourceDir), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * @return the siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @return the siteTagline
     */
    public String getSiteTagline() {
        return siteTagline;
    }

    /**
     * @return the siteAuthor
     */
    public String getSiteAuthor() {
        return siteAuthor;
    }

    /**
     * @return the siteBaseUrl
     */
    public String getSiteBaseUrl() {
        return siteBaseUrl;
    }

    /**
     * @return the sourceDir
     */
    public String getSourceDir() {
        return sourceDir;
    }

    /**
     * @return the outputDir
     */
    public String getOutputDir() {
        return outputDir;
    }

    /**
     * @return the excludeDirs
     */
    public List<String> getExcludeDirs() {
        return excludeDirs;
    }

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }
}

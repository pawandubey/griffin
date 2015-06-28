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
import static com.pawandubey.griffin.DirectoryCrawler.ROOT_DIR;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Reads and updates configuration for the site from the config.toml file.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Configurator {

    private static final String CONFIG_FILE = ROOT_DIR + FILE_SEPARATOR + "config.toml";

    private String siteName = "Your Own Griffin";
    private String siteTagline = "Not just another site";
    private String siteAuthor = "Admin";
    private String siteBaseUrl = ".";
    private String sourceDir = ROOT_DIR + FILE_SEPARATOR + "content";
    private String outputDir = ROOT_DIR + FILE_SEPARATOR + "output";
    private List<String> excludeDirs;
    private String dateFormat = InfoHandler.formatter.toString();
    private String theme = ROOT_DIR + FILE_SEPARATOR + "assets" + FILE_SEPARATOR + "templates/Wells/";
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
        }
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

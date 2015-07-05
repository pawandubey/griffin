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
import static com.pawandubey.griffin.ConfigurationKeys.EXCLUDE;
import static com.pawandubey.griffin.ConfigurationKeys.IMAGE;
import static com.pawandubey.griffin.ConfigurationKeys.INDEX_POSTS;
import static com.pawandubey.griffin.ConfigurationKeys.IN_DATE_FORMAT;
import static com.pawandubey.griffin.ConfigurationKeys.OUTPUT_DIR;
import static com.pawandubey.griffin.ConfigurationKeys.OUT_DATE_FORMAT;
import static com.pawandubey.griffin.ConfigurationKeys.PORT;
import static com.pawandubey.griffin.ConfigurationKeys.RENDER_TAGS;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_AUTHOR;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_BASE_URL;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_NAME;
import static com.pawandubey.griffin.ConfigurationKeys.SITE_TAGLINE;
import static com.pawandubey.griffin.ConfigurationKeys.SOCIAL;
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
import java.util.Map;

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
    private String inputDateFormat = "yyyy MM dd";
    private String outputDateFormat;
    private String theme = "wells";
    private Integer port = 9090;
    private Social social;
    private String headerImage;
    private Integer indexPosts = 5;
    private boolean renderTags = false;
    public static final String LINE_SEPARATOR = System.lineSeparator();

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
            inputDateFormat = toml.getString(IN_DATE_FORMAT.key);
            outputDateFormat = toml.getString(OUT_DATE_FORMAT.key);
            theme = toml.getString(THEME.key);
            indexPosts = Integer.valueOf(toml.getLong(INDEX_POSTS.key).toString());
            port = Integer.valueOf(toml.getLong(PORT.key).toString());
            Map<String, Object> socialLinks = toml.getTable(SOCIAL.key).to(Map.class);
            social = new Social(socialLinks);
            renderTags = toml.getBoolean(RENDER_TAGS.key);
            headerImage = toml.getString(IMAGE.key);
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

    protected Configurator withDateFormat(String format) {
        inputDateFormat = format;
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
        String conf = "#parsing details" + LINE_SEPARATOR
                      + "source = \"" + sourceDir + "\"" + LINE_SEPARATOR
                      + "output = \"" + outputDir + "\"" + LINE_SEPARATOR
                      + "exclude = []" + LINE_SEPARATOR
                      + "" + LINE_SEPARATOR
                      + "#styling" + LINE_SEPARATOR
                      + "inputdate = \"" + inputDateFormat + "\"" + LINE_SEPARATOR
                      + "outputdate = \"MMM d yyyy\"" + LINE_SEPARATOR
                      + "theme = \"hyde\"" + LINE_SEPARATOR
                      + "headerimage = \"\"" + LINE_SEPARATOR
                      + "postsperindex = 5" + LINE_SEPARATOR
                      + "" + LINE_SEPARATOR
                      + "#render files as per tags?" + LINE_SEPARATOR
                      + "rendertags = false" + LINE_SEPARATOR
                      + "" + LINE_SEPARATOR
                      + "#preview" + LINE_SEPARATOR
                      + "port = " + port + LINE_SEPARATOR
                      + "" + LINE_SEPARATOR
                      + "#social media details" + LINE_SEPARATOR
                      + "[social]" + LINE_SEPARATOR
                      + "	disqus = \"your disqus shortcode\"" + LINE_SEPARATOR
                      + "	fb = \"your facebook profile id\"" + LINE_SEPARATOR
                      + "	twitter = \"your twitter handle\"" + LINE_SEPARATOR
                      + "	github = \"your github profile id\"" + LINE_SEPARATOR
                      + "	gplus = \"your google plus profile id\"" + LINE_SEPARATOR
                      + "	so = \"your stackoverflow profile id\"" + LINE_SEPARATOR
                      + "" + LINE_SEPARATOR
                      + "#site details" + LINE_SEPARATOR
                      + "[site]" + LINE_SEPARATOR
                      + "	name = \"" + siteName + "\"" + LINE_SEPARATOR
                      + "	tagline = \"" + siteTagline + "\"" + LINE_SEPARATOR
                      + "	author = \"" + siteAuthor + "\"" + LINE_SEPARATOR
                      + "	baseurl = \"http://localhost:" + port + "\"";

        try (BufferedWriter br = Files.newBufferedWriter(path.resolve("config.toml"), StandardOpenOption.TRUNCATE_EXISTING)) {
            br.write(conf.trim());
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

    ;
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
     * @return the inputDateFormat
     */
    public String getInputDateFormat() {
        return inputDateFormat;
    }

    /**
     *
     * @return the outputDateFormat
     */
    public String getOutputDateFormat() {
        return outputDateFormat;
    }

    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     *
     * @return the number of posts per index page
     */
    public Integer getIndexPosts() {
        return indexPosts;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     *
     * @return the social links
     */
    public Social getSocial() {
        return social;
    }

    /**
     *
     * @return whether to render tags
     */
    public boolean getRenderTags() {
        return renderTags;
    }

    /**
     *
     * @return the String containing the path to the header image for the site.
     */
    public String getHeaderImage() {
        return headerImage;
    }

    /**
     * Encapsulates the social media links in the configuration
     */
    public static class Social {

        private String gplus;
        private String disqus;
        private String fb;
        private String twitter;
        private String github;
        private String so;

        Social(Map<String, Object> map) {
            for (String k : map.keySet()) {
                if (k.equals("gplus")) {
                    gplus = (String) map.get(k);
                }
                else if (k.equals("disqus")) {
                    disqus = (String) map.get(k);
                }
                else if (k.equals("fb")) {
                    fb = (String) map.get(k);
                }
                else if (k.equals("twitter")) {
                    twitter = (String) map.get(k);
                }
                else if (k.equals("so")) {
                    so = (String) map.get(k);
                }
                else {
                    github = (String) map.get(k);
                }
            }
        }

        /**
         * @return the gplus
         */
        public String getGplus() {
            return gplus;
        }

        /**
         * @return the disqus
         */
        public String getDisqus() {
            return disqus;
        }

        /**
         * @return the fb
         */
        public String getFb() {
            return fb;
        }

        /**
         * @return the twitter
         */
        public String getTwitter() {
            return twitter;
        }

        /**
         * @return the github
         */
        public String getGithub() {
            return github;
        }

        /**
         * @return the so
         */
        public String getSo() {
            return so;
        }

    }
}

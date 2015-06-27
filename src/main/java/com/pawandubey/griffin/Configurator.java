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

import static com.pawandubey.griffin.DirectoryCrawler.FILESEPARATOR;
import static com.pawandubey.griffin.DirectoryCrawler.ROOTDIR;
import java.util.List;

/**
 * Reads and updates configuration for the site from the config.toml file.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Configurator {

    public static final String CONFIG_FILE = ROOTDIR + FILESEPARATOR + "config.toml";
    public String siteName = "Your Own Griffin";
    public String siteTagline = "Not just another site";
    public String siteAuthor = "Admin";
    public String siteBaseUrl = ".";
    public String sourceDir = ROOTDIR + FILESEPARATOR + "content";
    public String outputDir = ROOTDIR + FILESEPARATOR + "output";
    public List<String> excludeDirs;
    public String dateFormat = InfoHandler.formatter.toString();

    public Configurator() {
//        Toml toml = new Toml();
//        toml.parse(new File(CONFIG_FILE));
//        siteName = toml.getString(SITE_NAME.key);
//        siteTagline = toml.getString(SITE_TAGLINE.key);
//        siteAuthor = toml.getString(SITE_AUTHOR.key);
//        siteBaseUrl = toml.getString(SITE_BASE_URL.key);
//        sourceDir = toml.getString(SOURCE_DIR.key);
//        outputDir = toml.getString(OUTPUT_DIR.key);
//        excludeDirs = toml.getList(EXCLUDE.key);
//        dateFormat = toml.getString(DATE_FORMAT.key);
    }

}

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

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public enum ConfigurationKeys {

    SITE_NAME("sitename"),
    SITE_AUTHOR("siteauthor"),
    SITE_BASE_URL("sitebaseurl"),
    SITE_TAGLINE("sitetagline"),
    SOURCE_DIR("source"),
    OUTPUT_DIR("output"),
    EXCLUDE("exclude"),
    DATE_FORMAT("dateformat"),
    THEME("theme");

    public final String key;

    ConfigurationKeys(String key) {
        this.key = key;
    }

}

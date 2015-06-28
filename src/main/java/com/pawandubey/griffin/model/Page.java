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
package com.pawandubey.griffin.model;

import static com.pawandubey.griffin.DirectoryCrawler.SOURCE_DIR;
import static com.pawandubey.griffin.DirectoryCrawler.config;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Page implements Parsable {

    private final String title;
    private final String author;
    private final Path location;
    private String content;
    private final String slug;
    private final String layout;
    private String permalink;
    private final List<String> tags;

    /**
     * Creates a Page instance with the parameters.
     *
     * @param titl
     * @param auth
     * @param loc
     * @param cont
     * @param slu
     * @param lay
     * @param tag
     */
    public Page(String titl, String auth, Path loc, String cont, String slu, String lay, List<String> tag) {
        author = auth;
        title = titl;
        location = loc;
        content = cont;
        slug = slu;
        layout = lay;
        tags = tag;
    }

    /**
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * @return the author
     */
    @Override
    public String getAuthor() {
        return author;
    }

    /**
     * @return the location
     */
    @Override
    public Path getLocation() {
        return location;
    }

    /**
     * @return the content
     */
    @Override
    public String getContent() {
        return content;
    }

    /**
     * @return the slug
     */
    @Override
    public String getSlug() {
        if (slug == null || slug.equals(" ")) {
            return String.join("-", title.trim().toLowerCase().split(" "));
        }
        else {
            return String.join("-", slug.trim().toLowerCase().split(" "));
        }
    }

    @Override
    public LocalDate getDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return the layout
     */
    @Override
    public String getLayout() {
        return layout;
    }

    /**
     * @return the permalink, as decided by whether the user has specified a
     * custom slug or not. If the slug is not specified, then the permalink is
     * constructed from the post-title
     */
    @Override
    public String getPermalink() {
        Path parentDir = Paths.get(SOURCE_DIR).relativize(location.getParent());
        permalink = config.getSiteBaseUrl().concat("/").concat(parentDir.resolve(getSlug()).toString()).concat("/");
        return permalink;
    }

    /**
     * Sets the content of the page.
     *
     * @param content the content to be set.
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return the list of tags.
     */
    @Override
    public List<String> getTags() {
        return tags;
    }

}

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

import com.pawandubey.griffin.Data;
import static com.pawandubey.griffin.Data.config;
import static com.pawandubey.griffin.DirectoryCrawler.SOURCE_DIRECTORY;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Post implements Parsable {

    private final String title;
    private final String author;
    //String summary;
    private final LocalDate date;
    private final String prettyDate;
    private final Path location;
    private String content;
    private final String slug;
    private final String layout;
    private String permalink;
    private final List<String> tags;

    /**
     * Creates a Post with the given paramenter.
     *
     * @param titl the post title
     * @param auth the post author
     * @param dat the post date
     * @param loc the post's Path
     * @param cont the post's content
     * @param slu the post slug
     * @param lay the layout
     * @param tag the list of tags
     */
    public Post(String titl, String auth, LocalDate dat,
                Path loc, String cont, String slu, String lay, List<String> tag) {
        title = titl;
        author = auth;
        //TODO add summary option
        //this.summary = summ;
        date = dat;
        prettyDate = date.format(DateTimeFormatter.ofPattern(config.getOutputDateFormat()));
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
     * @return the date
     */
    @Override
    public LocalDate getDate() {
        return date;
    }

    public String getPrettyDate() {
        return prettyDate;
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

    /**
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
        Path parentDir = Paths.get(SOURCE_DIRECTORY).relativize(location.getParent());
        permalink = Data.config.getSiteBaseUrl().concat("/").concat(parentDir.resolve(getSlug()).toString()).concat("/");
        return permalink;
    }

    /**
     * Sets the content of the post to the given String.
     *
     * @param content the String representing the new content.
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the list of tags for the post.
     */
    @Override
    public List<String> getTags() {
        return tags;
    }

}

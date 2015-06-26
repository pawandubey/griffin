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
package com.pawandubey.model;

import static com.pawandubey.DirectoryCrawler.SOURCEDIR;
import static com.pawandubey.Griffin.config;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    private final Path location;
    private String content;
    private final String slug;
    private final String layout;
    private String permalink;
    private final List<String> tags;

    public Post(String titl, String auth, LocalDate dat,
                Path loc, String cont, String slu, String lay, List<String> tag) {
        title = titl;
        author = auth;
        //TODO add summary option
        //this.summary = summ;
        date = dat;
        location = loc;
        content = cont;
        slug = slu;
        layout = lay;
        tags = tag;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the author
     */
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
    public String getLayout() {
        return layout;
    }

    @Override
    public String getPermalink() {
        Path parentDir = Paths.get(SOURCEDIR).relativize(location.getParent());
        permalink = config.getSiteBaseUrl().concat("/").concat(parentDir.resolve(getSlug()).toString()).concat("/");
        return permalink;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

}

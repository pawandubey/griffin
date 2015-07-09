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
import static com.pawandubey.griffin.DirectoryCrawler.EXCERPT_MARKER;
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

    public static final long serialVersionUID = 1L;

    private final String title;
    private final String author;
    private String excerpt;
    private final LocalDate date;
    private final String prettyDate;
    private final String location;
    private String content;
    private final String slug;
    private final String layout;
    private String permalink;
    private final String featuredImage;
    private final List<String> tags;

    /**
     * Creates a Post with the given paramenter.
     *
     * @param titl the post title
     * @param auth the post author
     * @param dat the post date
     * @param loc the post's Path
     * @param cont the post's content
     * @param image
     * @param slu the post slug
     * @param lay the layout
     * @param tag the list of tags
         */
    public Post(String titl, String auth, LocalDate dat,
                Path loc, String cont, String image, String slu, String lay, List<String> tag) {
        title = titl;
        author = auth;
        date = dat;
        prettyDate = date.format(DateTimeFormatter.ofPattern(config.getOutputDateFormat()));
        location = loc.toString();
        content = cont;
        slug = slu;
        layout = lay;
        tags = tag;
        featuredImage = image;
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
        return Paths.get(location);
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
        Path parentDir = Paths.get(SOURCE_DIRECTORY).relativize(Paths.get(location).getParent());
        permalink = Data.config.getSiteBaseUrl().concat("/").concat(parentDir.resolve(getSlug()).toString()).concat("/");
        return permalink;
    }

    /**
     * Sets the content of the post to the given String, replacing any excerpt
     * markers.. Also sets the excerpt of the post to till the first occurrence
     * of the excerpt marker.
     *
     * @param cont the String representing the new content.
     */
    @Override
    public void setContent(String cont) {
        this.content = cont.replace(EXCERPT_MARKER, "");
        int excInd = cont.indexOf(EXCERPT_MARKER);
//        System.out.println(excInd);
        excerpt = excInd > 0 ? cont.substring(0, excInd)
                  : cont.length() >= 255 ? cont.substring(0, 255) : cont;
    }

    /**
     * @return the list of tags for the post.
     */
    @Override
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @return the excerpt
     */
    @Override
    public String getExcerpt() {
        return excerpt;
    }

    /**
     *
     * @return the featured image, if exists, for this post.
     */
    @Override
    public Path getFeaturedImage() {
        return featuredImage != null ? Paths.get(featuredImage) : null;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Parsable)) {
            return false;
        }
        Parsable p = (Parsable) o;
        return p.getSlug().equals(this.getSlug());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.getSlug().hashCode();
        return hash;
    }

}

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

import java.nio.file.Path;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Page {

    private final String title;
    private final String author;
    private final Path location;
    private final String content;

    public Page(String titl, String auth, Path loc, String cont) {
        author = auth;
        title = titl;
        location = loc;
        content = cont;
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
     * @return the location
     */
    public Path getLocation() {
        return location;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

}

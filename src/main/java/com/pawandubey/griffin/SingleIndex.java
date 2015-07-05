/*
 * Copyright 2015 Pawan Dubey.
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

import static com.pawandubey.griffin.Data.config;
import static com.pawandubey.griffin.Indexer.totalIndexes;
import com.pawandubey.griffin.model.Parsable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class SingleIndex {

    private final String currentPage;
    private final String previousPage;
    private final String nextPage;
    private List<Parsable> posts;

    public SingleIndex(int c, int p, int n) {
        if (c == 1) {
            currentPage = config.getSiteBaseUrl();
            previousPage = "/";
            nextPage = config.getSiteBaseUrl().concat("/page/").concat("" + n);
        }
        else if (c == 2) {
            currentPage = config.getSiteBaseUrl().concat("/page/").concat("" + c);
            previousPage = config.getSiteBaseUrl();
            nextPage = config.getSiteBaseUrl().concat("/page/").concat("" + n);
        }
        else if (c == totalIndexes) {
            currentPage = config.getSiteBaseUrl().concat("/page/").concat("" + c);
            previousPage = config.getSiteBaseUrl().concat("/page/").concat("" + p);
            nextPage = "/";
        }
        else {
            currentPage = config.getSiteBaseUrl().concat("/page/").concat("" + c);
            previousPage = config.getSiteBaseUrl().concat("/page/").concat("" + p);
            nextPage = config.getSiteBaseUrl().concat("/page/").concat("" + n);;
        }
        posts = new ArrayList<>();
    }

    /**
     * @return the currentPage
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * @return the previousPage
     */
    public String getPreviousPage() {
        return previousPage;
    }

    /**
     * @return the nextPage
     */
    public String getNextPage() {
        return nextPage;
    }

    /**
     * @return the posts
     */
    public List<Parsable> getPosts() {
        return posts;
    }

}

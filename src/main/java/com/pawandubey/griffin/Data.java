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

import com.pawandubey.griffin.model.Parsable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Holds all the data objects which will be sent to the Renderer for parsing.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Data {
    static final LinkedBlockingQueue<Parsable> fileQueue = new LinkedBlockingQueue<>();
    static final List<Parsable> navPages = new ArrayList<>();
    static final List<Parsable> latestPosts = new ArrayList<>();
    public static final Configurator config = new Configurator();
    public static final Set<String> tags = new HashSet<>();
    public static final Data datum = new Data();

    /**
     * @return the fileQueue
     */
    public LinkedBlockingQueue<Parsable> getFileQueue() {
        return fileQueue;
    }

    /**
     * @return the navPages
     */
    public List<Parsable> getNavPages() {
        return navPages;
    }

    /**
     * @return the latestPosts
     */
    public List<Parsable> getLatestPosts() {
        return latestPosts;
    }

    /**
     * @return the config
     */
    public Configurator getConfig() {
        return config;
    }

    /**
     * @return the datum
     */
    public Data getDatum() {
        return datum;
    }
}

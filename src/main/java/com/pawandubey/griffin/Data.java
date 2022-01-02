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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * Holds all the data objects which will be sent to the Renderer for parsing.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Data {
    public static final LinkedBlockingQueue<Parsable> fileQueue = new LinkedBlockingQueue<>();
    public static final Set<Parsable> navPages = new HashSet<>();
    public static final Set<Parsable> latestPosts = new HashSet<>();
    public static final Configurator config = new Configurator();
    public static final ConcurrentMap<String, List<Parsable>> tags = new ConcurrentHashMap<>();
    public static final Data datum = new Data();
    public static final Set<ExecutorService> executorSet = new HashSet<>();

    /**
     * @return the fileQueue
     */
    public LinkedBlockingQueue<Parsable> getFileQueue() {
        return fileQueue;
    }

    /**
     * @return the navPages
     */
    public Set<Parsable> getNavPages() {
        return navPages;
    }

    /**
     * @return the latestPosts
     */
    public List<Parsable> getLatestPosts() {
        return latestPosts
                .stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
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

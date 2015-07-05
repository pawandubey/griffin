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
package com.pawandubey.griffin.cache;

import static com.pawandubey.griffin.Data.fileQueue;
import static com.pawandubey.griffin.Data.tags;
import static com.pawandubey.griffin.DirectoryCrawler.FILE_SEPARATOR;
import static com.pawandubey.griffin.DirectoryCrawler.ROOT_DIRECTORY;
import java.io.File;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Cacher {

    public static final String CACHE_PATH = ROOT_DIRECTORY + FILE_SEPARATOR + "cache.db";
    public final DB db;

    /**
     * Creates a new Cacher with a file database in the ROOT_FOLDER with
     * asyncWriteEnabled and transactionDisabled with JVM shutdown hook.
     */
    public Cacher() {
        db = DBMaker.newFileDB(new File(CACHE_PATH))
                .asyncWriteEnable()
                .closeOnJvmShutdown()
                .transactionDisable()
                .mmapFileEnableIfSupported()
                .make();
    }

    /**
     * Creates a cache of everything other than the fileQueue.
     */
    public void cacheTaggedParsables() {
        ConcurrentMap<String, Object> mainMap = db.getHashMap("mainMap");
        mainMap.put("tags", tags);
        db.commit();
    }

    /**
     * Caches the fileQueue before it goes for parsing.
     */
    public void cacheFileQueue() {
        ConcurrentMap<String, Object> mainMap = db.getHashMap("mainMap");
        mainMap.put("fileQueue", fileQueue);
    }

    /**
     * Fetches and returns the map from the cache.
     *
     * @return the cached map.
     */
    public ConcurrentMap<String, Object> readFromCacheIfExists() {
        return db.getHashMap("mainMap");
    }

    /**
     * Checks if the cache exists by checking if the map contains any elements.
     * This is necessary because the getHashMap method creates a new map if not
     * present instead of returning null.
     *
     * @return the HashMap
     */
    public boolean cacheExists() {
        return !db.getHashMap("mainMap").isEmpty();
    }

}

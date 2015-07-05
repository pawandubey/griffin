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
import static com.pawandubey.griffin.Data.fileQueue;
import com.pawandubey.griffin.model.Parsable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Handles creation and management of indexing the posts.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Indexer {

    private final List<SingleIndex> indexList;
    private final Queue<Parsable> queue;
    public static final Integer postsPerIndex = config.getIndexPosts();
    public static final int totalIndexes = (fileQueue.size() / postsPerIndex) + 1;

    public Indexer() {
        indexList = new ArrayList<>();
        queue = new PriorityQueue<>((a, b) -> {
            return b.getDate().compareTo(a.getDate());
        });
    }

    protected List<SingleIndex> getIndexList() {
        return this.indexList;
    }

    protected void initIndexes() {
        for (int i = 0; i < totalIndexes; i++) {
            indexList.add(new SingleIndex(i + 1, i , i + 2));
        }
    }

    protected void addToIndex(Parsable p) {
        queue.add(p);
    }

    /**
     * Sorts the index in the order of the date of publication of the posts.
     */
    protected void sortIndexes() {
        for (SingleIndex s : indexList) {
            for (int i = 0; i < postsPerIndex && !queue.isEmpty(); i++) {
                s.getPosts().add(queue.remove());
            }
        }
    }

   

}

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
package com.pawandubey;

import java.nio.file.Path;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Initializer {

    /**
     * Scaffolds out a new directory with the predefined Griffin directory
     * structure.
     *
     * @param rootPath the path at which the scaffolding has to take place
     * @param name the name to be given to the new directory
     * @return the path to the newly created directory
     */
    public Path scaffold(Path rootPath, String name) {
        //TODO implement
        return rootPath.resolve(name);
    }
}

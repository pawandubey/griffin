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
package com.pawandubey.griffin.renderer;

import com.pawandubey.griffin.Data;
import static com.pawandubey.griffin.DirectoryCrawler.FILE_SEPARATOR;
import static com.pawandubey.griffin.DirectoryCrawler.ROOT_DIRECTORY;
import com.pawandubey.griffin.SingleIndex;
import com.pawandubey.griffin.model.Parsable;
import java.io.IOException;
import java.util.List;

/**
 * Defines an interface for different renderers to implement for correctly
 * rendering a Griffin site.
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public interface Renderer {
    String ASSETS_FOLDER_NAME = "assets";
    String TEMPLATES_FOLDER_NAME = "templates";
    String templateRoot = ROOT_DIRECTORY + FILE_SEPARATOR
                          + ASSETS_FOLDER_NAME + FILE_SEPARATOR + TEMPLATES_FOLDER_NAME
                          + FILE_SEPARATOR + Data.config.getTheme();

    public String renderParsable(Parsable p) throws IOException;

    public String renderIndex(SingleIndex s) throws IOException;

    public String renderTagIndex(String tag, List<Parsable> list) throws IOException;

    public String renderSitemap() throws IOException;

    public String renderRssFeed() throws IOException;
}

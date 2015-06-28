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
package com.pawandubey.griffin;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import static com.pawandubey.griffin.DirectoryCrawler.FILE_SEPARATOR;
import static com.pawandubey.griffin.DirectoryCrawler.ROOT_DIRECTORY;
import static com.pawandubey.griffin.DirectoryCrawler.config;
import com.pawandubey.griffin.model.Parsable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Renderer {
    public final static String ASSETS_FOLDER_NAME = "assets";
    public final static String TEMPLATES_FOLDER_NAME = "templates";
    public final static String templateRoot = ROOT_DIRECTORY + FILE_SEPARATOR + ASSETS_FOLDER_NAME + FILE_SEPARATOR + TEMPLATES_FOLDER_NAME + FILE_SEPARATOR + config.getTheme();
    private final TemplateLoader loader = new FileTemplateLoader(templateRoot, ".html");
    private final Handlebars handlebar = new Handlebars(loader).with(new HighConcurrencyTemplateCache());
    private final Template postTemplate;
    private final Template pageTemplate;
    private final Template indexTemplate;

    /**
     * Creates a new Renderer instance and compiles the templates
     *
     * @throws IOException the exception
     */
    public Renderer() throws IOException {
        postTemplate = handlebar.compile("post");
        pageTemplate = handlebar.compile("page");
        indexTemplate = handlebar.compile("index");
    }

    /**
     * Renders the Parsable instance with the proper template according to its
     * layout.
     *
     * @param parsable the Parsable to be rendered.
     * @return the String representation of the rendering.
     * @throws IOException the exception
     */
    protected String renderParsable(Parsable parsable) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("config", config);
        map.put("post", parsable);
        map.put("navpages", InfoHandler.navPages);
        if (parsable.getLayout().equals("post")) {
            return postTemplate.apply(map);
        }
        else {
            return pageTemplate.apply(map);
        }
    }

    /**
     * Renders the index page for the site.
     *
     * @return the String representation of the rendering.
     * @throws IOException the exception
     */
    protected String renderIndex() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("config", config);
        map.put("latestposts", InfoHandler.latestPosts);
        map.put("navpages", InfoHandler.navPages);
        return indexTemplate.apply(map);
    }
}

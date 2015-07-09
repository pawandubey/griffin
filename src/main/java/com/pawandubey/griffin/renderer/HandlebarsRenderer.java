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
package com.pawandubey.griffin.renderer;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.pawandubey.griffin.Data;
import com.pawandubey.griffin.SingleIndex;
import com.pawandubey.griffin.model.Parsable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class HandlebarsRenderer implements Renderer {
    private final TemplateLoader loader = new FileTemplateLoader(templateRoot, ".html");
    private final Handlebars handlebar = new Handlebars(loader).with(new ConcurrentMapTemplateCache());
    private final Template postTemplate;
    private final Template pageTemplate;
    private final Template indexTemplate;
    private Template sitemapTemplate;
    private Template rssTemplate;
    private final Template tagTemplate;
    private Template notFoundTemplate;

    /**
     * Creates a new Renderer instance and compiles the templates
     *
     * @throws IOException the exception
     */
    public HandlebarsRenderer() throws IOException {
        postTemplate = handlebar.compile("post");
        pageTemplate = handlebar.compile("page");
        indexTemplate = handlebar.compile("index");
        tagTemplate = handlebar.compile("tagIndex");
        if (Files.exists(Paths.get(templateRoot, "SITEMAP.html"))) {
            sitemapTemplate = handlebar.compile("SITEMAP");
        }
        if (Files.exists(Paths.get(templateRoot, "feed.html"))) {
            rssTemplate = handlebar.compile("feed");
        }
        if (Files.exists(Paths.get(templateRoot, "404.html"))) {
            notFoundTemplate = handlebar.compile("404");
        }
    }

    /**
     * Renders the Parsable instance with the proper template according to its
     * layout.
     *
     * @param parsable the Parsable to be rendered.
     * @return the String representation of the rendering.
     * @throws IOException the exception
     */
    @Override
    public String renderParsable(Parsable parsable) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("data", Data.datum);
        map.put("post", parsable);
        map.put("config", Data.config);
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
     * @param s the single index
     * @return the String representation of the rendering.
     * @throws IOException the exception
     */
    @Override
    public String renderIndex(SingleIndex s) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("config", Data.config);
        map.put("data", Data.datum);
        map.put("index", s);
        return indexTemplate.apply(map);
    }

    /**
     * Renders the index page for each tag as supplied with the list containing
     * the posts tagged with the tag.
     *
     * @param tag the tag for the index
     * @param list the list of posts with the tag
     * @return the String representation of the index
     * @throws IOException the exception
     */
    @Override
    public String renderTagIndex(String tag, List<Parsable> list) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("tag", tag);
        map.put("posts", list);
        map.put("config", Data.config);
        map.put("data", Data.datum);
        return tagTemplate.apply(map);
    }

    /**
     * Renders the Sitemap.xml file
     *
     * @return the string representation of the Sitemap
     * @throws IOException
     */
    @Override
    public String renderSitemap() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("config", Data.config);
        map.put("data", Data.datum);
        return sitemapTemplate.apply(map);
    }

    /**
     * Renders the the rss feed
     *
     * @return the string representation of the feed.xml file
     * @throws IOException
     */
    @Override
    public String renderRssFeed() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("config", Data.config);
        map.put("data", Data.datum);
        return rssTemplate.apply(map);
    }

    @Override
    public String render404() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("config", Data.config);
        map.put("data", Data.datum);
        return notFoundTemplate.apply(map);
    }
}

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

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import static com.pawandubey.DirectoryCrawler.FILESEPARATOR;
import static com.pawandubey.DirectoryCrawler.ROOTDIR;
import static com.pawandubey.Griffin.config;
import com.pawandubey.model.Parsable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Renderer {

    public final static String templateRoot = ROOTDIR + FILESEPARATOR + "assets" + FILESEPARATOR + "templates/" + config.getTheme();
    private final TemplateLoader loader = new FileTemplateLoader(templateRoot, ".html");
    private final Handlebars handlebar = new Handlebars(loader);
    private final Template postTemplate;
    //private final Template pageTemplate;
    private final Template indexTemplate;

    public Renderer() throws IOException {
        postTemplate = handlebar.compile("post");
        //pageTemplate = handlebar.compile("page");
        indexTemplate = handlebar.compile("index");
    }

    protected String renderParsable(Parsable parsable, String content) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("post", parsable);
        map.put("content", content);
        return postTemplate.apply(map);
    }

    protected static String renderIndex() throws IOException {
        TemplateLoader loader = new FileTemplateLoader(templateRoot, ".html");
        Handlebars handlebar = new Handlebars(loader);
        Template template = handlebar.compile("index");
        Map<String, Object> map = new HashMap<>();
        map.put("config", config);
        map.put("latestposts", InfoHandler.latestPosts);
        return template.apply(map);
    }
}

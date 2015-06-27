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

import com.github.rjeschke.txtmark.BlockEmitter;
import java.util.List;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class CodeBlockEmitter implements BlockEmitter {

    @Override
    public void emitBlock(StringBuilder out, List<String> list, String meta) {
        out.append("<pre><code class= \"").append(meta).append("\">");
        for (final String s : list) {
            for (int i = 0; i < s.length(); i++) {
                final char c = s.charAt(i);
                switch (c) {
                    case '&':
                        out.append("&amp;");
                        break;
                    case '<':
                        out.append("&lt;");
                        break;
                    case '>':
                        out.append("&gt;");
                        break;
                    default:
                        out.append(c);
                        break;
                }
            }
            out.append('\n');
        }
        out.append("</code></pre>\n");
    }

}

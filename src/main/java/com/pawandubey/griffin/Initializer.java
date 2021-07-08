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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Pawan Dubey pawandubey@outlook.com
 */
public class Initializer {

    private final String zipp;// = Paths.get(ClassLoader.getSystemClassLoader().getResource(".").getPath()).getParent().toString();

    public Initializer() throws URISyntaxException {
        Path jarRootPath;

        jarRootPath = Paths.get(Initializer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        zipp = jarRootPath.toAbsolutePath().toString();           
        
    }

    /**
     * Scaffolds out a new directory with the predefined Griffin directory
     * structure.
     *
     * @param rootPath the path at which the scaffolding has to take place
     * @param name the name to be given to the new directory
     * @return the path to the newly created directory
     * @throws java.io.IOException the exception
     */
    public Path scaffold(Path rootPath, String name) throws IOException {
        //DirectoryCrawler.ROOT_DIRECTORY = rootPath.resolve(name).toAbsolutePath().normalize().toString();
        unzip(rootPath.resolve(name));
        return rootPath.resolve(name);
    }

    public void unzip(Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectory(targetDir);
        }
        final File file = new File(zipp + File.separator + "scaffold.zip");
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(file.toPath()))) {
            for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null; ) {
                Path resolvedPath = targetDir.resolve(ze.getName());
                if (ze.isDirectory()) {
                    Files.createDirectory(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    Files.copy(zipIn, resolvedPath);
                }
            }
        }
    }
}
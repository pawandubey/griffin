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
package com.pawandubey.griffin.cli;

import com.pawandubey.griffin.Griffin;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pawan Dubey pawandubey@outlook.com
 */
@Command(name = "new",
		description = "Scaffold out a new Griffin directory structure.")
public class NewCommand implements Callable<Integer> {

	@Parameters(description = "creates a new skeleton site at the given path", paramLabel = "<PATH>")
	public File file;

	@Option(names = {"--name", "-n"}, paramLabel = "<FOLDER_NAME>", description = "name of the directory to be created")
	private String name = "griffin";

	@Override
	public Integer call() {
		try {

			Griffin griffin = new Griffin(file.toPath().resolve(name));
			griffin.initialize(file.toPath(), name);
			System.out.println("Successfully created new site.");
		} catch (IOException | URISyntaxException ex) {
			Logger.getLogger(NewCommand.class.getName()).log(Level.SEVERE, null, ex);
			return -1;
		}
		return 0;
	}
}

# griffin
[![Build Status](https://travis-ci.org/pawandubey/griffin.svg)](https://travis-ci.org/pawandubey/griffin)
````
                       ___     ___
                __   /'___\  /'___\  __
   __    _ __  /\_\ /\ \__/ /\ \__/ /\_\     ___
 /'_ `\ /\`'__\\/\ \\ \ ,__\\ \ ,__\\/\ \  /' _ `\
/\ \L\ \\ \ \/  \ \ \\ \ \_/ \ \ \_/ \ \ \ /\ \/\ \
\ \____ \\ \_\   \ \_\\ \_\   \ \_\   \ \_\\ \_\ \_\
 \/___L\ \\/_/    \/_/ \/_/    \/_/    \/_/ \/_/\/_/
   /\____/
   \_/__/
````

Griffin is a very small, convenient, and **extremely fast** static site generator.
Griffin takes an opinionated approach to static site generation, making some decisions
for you to ensure that you get the best performance possible without any need for
complex command line fu. See usage demo below.

##Features
Griffin supports tags, pagination, live preview in your browser, social-media
support, easy theming with {{handlebars}} templates, syntax highlighting and more!
See the [full Feature list on the Wiki](http://github.com/pawandubey/griffin/wiki/Features) for more features.

##Usage Demo

[![asciicast](https://asciinema.org/a/egk1z7gb0mhjxvx7n3kdq1c03.png)](https://asciinema.org/a/egk1z7gb0mhjxvx7n3kdq1c03)

##Speed

Griffin has been made for speed. So it will never disappoint. Griffin can generate 5000 posts in around 8 seconds
on an average. That is less than 2 ms for each post. Although Griffin won't make much of a difference for the
first few posts, you'll appreciate its capacity to scale to thousands of posts without a major bump in parsing time.
Don't believe it? See for yourself below :

[![asciicast](https://asciinema.org/a/5z2srcn3f5j3cl0jnhganou5m.png)](https://asciinema.org/a/5z2srcn3f5j3cl0jnhganou5m)

##Installation

Grab the zip for the latest [release](http://github.com/pawandubey/griffin/releases) and unzip it somewhere.
Add the `bin` folder of the unzipped location to your PATH environment variable.
run `griffin --version` to check everything is working fine.


##Usage
Griffin is sub command based like git, svn etc. Each command is documented well.
````
griffin [subcommand] [options..] [arguments...]
````

However there are only three subcommands for three scenarios:

* To initiate a new Griffin site, use new:
````
griffin new [option] <path>
Options:

 <path>                   : creates a new skeleton site at the given path
 --help (-h)              : find help about this command (default: true)
 -name (-n) <folder_name> : name of the directory to be created (default: griffin)
````

* To parse your content, use publish:
````
griffin publish [option]
Options:

 --help (-h)    : find help about this command (default: true)
 --quick (-q)   : Publish only the files which have changed since the last modification. (default: false)
 --rebuild (-r) : Rebuild the site from scratch. This may take time for more number of posts. (default: false)
````

* To preview your site in your browser, use preview:
````
griffin preview [option]
Options:

 --help (-h)               : find help about this command (default: true)
 --port (-p) <port_number> : Port on which to launch the preview. Default to your configuredData. port. (default: 9090)
````

That is basically it. You write all your content in markdown. And run `griffin publish` and see the magic happen.


## License
Copyright 2015 Pawan Dubey pawandubey@outlook.com.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


# griffin
****
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
## Contribution guidelines
To contribute, 
* First time:
	* first `fork` this repository from the master branch.
    * then `git clone` your fork onto your local machine.
    * setup the remotes properly to pull the code from upstream:
        * `cd griffin`
        * do `git remote add upstream https://github.com/pawandubey/griffin.git`
        * verify you have both `origin` and `upstream` remotes by `git remote -v` 

* Before every contribution:
    * Start a new topic branch off master branch:
        * `cd griffin`
        * `git checkout master`
        * `git pull upstream master`
        * `git push origin master`
        * `git branch *<your branch name>*`
        * `git checkout *<your branch name>*`
    * Then commit all changes into your topic branch only. **This is important**.
    * And push to **your fork** on github. 
        * `git checkout *<your branch name>*`
        * `git push origin *<your branch name>*` 
    * Finalize your changes and prepare for sending pull request.

* Before sending pull request:
    * `git checkout master`
    * `git pull upstream master`
    * `git checkout *<your branch name>*`
    * `git rebase master`
    * `git push origin *<your branch name>*`
* Send a pull request on github. [See how](https://help.github.com/articles/using-pull-requests/)

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


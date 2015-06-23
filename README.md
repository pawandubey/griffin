# griffin
****
[![Build Status](https://travis-ci.org/pawandubey/griffin.svg)](https://travis-ci.org/pawandubey/griffin)

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
Licensed under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

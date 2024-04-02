# How to Build

## Prepare dependencies

* JDK 1.8 (JDK 1.9+ are not supported yet)
* On Linux Ubuntu system (e.g. Ubuntu 16.04.4 LTS), ensure that the machine has [__Oracle JDK 8__](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04), instead of having __Open JDK 8__ in the system. If you are building the source code by using __Open JDK 8__
* Open **UDP** ports for connection to the network
* **Minimum** 2 CPU Cores

## Getting the code with git

* Use Git from the console, see the [Setting up Git](https://help.github.com/articles/set-up-git/) and [Fork a Repo](https://help.github.com/articles/fork-a-repo/) articles.
* `develop` branch: the newest code 
* `master` branch: more stable than develop.
In the shell command, type:
  ```bash
  git clone https://github.com/tronprotocol/java-tron.git
  git checkout -t origin/master
  ```

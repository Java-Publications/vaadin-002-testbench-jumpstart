# Testbench Demo


## Docker
If you do not have Docker installed, you have to download and install it.
Official informatins about this you could 
find here [https://docs.docker.com/engine/installation/](https://docs.docker.com/engine/installation/)

The Docker installation is for the most OS a simple process, running the installer.
There is normally no aditional configuration needed.

After Docker is installed, you can start with a mvn clean install.
This will download the selenium images from Docker HUB and start them during the 
compile and test phase of this project. 

After e sucessful build, the docker instances of selenium are stopped again.
You can check this with the command docker ps. If you are not able to see 
entries like selenium/hub or selenium/node-[chrome, phantomjs,firefox]
everything was shutdown normally.

## Developer infos

If you want to work on single tests, and do not want to run a mvn clean install, you should do the follwing.

* start the Docker SeleniumHUB locally. For this you can use the mvn docker:start command.
* after the BUILD SUCCESS is shown, means that the SeleniumHUB is up and running, you can run single tests via IDE.

if you have any questions, ask me
 - Twitter @SvenRuppert
 - mail: sven@vaadin.com

Happy testing


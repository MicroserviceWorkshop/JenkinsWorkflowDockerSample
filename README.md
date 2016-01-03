# Sample for Jenkins Workflow plug-in and Docker

This sample makes use of the Jenkins Workflow plug-in and Docker to show how you can implement a continuous delivery pipeline.

## About Jenkins Workflow

The Jenkins Workflow feature allows you to write the build steps in a Groovy script instead of clicking the build together in a UI. To use that feature you need this plug-in:
https://wiki.jenkins-ci.org/display/JENKINS/Workflow+Plugin

### Pros
* The build script can be stored and versioned together with you source
* Allows you to handle pipelines in one build

### Cons
* The feature is still quite new
* You have to gather information from many different places to learn about it

### Links
Here are some of the links that might be useful:

* [Forum on Google Groups](https://groups.google.com/forum/#!topicsearchin/jenkinsci-users/workflow)
* [The main tutorial](https://github.com/jenkinsci/workflow-plugin/blob/master/TUTORIAL.md)
* [Workflow examples](https://github.com/jenkinsci/workflow-examples): it's a work in progress but contains a good [Best Practices](https://github.com/jenkinsci/workflow-examples/blob/master/docs/BEST_PRACTICES.md) document
* [GitHub repository of the workflow plug-in](https://github.com/jenkinsci/workflow-plugin): contains some of the documentation and you can find out about the current support for the different workflow steps
* [Docker image with Jenkins Workflow](https://github.com/jenkinsci/workflow-plugin/blob/master/demo/README.md): You can use this to play around with Jenkins Workflow without installing anything
* [Continuous Delivery with Jenkins Workflow](http://documentation.cloudbees.com/docs/cookbook/_continuous_delivery_with_jenkins_workflow.html): from the Jenkins Cookbook

## About Docker in Jenkins Workflow

To use Docker from within a Jenkins Workflow script you need this plug-in:
[CloudBees Docker Workflow Plugin](https://wiki.jenkins-ci.org/display/JENKINS/CloudBees+Docker+Workflow+Plugin).
This plug-in provides a `docker` variable to the scripts that can execute different Docker commands. Here is the [documentation for the Docker step](http://documentation.cloudbees.com/docs/cje-user-guide/docker-workflow.html). And if you want to know exactly what's in the `docker` variable, here is [its source](https://github.com/jenkinsci/docker-workflow-plugin/blob/master/src/main/resources/org/jenkinsci/plugins/docker/workflow/Docker.groovy).

## What is in this project

|Location        |Content|
|----------------|-------|
|books           | A simple REST service implemented using Spring Boot. This is our "application".|
|gradle          | The Gradle wrapper we use to build the stuff.|
|integrationtest | The scripts to execute the integration tests.|
|jenkins         | A docker image for a Jenkins build server that already contains the workflow plug-ins and the docker tools.|
|Jenkinsfile     | The Jenkins workflow script with the instructions for the continuous delivery pipeline. |

## How to make this work

Here are the steps that we are going into more detail below:

* First you need Docker on your computer
* Then we create and start the Jenkins docker image
* And finally configure a multibranch workflow build job

### Docker prerequisites

This is not a tutorial on Docker, so I assume you know how this stuff works.
Docker can be found on https://docker.com. Install it and make sure everything works fine.

#### Open docker daemon for TCP connections
On Linux the Docker tools communicate using non-networked sockets. But since we are accessing the Docker daemon from within another container, the daemon needs to be accessible via HTTP.

> WARNING: Opening the Docker daemon to HTTP is a security risk. Please do this only in a trusted network.

As described [here](https://docs.docker.com/engine/articles/configuring/#configuring-docker) you can configure different protocols in `/etc/default/docker`. We bind the TCP to all IPs on the machine by adding `-H tcp://0.0.0.0:2376` to `DOCKER_OPTS`.

    DOCKER_OPTS="-H tcp://0.0.0.0:2376 -H unix:///var/run/docker.sock"

Don't forget to restart the service. If your client needs some configuration maybe this [list of environment variables](https://docs.docker.com/engine/reference/commandline/cli/#environment-variables) might help.

The final test to see if it works is to query something via the Docker API.

    curl --get http://192.168.1.110:2376/version

Of course you use your own IP, but then you should see something like this:

    {"Version":"1.9.1","ApiVersion":"1.21","GitCommit":"a34a1d5",
    "GoVersion":"go1.4.2","Os":"linux","Arch":"amd64",
    "KernelVersion":"3.13.0-39-generic",
    "BuildTime":"Fri Nov 20 13:12:04 UTC 2015"}

#### Create and start the Jenkins Docker image

In the `jenkins` folder you find two scripts. One to build, and one to run the Docker image.

##### Build

The `Dockerfile` inherits form the `jenkins` base image, installs the Docker tools and downloads all the plugins defined in `plugins.txt`.

##### Run

The run script first tries to determine the IP address of the Docker daemon. This is necessary because we need to set the environment variable in the Jenkins server so that it knows where to find the Docker daemon.

> If it does not work, you might just edit the script and give it the correct IP.

When starting the new Docker container it maps the port `8080` and the `~/jenkins_home` from your computer to the container.

If the server is up and running you should be able to open it in a browser on http://localhost:8080 (or the IP of your Docker daemon instead of localhost).


### Configure a multibranch workflow build job

* On the Jenins server create a `New Item`
* Give it a sensible name
* Choose `Multibranch Workflow`
* And hit `Ok`
* Add a Git repository to `Branch Sources` and use the URL https://github.com/MicroserviceWorkshop/JenkinsWorkflowDockerSample.git
* Press `Save`
* And let the magic happen
 * First it does index the branches of the repository
 * It finds the `master` branch and starts to build it

If everything works fine the build will not finish. If you have a look at the log you should see something like:

    Do you want to deploy to the UAT system?
    Proceed or Abort

This is one of the `input` steps of the Jenkinsfile. Choose as you wish.

Well, if everything is blue (or gray if you aborted) you're done. Congratulations!

## The Jenkinsfile

This file contains the whole definition of the CI/CD pipeline. I hope it speaks for itself.

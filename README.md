# Sample for Jenkins Workflow plug-in and Docker

This sample makes use of the Jenkins Workflow plug-in and Docker to show how you can implement a continuous delivery pipeline.

## About Jenkins Workflow

The Jenkins Workflow feature allows you to write the build steps in a Groovy script instead of clicking the build together in a UI.

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

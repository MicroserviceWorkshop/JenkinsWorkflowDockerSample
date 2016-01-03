curl --get http://$1:$2/echo | grep "Hello World"

curl --get http://$1:$2/echo?name=JenkinsWorkflowDockerSample | grep "Hello JenkinsWorkflowDockerSample"

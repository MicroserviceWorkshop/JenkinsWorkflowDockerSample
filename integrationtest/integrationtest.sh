curl --get http://localhost:8080/echo | grep "Hello World"

curl --get http://localhost:8080/echo?name=JenkinsWorkflowDockerSample | grep "Hello JenkinsWorkflowDockerSample"

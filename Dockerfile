FROM java:openjdk-8u66-jre

MAINTAINER marco.a.poli@gmail.com

EXPOSE 8080

ADD build/libs/JenkinsWorkflowDockerSample-0.0.1-SNAPSHOT.jar /app/JenkinsWorkflowDockerSample.jar

CMD ["java", "-jar", "/app/JenkinsWorkflowDockerSample.jar"]

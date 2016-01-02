export MY_IP=`ip addr show eth0 | awk '/inet / {print $2}' | cut -d/ -f1`
echo My IP : $MY_IP

docker run -d -p 8080:8080 -e DOCKER_HOST=$MY_IP:2376 -v ~/jenkins_home:/var/jenkins_home polim/jenkins-playground

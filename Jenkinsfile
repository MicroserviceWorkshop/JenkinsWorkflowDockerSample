node {
    checkout scm

    def mvnHome = tool 'M3'
    sh "${mvnHome}/bin/mvn clean package"

}
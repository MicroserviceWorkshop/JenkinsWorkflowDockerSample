node {
    checkout scm

    def gradleHome = tool 'Gradle2.6'

    echo gradleHome

    sh "${gradleHome}/bin/gradle clean build"

}
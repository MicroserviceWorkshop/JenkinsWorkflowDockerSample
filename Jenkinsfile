node {
    checkout scm

    def gradleHome = tool 'Gradle2.6'
    echo gradleHome

    sh "${gradleHome}/bin/gradle clean build"
    archive 'build/libs/*.jar'
    step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/TEST-*.xml'])
}
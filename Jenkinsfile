#!groovy

main()

def void main() {
    commitStage()

    if (!isOnMaster()) {
        return;
    }

    integrationStage()

    timeout(time: 4, unit: 'DAYS') {
        input 'Do you want to deploy to the UAT system?'
    }
    userAcceptanceStage()

    timeout(time: 4, unit: 'DAYS') {
        input 'Do you want to deploy to the PRODUCTION system?'
    }
    productionStage()
}

/*
Compile
Execute unit tests
Run code analysis
Build and archive artifacts
 */
def void commitStage() {
    stage name: 'Commit Stage'

    node {
        sh "env"

        //checkout scm
        git 'https://github.com/MicroserviceWorkshop/JenkinsWorkflowDockerSample.git'

        sh "./gradlew clean"

        try {
            sh "./gradlew build"
        }
        finally {
            step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/TEST-*.xml'])
        }

        archive 'build/libs/*.jar'

        if (isOnMaster()) {
            stage name: 'Commit Stage - Build Docker Image', concurrency: 1

            docker.withServer('192.168.1.110:4243') {
                def image = docker.build "polim/JenkinsWorkflowDockerSample"
                // image.push('latest')
            }
        }
    }
}

/*
Provisionierung Testsysteme
Deployment auf Testsystemen
|| End to End Tests ausführen
|| Last Tests ausführen
|| Manuelle Tests ausführen und bestätigen
 */
def void integrationStage() {
    stage name: 'Integration Stage', concurrency: 3
}

/*
Deploy to the demo system
 */
def void userAcceptanceStage() {
    stage name: 'User Acceptance Stage', concurrency: 1
}

/*
Deploy to the production system
*/
def void productionStage() {
    stage name: 'Production Stage', concurrency: 1
}

private boolean isOnMaster() {
    env.BRANCH_NAME == 'master'
}

#!groovy

main()

private def void main() {
    checkEnvironment()

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

private void checkEnvironment() {
    node {
        sh "env"

        echo "Building on branch: ${env.BRANCH_NAME}"

        if (env.DOCKER_HOST) {
            echo "The DOCKER_HOST is ${env.DOCKER_HOST}"
        } else {
            error "You need to configure the DOCKER_HOST environment variable"
        }
    }
}

private def void commitStage() {
    stage name: 'Commit Stage'

    node {
        checkout scm
        // git 'https://github.com/MicroserviceWorkshop/JenkinsWorkflowDockerSample.git'

        sh "./gradlew -b books/build.gradle clean"

        try {
            sh "./gradlew -b books/build.gradle build"
        }
        finally {
            step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/TEST-*.xml'])
        }

        archive 'build/libs/*.jar'

        if (isOnMaster()) {
            stage name: 'Commit Stage - Build Docker Image', concurrency: 1

            dir('books') {
                docker.withServer(env.DOCKER_HOST) {
                    def image = docker.build "polim/jenkins_workflow_docker_sample"
                    // image.push('latest')
                }
            }
        }
    }
}

private def void integrationStage() {
    stage name: 'Integration Stage', concurrency: 1

    node {
        docker.withServer(env.DOCKER_HOST) {
            def image = docker.image "polim/jenkins_workflow_docker_sample"
            image.withRun("-p 12000:8080") {

            }
        }
    }
}

private def void userAcceptanceStage() {
    stage name: 'User Acceptance Stage', concurrency: 1
}

private def void productionStage() {
    stage name: 'Production Stage', concurrency: 1
}

private boolean isOnMaster() {
    return !env.BRANCH_NAME || env.BRANCH_NAME == 'master';
}

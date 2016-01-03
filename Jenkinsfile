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
        if (isScmConfigured()) {
            checkout scm
        }
        else {
            git 'https://github.com/MicroserviceWorkshop/JenkinsWorkflowDockerSample.git'
        }

        dir('books') {
            sh "../gradlew clean"

            try {
                sh "../gradlew build"
            }
            finally {
                step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/TEST-*.xml'])
            }

            archive 'build/libs/*.jar'

            if (isOnMaster()) {
                stage name: 'Commit Stage - Build Docker Image', concurrency: 1

                docker.withServer(env.DOCKER_HOST) {
                    def image = docker.build "polim/jenkins_workflow_docker_sample"
                    // image.push('latest')
                }
            }
        }

        stash(name: 'integrationtest', includes: 'integrationtest/*')
    }
}

private def void integrationStage() {
    stage name: 'Integration Stage', concurrency: 1

    node {
        unstash(name: 'integrationtest')

        docker.withServer(env.DOCKER_HOST) {
            def image = docker.image "polim/jenkins_workflow_docker_sample"
            def host = env.DOCKER_HOST.substring(0, env.DOCKER_HOST.indexOf(':'))
            echo "Test against host: ${host}"
            withEnv(["TESTSERVER=${host}"]) {
                // TODO use a dynamic port instead of 12k
                image.withRun('-p 12000:8080') { c ->
                    try {
                        // TODO this needs improvement. Try to detect when the server is ready.
                        sleep time: 20, unit: 'SECONDS'
                        sh 'integrationtest/integrationtest.sh $TESTSERVER 12000'
                    }
                    catch (e) {
                        echo "The test failed. See log above."
                        throw e
                    }
                }
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

private boolean isScmConfigured() {
    // if the SCM is not configured, then the branch name is null
    return env.BRANCH_NAME;
}
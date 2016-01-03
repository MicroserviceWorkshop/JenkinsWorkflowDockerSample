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
    stage name: 'Commit'

    node {
        if (isScmConfigured()) {
            // This is the path in a "multibranch workflow" job.
            checkout scm
        }
        else {
            // This is the path if you copy paste the script into a "workflow" job.
            // It simplifies development.
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
                stage name: 'Commit - Build Docker Image', concurrency: 1

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
    stage name: 'Integration', concurrency: 3

    node {
        docker.withServer(env.DOCKER_HOST) {
            def image = docker.image "polim/jenkins_workflow_docker_sample"
            def host = env.DOCKER_HOST.substring(0, env.DOCKER_HOST.indexOf(':'))
            echo "Test against host: ${host}"

            image.withRun('-P') { container ->
                def port = findPort(container)
                echo "Using port: ${port}"
                unstash(name: 'integrationtest')

                try {
                    // TODO this needs improvement. Try to detect when the server is ready.
                    sleep time: 10, unit: 'SECONDS'
                    sh "integrationtest/integrationtest.sh ${host} ${port}"
                }
                catch (e) {
                    echo "The test failed. See log above."
                    throw e
                }
            }
        }
    }
}

private def void userAcceptanceStage() {
    stage name: 'User Acceptance', concurrency: 1

    node {
        sh "docker -H ${env.DOCKER_HOST} stop JenkinsWorkflowDockerSample || true"
        sh "docker -H ${env.DOCKER_HOST} rm JenkinsWorkflowDockerSample || true"
        sh "docker -H ${env.DOCKER_HOST} run -d -p 9090:8080 --name JenkinsWorkflowDockerSample polim/jenkins_workflow_docker_sample"
    }
}

private def void productionStage() {
    stage name: 'Production', concurrency: 1

    // Do whatever is necessary
}

private boolean isOnMaster() {
    return !env.BRANCH_NAME || env.BRANCH_NAME == 'master';
}

private boolean isScmConfigured() {
    // if the SCM is not configured, then the branch name is null
    return env.BRANCH_NAME;
}

/**
 * Finds a specific port mapping.
 * The sample is from here: https://docs.docker.com/engine/reference/commandline/inspect/
 */
def findPort(container) {
    sh "docker inspect --format='{{(index (index .NetworkSettings.Ports \"8080/tcp\") 0).HostPort}}' ${container.id} > port"
    readFile('port').trim()
}

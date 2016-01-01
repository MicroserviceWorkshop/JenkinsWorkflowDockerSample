#!groovy

main()

def void main() {
    echo "Git branch: ${GIT_BRANCH}"
    commitStage()
    acceptanceTestStage()

    if (env.GIT_BRANCH != 'master') {
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
        //checkout scm
        git 'https://github.com/MicroserviceWorkshop/jwd_books.git'

        sh "./gradlew clean"

        try {
            sh "./gradlew build"
        }
        finally {
            step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/TEST-*.xml'])
        }

        archive 'build/libs/*.jar'
    }
}

/*
Run the slow feedback tests
 */
def void acceptanceTestStage() {
    stage name: 'Acceptance Test Stage'
}

/*
Provisionierung Testsysteme
Deployment auf Testsystemen
|| End to End Tests ausführen
|| Last Tests ausführen
|| Manuelle Tests ausführen und bestätigen
 */
def void integrationStage() {
    stage name: 'Integration Stage'
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
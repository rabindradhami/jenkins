def call() {
    pipeline {
        agent any

        environment {
            IMAGE_NAME = 'test:latest'
        }

        stages {
            stage('Checkout Code') {
                steps {
                    script {
                        // Get the user who started the job (username)
                        def cause = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)
                        RUN_BY = cause?.userName
                        echo "Job started by: ${RUN_BY}"
                    }

                    git branch: 'main', url: 'https://github.com/rabindradhami/raben-test-repo.git'
                }
            }

            stage('Approval') {
                steps {
                    input message: 'Please approve this step.', submitter: 'anotherPerson'
                }
            }

            stage('Test') {
                steps {
                    echo "Test"
                }
            }
        }
    }
}

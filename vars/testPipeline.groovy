def call(){
    pipeline {
        agent any

        environment {
            IMAGE_NAME = 'test:latest'  // Change this as needed
            RUN_BY = ''
        }

        stages {
            stage('Checkout Code') {
                steps {
                    script {
                        // Store the user who triggered the job
                        RUN_BY = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause).getUserId()
                        echo "Job started by: ${RUN_BY}"
                    }

                    git branch: 'main', url: 'https://github.com/rabindradhami/raben-test-repo.git'
                }
            }

            stage('Install Docker') {
                steps {
                    dockerInstall()
                }
            }

            stage('Build Docker Image') {
                steps {
                    dockerBuild(IMAGE_NAME, '.')
                }
            }

            stage('Approval') {
                steps {
                    script {
                        def userInput = input(
                            id: 'approvalInput', message: 'Do you approve this build?', parameters: []
                        )

                        def approvalUserId = currentBuild.rawBuild.getAction(org.jenkinsci.plugins.workflow.support.steps.input.InputAction)
                            .getExecutions()
                            .find { it.id == 'approvalInput' }
                            .getApprover()
                            .getId()

                        echo "Approval given by: ${approvalUserId}"

                        if (approvalUserId == RUN_BY) {
                            error "Approval cannot be given by the user who started the job."
                        }
                    }
                }
            }
        }
    }
}

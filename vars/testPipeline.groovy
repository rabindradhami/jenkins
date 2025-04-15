def call(){
    pipeline {
        agent any

        environment {
            IMAGE_NAME = 'test:latest'
            RUN_BY = ''
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

            // stage('Install Docker') {
            //     steps {
            //         dockerInstall()
            //     }
            // }

            // stage('Build Docker Image') {
            //     steps {
            //         dockerBuild(IMAGE_NAME, '.')
            //     }
            // }

            stage('Approval') {
                steps {
                    script {
                        input(id: 'approvalInput', message: 'Do you approve this build?')

                        // Safely fetch the approver's display name
                        def inputAction = currentBuild.rawBuild.getAction(org.jenkinsci.plugins.workflow.support.steps.input.InputAction)
                        def inputExecution = inputAction?.executions?.find { it.id == 'approvalInput' }
                        def approverName = inputExecution?.approver?.name

                        if (!approverName) {
                            error "Could not determine approver. Make sure a user clicked the Proceed button."
                        }

                        echo "Approval given by: ${approverName}"

                        if (approverName == RUN_BY) {
                            error "Approval cannot be done by the same user who started the job (${RUN_BY})."
                        }
                    }
                }
            }
        }
    }
}

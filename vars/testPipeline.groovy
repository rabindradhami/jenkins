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
                        // Get the user who triggered the build
                        def cause = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)
                        RUN_BY = cause?.getUserId()
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
                        // Prompt for manual approval and collect approver ID manually
                        def inputData = input(
                            id: 'approvalInput',
                            message: 'Do you approve this deployment?',
                            parameters: [
                                string(name: 'APPROVER', description: 'Enter your Jenkins username to approve')
                            ]
                        )

                        def approver = inputData
                        echo "Approval given by: ${approver}"

                        if (approver == RUN_BY) {
                            error "Approval cannot be performed by the same user who triggered the job (${RUN_BY})."
                        }
                    }
                }
            }
        }
    }
}

def call() {
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
                        // Capture the username of the person who started the job
                        def cause = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)
                        RUN_BY = cause?.userName ?: 'Unknown'
                        echo "Job started by: ${RUN_BY}"
                    }

                    git branch: 'main', url: 'https://github.com/rabindradhami/raben-test-repo.git'
                }
            }

            stage('Approval') {
                steps {
                    script {
                        // Pause for approval
                        def approver = input message: 'Please approve this step.'
                        // Capture the approver details
                        def APPROVED_BY = approver.getUser().toString() ?: 'Unknown'
                        echo "Approval granted by: ${APPROVED_BY}"

                        // Validate that the approver is not the job initiator
                        if (APPROVED_BY == RUN_BY) {
                            error("The approver (${APPROVED_BY}) cannot be the same as the job initiator (${RUN_BY}).")
                        }
                    }
                }
            }

            stage('Test') {
                steps {
                    echo "Running test stage"
                }
            }
        }
    }
}

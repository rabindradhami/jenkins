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

                    git branch: 'main', url: 'https://github.com/rabindradhami/raben-test-repo.git'
                }
            }
        }

        stage('Approval') {
            steps {
                script {
                    // Define the function within the same script
                    def validateApprover = { build, starter ->
                        // Pause for approval
                        input message: 'Please approve this step.'

                        // Get the approver's username
                        def approverCause = build.rawBuild.getCause(hudson.model.Cause$UserIdCause)
                        def approver = approverCause?.userName ?: 'Unknown'

                        // Validate the approver
                        if (approver == starter) {
                            error("The approver (${approver}) cannot be the same as the job initiator (${starter}).")
                        }
                        return approver
                    }

                    // Call the function and validate approval
                    def approver = validateApprover(currentBuild, RUN_BY)
                    echo "Approval granted by: ${approver}"
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

def call() {
    pipeline {
        agent any

        environment {
            INITIATOR_USER_ID = ''
            APPROVER_USER_ID  = ''
        }

        stages {
            stage('Get Initiator') {
                steps {
                    script {
                        // Capture the build initiator
                        wrap([$class: 'BuildUser']) {
                            env.INITIATOR_USER_ID = env.BUILD_USER_ID
                            echo "Initiated by: ${env.INITIATOR_USER_ID} (${env.BUILD_USER})"
                        }
                    }
                }
            }

            stage('Wait for Approval') {
                steps {
                    script {
                        // Manual approval step
                        input message: 'Do you approve this action?', ok: 'Approve'
                    }
                }
            }

            stage('Get Approver') {
                steps {
                    script {
                        // After input approval, the approver becomes the current BUILD_USER
                        wrap([$class: 'BuildUser']) {
                            env.APPROVER_USER_ID = env.BUILD_USER_ID
                            echo "Approved by: ${env.APPROVER_USER_ID} (${env.BUILD_USER})"
                        }

                        // Check if initiator and approver are the same
                        if (env.INITIATOR_USER_ID == env.APPROVER_USER_ID) {
                            error("Approval failed: Initiator and approver cannot be the same user.")
                        } else {
                            echo "Approval successful by a different user."
                        }
                    }
                }
            }
        }

        post {
            success {
                echo "✅ Pipeline succeeded."
            }
            failure {
                echo "❌ Pipeline failed due to same-user approval."
            }
        }
    }
}

call()

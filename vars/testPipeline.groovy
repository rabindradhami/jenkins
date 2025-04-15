def call() {
    pipeline {
        agent any

        stages {
            stage('Get Initiator') {
                steps {
                    script {
                        wrap([$class: 'BuildUser']) {
                            // Assign to a global variable (not environment) to retain its value
                            INITIATOR_USER_ID = env.BUILD_USER_ID
                            echo "Initiated by: ${INITIATOR_USER_ID} (${env.BUILD_USER})"
                        }
                    }
                }
            }

            stage('Approval') {
                steps {
                    script {
                        // Capture the approver's user ID into a variable
                        APPROVER_USER_ID = input(
                            message: 'Do you approve this action?',
                            ok: 'Approve',
                            submitterParameter: 'APPROVER'
                        )
                    }
                }
            }

            stage('Validate Approver') {
                steps {
                    script {
                        echo "Initiator: ${INITIATOR_USER_ID}"
                        echo "Approver : ${APPROVER_USER_ID}"

                        if (INITIATOR_USER_ID == APPROVER_USER_ID) {
                            error("❌ Approval failed: Initiator and approver cannot be the same user.")
                        } else {
                            echo "✅ Approval succeeded: Different user approved the build."
                        }
                    }
                }
            }
        }

        post {
            success {
                echo "🎉 Pipeline succeeded."
            }
            failure {
                echo "❌ Pipeline failed due to same-user approval."
            }
        }
    }
}

call()

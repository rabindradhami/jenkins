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
                        wrap([$class: 'BuildUser']) {
                            env.INITIATOR_USER_ID = env.BUILD_USER_ID
                            echo "Initiated by: ${env.INITIATOR_USER_ID} (${env.BUILD_USER})"
                        }
                    }
                }
            }

            stage('Approval') {
                steps {
                    script {
                        // The submitter's Jenkins ID will be assigned to APPROVER_USER_ID
                        input(
                            message: 'Do you approve this action?',
                            ok: 'Approve',
                            submitterParameter: 'APPROVER_USER_ID'
                        )
                    }
                }
            }

            stage('Validate Approver') {
                steps {
                    script {
                        echo "Initiator: ${env.INITIATOR_USER_ID}"
                        echo "Approver : ${env.APPROVER_USER_ID}"

                        if (env.INITIATOR_USER_ID == env.APPROVER_USER_ID) {
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

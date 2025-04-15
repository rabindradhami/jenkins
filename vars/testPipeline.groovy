def call() {
    pipeline {
        agent any
        
        stages {
            stage('Trigger Job') {
                steps {
                    script {
                        // Wrap with BuildUser to get the user who triggered the build
                        wrap([$class: 'BuildUser']) {
                            def initiatorUser = env.BUILD_USER_ID
                            echo "Job triggered by: ${initiatorUser}"
                        }
                    }
                }
            }
            
            stage('Approval Check') {
                steps {
                    script {
                        // Wrap with BuildUser to get the user who triggered the build
                        wrap([$class: 'BuildUser']) {
                            def initiatorUser = env.BUILD_USER_ID
                            
                            // Wait for user approval
                            def approverUser = input message: 'Do you approve this action?', ok: 'Approve'
                            echo "Approval received from: ${approverUser}"

                            // Compare the initiator and approver
                            if (initiatorUser == approverUser) {
                                // If the same user approves, fail the build
                                currentBuild.result = 'FAILURE'
                                error("The initiator cannot approve the job.")
                            } else {
                                // If a different user approves, succeed the build
                                currentBuild.result = 'SUCCESS'
                                echo "Job approved by a different user: ${approverUser}"
                            }
                        }
                    }
                }
            }
        }
        
        post {
            success {
                echo 'Job completed successfully!'
            }
            failure {
                echo 'Job failed due to same user approval!'
            }
        }
    }
}

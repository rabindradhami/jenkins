def call() {
    pipeline {
        agent any

        stages {
            stage('Approval Flow') {
                steps {
                    script {
                        checkApproval()
                    }
                }
            }
        }

        // post {
        //     success {
        //         echo "🎉 Pipeline succeeded."
        //     }
        //     failure {
        //         echo "❌ Pipeline failed due to same-user approval."
        //     }
        // }
    }
}

call()
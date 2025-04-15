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
                    input message: 'Please approve this step.', submitter: 'anotherPerson'
            }
            stage('Test'){
                steps{
                    echo "Pipeline"
                }
            }    
        }
    }
}
}
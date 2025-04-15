def call(){
    pipeline {
        agent any

        environment {
            IMAGE_NAME = 'test:latest'
            RUN_BY = ''
            APPROVED_BY = ''
        }

        stages {
            stage('Checkout Code') {
                steps {
                    script {
                        def cause = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)
                        RUN_BY = cause?.userName
                        echo "Job started by: ${RUN_BY}"
                    }

                    git branch: 'main', url: 'https://github.com/rabindradhami/raben-test-repo.git'
                }
            }

            stage('Approval') {
                steps {
                    script {
                        input(
                            message: 'Do you approve this build?',
                            ok: 'Approve',
                            submitterParameter: 'APPROVED_BY'
                        )

                        def approvedBy = env.APPROVED_BY
                        echo "Approval given by: ${approvedBy}"

                        if (approvedBy == RUN_BY) {
                            error "Approval cannot be done by the same user who started the job (${RUN_BY})."
                        }
                    }
                }
            }
        }
    }
}

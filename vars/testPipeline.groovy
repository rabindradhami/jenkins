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
                        def dummyInput = input(
                            message: 'Do you approve this build?',
                            ok: 'Approve',
                            submitterParameter: 'APPROVER_USER'
                        )
                        def approvedBy = env.APPROVER_USER

                        echo "Approval given by: ${approvedBy}"

                        if (!approvedBy) {
                            error "Could not determine who approved the job."
                        }

                        if (approvedBy == RUN_BY) {
                            error "Approval cannot be done by the same user who started the job (${RUN_BY})."
                        }
                    }
                }
            }
        }
    }
}

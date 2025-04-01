def call() {
    pipeline {
        agent any

        stages {
            stage('Build') {
                steps {
                    echo 'Building the project...'
                }
            }
            
            stage('Test') {
                steps {
                    echo 'Running tests...'
                }
            }

            stage('Deploy') {
                steps {
                    echo 'Deploying the project...'
                }
            }

            stage('Hello') {
                steps {
                    helloPrint()
                }
            }
        }
    }
}

// def call() {
    pipeline {
        agent any

        stages {
            stage('Build') {
                steps {
                    scripts{
                    echo 'Building the project...'
                    }
                }
            }
            
            stage('Test') {
                steps {
                    scripts {
                    echo 'Running tests...'
                    }
                }
            }

            stage('Deploy') {
                steps {
                    scripts {
                    echo 'Deploying the project...'
                    }
                }
            }

            // stage('Hello') {
            //     steps {
            //         scripts{
            //         helloPrint()
            //         }
            //     }
            // }
        }
    }
// }

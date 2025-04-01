def call(){
pipeline {
    agent any

    environment {
        IMAGE_NAME = 'test:latest'  // Change this as needed
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/rabindradhami/raben-test-repo.git'
            }
        }
        
        stage('Install Docker') {
            steps {
                dockerInstall()
            }
        }
        
        stage('Build Docker Image') {
            steps {
                dockerBuild(IMAGE_NAME, '.')
            }
        }
    }
 }
}
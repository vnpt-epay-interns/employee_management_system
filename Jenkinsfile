pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }

    tools{
        maven 'apache-maven-3.8.7'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                sh 'sudo docker build -t ems-be:lts3 .'
                sh 'sudo docker run -d -p 8080:8080 ems-be:lts3'
            }
        }

    }
}
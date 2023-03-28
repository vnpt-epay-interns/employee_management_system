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
                echo 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker build -t myapp .'
                sh 'docker run -d -p 8080:8080 myapp'
            }
        }

    }
}
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'ems-be'
        DOCKER_TAG = 'lts'
    }

    triggers {
        pollSCM '* * * * *'
    }

    tools{
        maven 'maven'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -v'
                sh 'mvn clean package'
                sh 'docker rmi -f $DOCKER_IMAGE || true'
                sh 'docker build -t $DOCKER_IMAGE:$DOCKER_TAG .'
            }
        }
        stage('Test') {
            steps {
                sh 'docker rm -f $DOCKER_IMAGE || true'
                sh 'docker run -d -p 8080:8080 --name $DOCKER_IMAGE $DOCKER_IMAGE:$DOCKER_TAG'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker stop $DOCKER_IMAGE || true'
                sh 'docker rm -f $DOCKER_IMAGE || true'
                sh 'docker run -d -p 8080:8080 --name $DOCKER_IMAGE $DOCKER_IMAGE:$DOCKER_TAG'
            }
        }
    }
}
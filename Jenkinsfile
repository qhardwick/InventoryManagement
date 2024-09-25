pipeline {
    agent any
    environment {
        BACKEND_EC2_IP = '3.95.37.62' // Replace with your backend EC2 instance IP
//         DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials-id')
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/qhardwick/InventoryManagement.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("tylercausey/spring-backend:latest")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    // Manually login to Docker Hub using shell command
                    sh 'docker login -u tylercausey -p dckr_pat_wqSj0-F1Eo32rfpUwNIhh_BTgy8'

                    // Push the Docker image
                    dockerImage.push('latest')
                }
            }
        }
        stage('Deploy to Backend EC2') {
            steps {
                sshagent(['11111']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ec2-user@${BACKEND_EC2_IP} '
                    docker pull tylercausey/spring-backend:latest &&
                    docker stop backend-container || true &&
                    docker rm backend-container || true &&
                    docker run -d --name backend-container -p 8080:8080 -e databaseUrl=jdbc:postgresql://taq-database.cluster-c4eqo06kg56i.us-east-1.rds.amazonaws.com:5432/postgres -e databaseUser=postgres -e databasePass=postgres tylercausey/spring-backend:latest'
                    """
                }
            }
        }
    }
}

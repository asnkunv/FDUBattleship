pipeline {
    agent any

    stages {
        stage('Clone') {
            steps {
                git branch: 'ci/cd-pipeline',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/asnkunv/FDUBattleship.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t battleship-app .'
            }
        }

        stage('Stop Old Container') {
            steps {
                sh 'docker stop battleship || true'
                sh 'docker rm battleship || true'
            }
        }

        stage('Run New Container') {
            steps {
                sh 'docker run -d --name battleship -p 8090:8090 battleship-app'
            }
        }
    }
}
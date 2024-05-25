pipeline {
    agent any

    tools {
        maven 'my-maven'
    }
    environment {
        MYSQL_ROOT_LOGIN = credentials('mysql-root-login')
    }
    stages {
        stage('Build with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Packaging/Pushing image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t thanhtungeric/springboot .'
                    sh 'docker push thanhtungeric/springboot'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'mysql-root-login', usernameVariable: 'MYSQL_ROOT_LOGIN_USR', passwordVariable: 'MYSQL_ROOT_LOGIN_PSW')]) {
                        sh 'docker-compose down -v || echo "No containers to stop"'
                        sh 'docker-compose pull'
                        sh 'MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_LOGIN_PSW} docker-compose up -d'
                        sh 'sleep 20'
                        sh "docker exec -i thanhtung-mysql mysql --user=root --password=${MYSQL_ROOT_LOGIN_PSW} < script"
                    }
                }
            }
        }
    }
    post {
        // Clean after build
        always {
            cleanWs()
        }
    }
}

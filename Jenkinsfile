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

    post {
        // Clean after build
        always {
            cleanWs()
        }
    }
}

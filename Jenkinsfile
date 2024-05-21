pipeline {
    agent any

    tools {
        maven 'my-maven'
        jdk 'my-java-17'
    }
    environment {
        MYSQL_ROOT_LOGIN = credentials('MYSQL')
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
                withDockerRegistry(credentialsId: 'DOCKERHUB', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t thanhtungeric/springboot .'
                    sh 'docker push thanhtungeric/springboot'
                }
            }
        }

        stage('Deploy MySQL to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull mysql:8.0'
                sh 'docker network create dev || echo "this network exists"'
                sh 'docker container stop thanhtung-mysql || echo "this container does not exist" '
                sh 'echo y | docker container prune'
                sh 'docker volume rm thanhtung-mysql-data || echo "no volume"'

                sh "docker run --name thanhtung-mysql --rm --network dev -v thanhtung-mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_LOGIN} -e MYSQL_DATABASE=db_example -d mysql:8.0"
                sh 'sleep 20'
                sh "docker exec -i thanhtung-mysql mysql --user=root --password=${MYSQL_ROOT_LOGIN} < script"
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull thanhtungeric/springboot'
                sh 'docker container stop thanhtung-springboot || echo "this container does not exist" '
                sh 'docker network create dev || echo "this network exists"'
                sh 'echo y | docker container prune'

                sh 'docker container run -d --rm --name thanhtung-springboot -p 8081:8080 --network dev thanhtungeric/springboot'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}

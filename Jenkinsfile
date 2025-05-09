pipeline {
    agent any
    
    environment {
        AWS_REGION = 'us-east-1'  // Change to your AWS region
        ECR_REPOSITORY_URI = '${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/mis-backend'
        IMAGE_TAG = "build-${BUILD_NUMBER}"
        DEPLOYMENT_TYPE = 'ecs'  // Options: 'ecs', 'eb' (Elastic Beanstalk)
        ECS_CLUSTER = 'mis-cluster'
        ECS_SERVICE = 'mis-backend-service'
        EB_APP_NAME = 'mis-backend'
        EB_ENV_NAME = 'mis-backend-env'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        
        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${ECR_REPOSITORY_URI}:${IMAGE_TAG}")
                }
            }
        }
        
        stage('Push to ECR') {
            steps {
                script {
                    withAWS(credentials: 'aws-credentials', region: "${AWS_REGION}") {
                        sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY_URI}"
                        sh "docker push ${ECR_REPOSITORY_URI}:${IMAGE_TAG}"
                        // Tag as latest for convenience
                        sh "docker tag ${ECR_REPOSITORY_URI}:${IMAGE_TAG} ${ECR_REPOSITORY_URI}:latest"
                        sh "docker push ${ECR_REPOSITORY_URI}:latest"
                    }
                }
            }
        }
        
        stage('Deploy to AWS') {
            steps {
                script {
                    withAWS(credentials: 'aws-credentials', region: "${AWS_REGION}") {
                        if (env.DEPLOYMENT_TYPE == 'ecs') {
                            // Deploy to ECS
                            sh """
                                aws ecs update-service --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --force-new-deployment
                            """
                        } else if (env.DEPLOYMENT_TYPE == 'eb') {
                            // Deploy to Elastic Beanstalk
                            sh """
                                # Update Dockerrun.aws.json with the new image tag
                                sed -i 's|"Name": ".*"|"Name": "${ECR_REPOSITORY_URI}:${IMAGE_TAG}"|g' Dockerrun.aws.json
                                # Deploy to Elastic Beanstalk
                                aws elasticbeanstalk update-environment --application-name ${EB_APP_NAME} --environment-name ${EB_ENV_NAME} --version-label ${IMAGE_TAG}
                            """
                        }
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline execution failed!'
        }
        always {
            // Clean up Docker images to save space
            sh "docker rmi ${ECR_REPOSITORY_URI}:${IMAGE_TAG} || true"
            sh "docker rmi ${ECR_REPOSITORY_URI}:latest || true"
        }
    }
}

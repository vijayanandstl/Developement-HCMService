pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'docker.io'  // Docker Hub registry
        DOCKER_IMAGE = 'techbu/candidate-service'  // Updated to include username
        DOCKER_TAG = "${BUILD_NUMBER}"
        KUBE_NAMESPACE = 'hcm'
        DOCKER_USER = 'techbu'
        DOCKER_PASS = 'techbu@Stl2025'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Docker Build') {
            steps {
                sh '''
                    docker login -u $DOCKER_USER -p $DOCKER_PASS
                    docker build -t $DOCKER_IMAGE:$DOCKER_TAG .
                    docker push $DOCKER_IMAGE:$DOCKER_TAG
                '''
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Using kubeconfig from Jenkins credentials
                    withCredentials([file(credentialsId: 'kubeconfig-jenkins', variable: 'KUBECONFIG')]) {
                        // Update deployment with new image
                        sh """
                            kubectl --kubeconfig=$KUBECONFIG set image deployment/candidate-service \
                                candidate-service=$DOCKER_IMAGE:$DOCKER_TAG \
                                -n $KUBE_NAMESPACE
                            
                            # Verify deployment
                            kubectl --kubeconfig=$KUBECONFIG rollout status deployment/candidate-service -n $KUBE_NAMESPACE
                        """
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Cleanup Docker credentials
            sh 'docker logout'
            
            // Cleanup workspace
            cleanWs()
        }
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
} 
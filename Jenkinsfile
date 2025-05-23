pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'docker.io'  // Docker Hub registry
        DOCKER_IMAGE = 'techbu/hcm'  // Updated to new repository name
        DOCKER_TAG = "${BUILD_NUMBER}"
        KUBE_NAMESPACE = 'hcm'
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
                withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        docker login -u $DOCKER_USER -p $DOCKER_PASS
                        docker build -t $DOCKER_IMAGE:$DOCKER_TAG .
                        docker push $DOCKER_IMAGE:$DOCKER_TAG
                    '''
                }
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
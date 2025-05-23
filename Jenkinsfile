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
                        echo "Logging into Docker Hub..."
                        docker login -u $DOCKER_USER -p $DOCKER_PASS || exit 1
                        
                        echo "Building Docker image..."
                        docker build -t $DOCKER_IMAGE:$DOCKER_TAG . || exit 1
                        
                        echo "Tagging latest image..."
                        docker tag $DOCKER_IMAGE:$DOCKER_TAG $DOCKER_IMAGE:latest || exit 1
                        
                        echo "Pushing versioned image..."
                        docker push $DOCKER_IMAGE:$DOCKER_TAG || exit 1
                        
                        echo "Pushing latest image..."
                        docker push $DOCKER_IMAGE:latest || exit 1
                    '''
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Using kubeconfig from Jenkins credentials
                    withCredentials([file(credentialsId: 'kubeconfig-jenkins', variable: 'KUBECONFIG')]) {
                        sh """
                            echo "Updating kustomization with new image tag..."
                            cd k8s/candidate-deployment
                            kustomize edit set image techbu/hcm:$DOCKER_TAG || exit 1
                            
                            echo "Applying kustomization..."
                            kubectl --kubeconfig=$KUBECONFIG apply -k . || exit 1
                            
                            echo "Waiting for deployment to complete..."
                            kubectl --kubeconfig=$KUBECONFIG rollout status deployment/candidate-service -n $KUBE_NAMESPACE || exit 1
                            
                            echo "Deployment completed successfully!"
                        """
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Cleanup Docker credentials
            sh 'docker logout || true'
            
            // Cleanup workspace
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed! Check the logs for details.'
        }
    }
} 
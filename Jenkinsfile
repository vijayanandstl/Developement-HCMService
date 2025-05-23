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
                        echo "$DOCKER_PASS" | docker login -u $DOCKER_USER --password-stdin || exit 1
                        
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
                    withCredentials([file(credentialsId: 'kubeconfig-jenkins', variable: 'KUBECONFIG')]) {
                        sh """
                            echo "Verifying cluster access..."
                            kubectl --kubeconfig=$KUBECONFIG cluster-info || exit 1
                            
                            echo "Creating namespace if it doesn't exist..."
                            kubectl --kubeconfig=$KUBECONFIG create namespace $KUBE_NAMESPACE --dry-run=client -o yaml | kubectl --kubeconfig=$KUBECONFIG apply -f - || exit 1
                            
                            echo "Creating deployment if it doesn't exist..."
                            cat <<EOF | kubectl --kubeconfig=$KUBECONFIG apply -f -
                            apiVersion: apps/v1
                            kind: Deployment
                            metadata:
                              name: candidate-service
                              namespace: $KUBE_NAMESPACE
                            spec:
                              replicas: 1
                              selector:
                                matchLabels:
                                  app: candidate-service
                              template:
                                metadata:
                                  labels:
                                    app: candidate-service
                                spec:
                                  containers:
                                  - name: candidate-service
                                    image: $DOCKER_IMAGE:$DOCKER_TAG
                                    ports:
                                    - containerPort: 8080
                                    resources:
                                      requests:
                                        memory: "256Mi"
                                        cpu: "200m"
                                      limits:
                                        memory: "512Mi"
                                        cpu: "500m"
                            EOF
                            
                            echo "Creating service if it doesn't exist..."
                            cat <<EOF | kubectl --kubeconfig=$KUBECONFIG apply -f -
                            apiVersion: v1
                            kind: Service
                            metadata:
                              name: candidate-service
                              namespace: $KUBE_NAMESPACE
                            spec:
                              selector:
                                app: candidate-service
                              ports:
                              - port: 80
                                targetPort: 8080
                              type: ClusterIP
                            EOF
                            
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
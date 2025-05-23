#!/bin/bash

# Create Docker credentials
echo "Creating Docker credentials..."
curl -X POST \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "": "0",
    "credentials": {
      "scope": "GLOBAL",
      "id": "docker-credentials",
      "username": "techbu",
      "password": "techbu@Stl2025",
      "description": "Docker Hub credentials",
      "$class": "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl"
    }
  }' \
  http://localhost:8080/credentials/store/system/domain/_/createCredentials

# Create Kubernetes config credentials
echo "Creating Kubernetes config credentials..."
curl -X POST \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "": "0",
    "credentials": {
      "scope": "GLOBAL",
      "id": "kubeconfig-jenkins",
      "description": "Kubernetes Config File",
      "file": "base64-encoded-kubeconfig",
      "$class": "org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl"
    }
  }' \
  http://localhost:8080/credentials/store/system/domain/_/createCredentials

echo "Credentials setup completed!" 
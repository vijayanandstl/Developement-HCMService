#!/bin/bash

# Create Docker credentials
echo "Creating Docker credentials..."
echo 'techbu' | jenkins-cli create-credentials-by-xml system::system::jenkins _ \
    < <(cat <<EOF
<com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl>
  <scope>GLOBAL</scope>
  <id>docker-credentials</id>
  <description>Docker Registry Credentials</description>
  <username>techbu</username>
  <password>techbu@Stl2025</password>
</com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl>
EOF
)

# Create Kubernetes credentials
echo "Creating Kubernetes credentials..."
echo 'kubeconfig' | jenkins-cli create-credentials-by-xml system::system::jenkins _ \
    < <(cat <<EOF
<org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl>
  <scope>GLOBAL</scope>
  <id>kubeconfig-jenkins</id>
  <description>Kubernetes Config File</description>
  <fileName>kubeconfig</fileName>
  <contentBase64>$(base64 -w 0 ~/.kube/config)</contentBase64>
</org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl>
EOF
)

echo "Credentials setup completed!" 
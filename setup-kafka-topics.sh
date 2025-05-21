#!/bin/bash

# Connect to Kafka pod
KAFKA_POD=$(kubectl get pods -n kafka -l app=kafka -o jsonpath="{.items[0].metadata.name}")

# List of topics to create
TOPICS=(
    "candidate-created"
    "candidate-updated"
    "candidate-deleted"
    "candidate-education-created"
    "candidate-work-history-created"
    "candidate-certification-created"
    "skill-created"
    "candidate-skill-created"
)

# Create each topic
for topic in "${TOPICS[@]}"; do
    echo "Creating topic: $topic"
    kubectl exec -n kafka $KAFKA_POD -- kafka-topics.sh --create \
        --bootstrap-server localhost:9092 \
        --topic $topic \
        --partitions 1 \
        --replication-factor 1 \
        --if-not-exists
done

# List all topics to verify
echo "Listing all topics:"
kubectl exec -n kafka $KAFKA_POD -- kafka-topics.sh --list --bootstrap-server localhost:9092 
#!/bin/bash

# Load environment variables from .env file
if [ -f "$(dirname "$0")/../config/.env" ]; then
    set -a
    source "$(dirname "$0")/../config/.env"
    set +a
fi

# Set defaults if not provided
export DB_HOST=${DB_HOST:-localhost}
export DB_PORT=${DB_PORT:-3306}
export DB_NAME=${DB_NAME:-social_media}
export DB_USERNAME=${DB_USERNAME:-root}
export SERVER_PORT=${SERVER_PORT:-8080}
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-dev}

# Verify required environment variables
if [ -z "$DB_PASSWORD" ]; then
    echo "❌ ERROR: DB_PASSWORD is not set. Please set it in config/.env"
    exit 1
fi

if [ -z "$JWT_SECRET" ]; then
    echo "❌ ERROR: JWT_SECRET is not set. Please set it in config/.env"
    exit 1
fi

echo "✅ Starting application with environment variables from config/.env"

# Start the application
cd "$(dirname "$0")/.."
java -jar target/webapplication-0.0.1-SNAPSHOT.war

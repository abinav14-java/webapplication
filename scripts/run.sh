#!/bin/bash

# Load environment variables
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=social_media
export DB_USERNAME=root
export DB_PASSWORD='***REMOVED***'
export JWT_SECRET=x7kL9pQ2mN8vB5cJ4wR6sT1uD3fG0hE9
export SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=dev

# Start the application
cd "$(dirname "$0")"
java -jar target/webapplication-0.0.1-SNAPSHOT.war

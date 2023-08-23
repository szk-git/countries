#!/bin/bash

# Run Gradle tasks
./gradlew clean build

# Identify the JAR file that doesn't contain the word "plain"
# shellcheck disable=SC2010
JAR_PATH=$(ls build/libs/ | grep -v "plain" | grep ".jar$")

# Build the Docker image
docker build --build-arg JAR_FILE=build/libs/"$JAR_PATH" -t countries-image .

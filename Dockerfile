FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./gradlew clean build
RUN JAR_PATH=$(ls build/libs/ | grep -v "plain" | grep ".jar$") && \
    mv build/libs/$JAR_PATH /app/countries.jar
ENTRYPOINT ["java", "-jar", "/app/countries.jar"]

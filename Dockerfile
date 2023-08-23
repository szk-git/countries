FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} countries.jar
ENTRYPOINT ["java", "-jar", "/countries.jar"]

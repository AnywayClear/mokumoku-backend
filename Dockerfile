FROM adoptopenjdk:11-jdk-hotspot AS TEMP_BUILD_IMAGE
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY . ./
RUN chmod +x gradlew
RUN ./gradlew build -x test --stacktrace

FROM adoptopenjdk:11-jdk-hotspot
ENV ARTIFACT_NAME=project-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .
EXPOSE 8080
CMD ["java", "-jar", "project-0.0.1-SNAPSHOT.jar"]
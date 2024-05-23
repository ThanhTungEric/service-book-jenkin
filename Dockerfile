FROM openjdk:19
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/*.jar
COPY ./target/service-book-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:17
EXPOSE 8081
WORKDIR /app
COPY target/service-book-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java", "-jar" , "app.jar" ]

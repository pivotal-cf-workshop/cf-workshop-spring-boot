FROM openjdk:8
VOLUME /tmp
ADD ./target/cf-workshop-spring-boot-0.0.2-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
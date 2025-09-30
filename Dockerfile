FROM openjdk:8-jdk-slim
VOLUME /tmp
Add ilawgestaov3/target/ilawgestaov3-0.0.1-SNAPSHOT.jar ilawgestaov3.jar
EXPOSE 8084
RUN bash -c 'touch /ilawgestaov3.jar'
ENTRYPOINT ["java","-jar","/ilawgestaov3.jar"]

FROM eclipse-temurin:21.0.10_7-jre-noble
LABEL maintainer="togo.huang@foxmail.com"
USER application:application
RUN mkdir -p /opt/config
WORKDIR /opt
ARG TIME_ZONE="Asia/Shanghai"
ENV TZ=$TIME_ZONE
COPY src/main/resources/application.yml  /opt/config/application.yml
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["/bin/sh", "-c","exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /opt/app.jar"]
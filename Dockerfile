FROM openjdk:17-alpine

WORKDIR /opt/service

ADD ./target/epaper-service.jar /opt/service/epaper-service.jar

EXPOSE 8080 8200
CMD ["java", "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8200", "-jar", "epaper-service.jar"]

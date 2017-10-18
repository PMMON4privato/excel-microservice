# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine

# Copy application WAR (with libraries inside)
COPY ./target/service.jar /app.jar

# Specify default command
CMD ["/usr/bin/java", "-jar", "-Xmx6g", "/app.jar"]

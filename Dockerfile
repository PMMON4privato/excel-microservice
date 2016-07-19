FROM java:8
COPY ./target/excel-microservice.jar /usr/src/app/
WORKDIR /usr/src/app
CMD java -jar excel-microservice.jar
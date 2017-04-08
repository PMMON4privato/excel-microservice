FROM java:8
COPY ./target/excel-microservice.jar /usr/src/app/
COPY ./base.xlsx /usr/src/app
WORKDIR /usr/src/app
EXPOSE 9267
CMD java -Xmx4g -jar excel-microservice.jar
FROM openjdk:11
EXPOSE 8080
ADD target/TaxiBack.jar TaxiBack.jar
ENTRYPOINT ["java", "-jar", "TaxiBack.jar"]
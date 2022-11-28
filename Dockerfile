FROM openjdk:11
RUN #mvn clean package
ADD target/TaxiBack.jar TaxiBack.jar
ENTRYPOINT ["java", "-jar", "TaxiBack.jar"]
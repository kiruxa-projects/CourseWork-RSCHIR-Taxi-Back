FROM java:8
ADD target/TaxiBack.jar /opt/TaxiBack/TaxiBack.jar
CMD ["java","-jar","/opt/TaxiBack/TaxiBack.jar"]
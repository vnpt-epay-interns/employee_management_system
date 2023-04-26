#base image: linux alpine os with open jdk 19
FROM openjdk:19-jdk-alpine
#copy jar from local into docker image
#COPY target/Employee_Management_System-0.0.1-SNAPSHOT.jar Employee_Management_System-0.0.1-SNAPSHOT.jar
##command line to run jar
#ENTRYPOINT ["java", "-jar", "/Employee_Management_System-0.0.1-SNAPSHOT.jar"]
WORKDIR /app
COPY target/Employee_Management_System-0.0.1-SNAPSHOT.jar /app/Employee_Management_System-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/Employee_Management_System-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
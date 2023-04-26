#base image: linux alpine os with open jdk 19
FROM openjdk:19-jdk-alpine
#copy jar from local into docker image
COPY target/Employee_Management_System-0.0.1-SNAPSHOT.jar Employee_Management_System-0.0.1-SNAPSHOT.jar
#command line to run jar
ENTRYPOINT ["java", "-jar", "/Employee_Management_System-0.0.1-SNAPSHOT.jar"]
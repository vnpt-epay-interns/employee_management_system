#base image: linux alpie os with open jdk 19
FROM openjdk:19-alpine3.16
#copy jar from local into docker image
COPY target/Employee_Management_System-0.0.1-SNAPSHOT.jar Employee_Management_System-0.0.1-SNAPSHOT.jar
#command line to run the jar
ENTRYPOINT ["java","-jar","/Employee_Management_System-0.0.1-SNAPSHOT.jar"]


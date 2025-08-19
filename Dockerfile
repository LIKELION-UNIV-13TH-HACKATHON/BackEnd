FROM bellsoft/liberica-openjdk-debian:21

ARG JAR_FILE=build/libs/*SNAPSHOT.jar

COPY ${JAR_FILE} project.jar

<<<<<<< HEAD
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "project.jar"]
=======
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=prod-env", "project.jar"]
>>>>>>> Setting/#70

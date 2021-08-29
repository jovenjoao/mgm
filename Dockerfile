# we will use openjdk 8 with alpine as it is a very small linux distro
FROM gradle:6.4.1-jdk11

#copy files
COPY ./ ./

# package our application code
RUN ./gradlew clean build -x test

# set the startup command to execute the jar
CMD ["java", "-jar", "./build/libs/mgm-0.1-all.jar"]
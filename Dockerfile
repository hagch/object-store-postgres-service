FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","--enable-preview","-jar","/app.jar","-Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"]
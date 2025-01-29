#Launch: "docker build -t deal-service -f deal/Dockerfile ."
FROM maven:3.9.1-eclipse-temurin-17 AS builder
WORKDIR /workplace
COPY ../core/target/core-1.0-SNAPSHOT.jar /build/libs/core-1.0-SNAPSHOT.jar

RUN mvn install:install-file \
    -Dfile=/build/libs/core-1.0-SNAPSHOT.jar \
    -DgroupId=ru.neoflex \
    -DartifactId=core \
    -Dversion=1.0-SNAPSHOT \
    -Dpackaging=jar
COPY deal/pom.xml .
COPY deal/src/main/ src/main/
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS layers
WORKDIR /tmp
ARG JAR_FILE=/workplace/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar
WORKDIR /workplace
RUN java -Djarmode=layertools -jar /tmp/app.jar extract

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
RUN adduser -D user
USER user
COPY --from=layers /workplace/dependencies/ ./
COPY --from=layers /workplace/snapshot-dependencies/ ./
COPY --from=layers /workplace/spring-boot-loader/ ./
COPY --from=layers /workplace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8082
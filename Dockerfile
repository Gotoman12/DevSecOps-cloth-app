FROM maven:3.8.3-eclipse-temurin-17 As builder
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin-17-jre
WORKDIR /app
COPY --from=builder /app/target/cloth-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
RUN ["java","-jar","/app/app.jar"]
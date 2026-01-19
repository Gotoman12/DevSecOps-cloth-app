FROM eclipse-temurin:17-jdk As builder
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/cloth-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
RUN ["java","-jar","/app/app.jar"]
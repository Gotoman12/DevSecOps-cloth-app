FROM maven:3.8.3-eclipse-temurin-17 As builder
RUN useradd -m -s /bin/bash appuser
WORKDIR /app
COPY . /app
RUN chown -R appuser:appuser /app
USER appuser

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN mkdir /app/data
COPY --from=builder /app/target/cloth-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java","-jar","/app/app.jar"]
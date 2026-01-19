FROM eclipse-temurin:17-jdk As builder
WORKDIR /app
COPY . /app
RUN mvn clean package
---
FROM eclipse-temurin:17-jdk
COPY --copy=builder  dest
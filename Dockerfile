# 1. Maven bilan build qilish (builder stage)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Runtime uchun kichik image (faqat app.jar)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Jar file ni oldingi build'dan olish
COPY --from=builder /app/target/*.jar app.jar

# Environment variables (optional - siz istagancha)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://157.180.90.169:5432/vessen_db
ENV SPRING_DATASOURCE_USERNAME=vessen_user
ENV SPRING_DATASOURCE_PASSWORD=root

#EXPOSE 8080 (optional, faqat informativ)
EXPOSE 8080

# App-ni ishga tushurish
ENTRYPOINT ["java", "-jar", "app.jar"]

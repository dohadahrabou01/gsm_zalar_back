# Utiliser une image de base avec OpenJDK
FROM maven:3.8.3-openjdk-17 AS build

# Définir le répertoire de travail
WORKDIR /app
COPY ./app/ .

RUN mvn clean package
# Copier le fichier JAR de l'application dans le conteneur

FROM openjdk:17-alphine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/app/gsm_zalar.jar"]


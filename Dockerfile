FROM maven:3.8.3-openjdk-17 AS build

# Définir le répertoire de travail
WORKDIR /gsm_zalar

# Copier les fichiers de l'application
COPY ./gsm_zalar/ .

# Construire l'application
RUN mvn clean package

# Utiliser une image Alpine avec OpenJDK 17 pour exécuter l'application
FROM openjdk:17-alpine

# Définir le répertoire de travail
WORKDIR /gsm_zalar

# Copier le fichier JAR de l'application depuis l'étape de build
COPY --from=build /app/target/*.jar /app/app.jar

# Exposer le port 9000
EXPOSE 9000

# Définir le point d'entrée pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

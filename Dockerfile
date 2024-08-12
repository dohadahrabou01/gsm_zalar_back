FROM maven:3.8.3-openjdk-17 AS build

# Définir le répertoire de travail
WORKDIR /gsm_zalar

# Copier les fichiers de l'application
COPY ./gsm_zalar/ .
COPY ./gsm_zalar/pom.xml ./gsm_zalar/src /gsm_zalar/
# Construire l'application
RUN mvn clean package -DskipTests

# Utiliser une image Alpine avec OpenJDK 17 pour exécuter l'application
FROM openjdk:17-alpine

# Définir le répertoire de travail
WORKDIR /gsm_zalar

# Copier le fichier JAR de l'application depuis l'étape de build
COPY --from=build /gsm_zalar/target/*.jar /gsm_zalar/app.jar

# Exposer le port 9000
EXPOSE 9000

# Définir le point d'entrée pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
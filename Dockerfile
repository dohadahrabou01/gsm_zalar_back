# Utiliser une image de base avec OpenJDK
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR de l'application dans le conteneur
COPY target/gsm_zalar-0.0.1-SNAPSHOT.jar /app/gsm_zalar.jar


ENTRYPOINT ["java", "-jar", "/app/gsm_zalar.jar"]


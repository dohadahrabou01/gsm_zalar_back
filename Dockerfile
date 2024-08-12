FROM maven:3.8.3-openjdk-17 AS build

# Définir le répertoire de travail
WORKDIR /gsm_zalar_back

# Copier les fichiers de l'application
COPY ./gsm_zalar_back/ .
COPY ./gsm_zalar_back/pom.xml ./gsm_zalar_back/src /gsm_zalar_back/
# Construire l'application
RUN mvn clean package -DskipTests

# Utiliser une image Alpine avec OpenJDK 17 pour exécuter l'application
FROM openjdk:17-alpine

# Définir le répertoire de travail
WORKDIR /gsm_zalar_back

# Copier le fichier JAR de l'application depuis l'étape de build
COPY --from=build /gsm_zalar_back/target/*.jar /gsm_zalar_back/app.jar

# Exposer le port 9000
EXPOSE 9000

# Définir le point d'entrée pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
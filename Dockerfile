# Utiliser une image de base avec Maven et OpenJDK
FROM maven:3.8.6-openjdk-17 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et les sources dans le conteneur
COPY gsm_zalar_back/pom.xml .
COPY gsm_zalar_back/src /app/src

# Vérifier que le pom.xml est bien présent
RUN ls -l

# Construire le projet Maven
RUN mvn clean package -DskipTests

# Utiliser une image plus légère pour exécuter l'application
FROM openjdk:17-alpine

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR construit depuis l'image de build
COPY --from=build /app/target/mon-app.jar /app/mon-app.jar

# Exposer le port
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/app/mon-app.jar"]

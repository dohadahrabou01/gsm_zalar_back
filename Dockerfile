# Étape 1 : Construire l'application avec Maven
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copier les fichiers Maven et le code source
COPY pom.xml .
COPY src ./src

# Compiler le projet
RUN mvn clean package -DskipTests

# Étape 2 : Exécuter l'application avec une image JDK
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier le jar généré dans l'image finale
COPY --from=build /app/target/gsm_zalar-0.0.1-SNAPSHOT.jar /app/gsm_zalar.jar

# Exposer le port
EXPOSE 9000

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "gsm_zalar.jar"]

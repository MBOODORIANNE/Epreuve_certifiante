# ── Stage 1: Build ────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copier pom.xml et télécharger les dépendances (couche cachée)
COPY pom.xml .
RUN apk add --no-cache maven && \
    mvn dependency:go-offline -B

# Copier les sources et builder
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Runtime ──────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

# Métadonnées
LABEL maintainer="MBOO"
LABEL description="EcoMove - Plateforme B2B de Covoiturage Domicile-Travail"
LABEL version="1.0.0"

WORKDIR /app

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S ecomove && adduser -S ecomove -G ecomove

# Copier le JAR depuis le stage builder
COPY --from=builder /app/target/*.jar app.jar

# Créer le dossier de logs
RUN mkdir -p /app/logs && chown -R ecomove:ecomove /app

USER ecomove

# Port exposé
EXPOSE 8080

# Variables d'environnement par défaut (surchargeables)
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE="prod"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

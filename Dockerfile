# --- ESTÁGIO 1: Build da Aplicação (Atualizado para Java 21) ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache das dependências do Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Compila o JAR limpando os testes para ser mais rápido
COPY src ./src
RUN mvn clean package -DskipTests

# --- ESTÁGIO 2: Imagem Final (Atualizado para Java 21) ---
FROM eclipse-temurin:21-jre-alpine
LABEL authors="vinicius-ubuntu"
WORKDIR /app

# Segurança: Cria usuário de sistema sem privilégios root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia apenas o JAR final gerado no estágio anterior
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
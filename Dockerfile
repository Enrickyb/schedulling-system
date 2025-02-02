# Usa a imagem oficial do Java 21
FROM openjdk:21-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR gerado pelo Maven
COPY target/scheduling-0.0.1-SNAPSHOT.jar  scheduling.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "scheduling.jar"]

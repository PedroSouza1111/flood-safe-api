# Etapa 1: Build da Aplicação
# Use uma imagem com o JDK e o Maven para compilar o projeto
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Criação da Imagem Final
# Use uma imagem enxuta, apenas com o Java Runtime
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho dentro do container 
WORKDIR /app

# Cria um usuário não-root para rodar a aplicação 
RUN useradd -ms /bin/bash appuser
USER appuser

# Copia o .jar da etapa de build para a imagem final
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que a aplicação usa 
EXPOSE 8080

# Define uma variável de ambiente (exemplo) 
ENV APP_PROFILE=production

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
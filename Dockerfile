# --- Estágio 1: Build Otimizado ---
# Usamos uma imagem padrão com JDK e Maven para compilar o projeto.
FROM maven:3.8.5-openjdk-17 AS build

# Define o diretório de trabalho dentro do contêiner de build.
WORKDIR /app

# 1. Copia APENAS o arquivo pom.xml.
# A ordem dos comandos COPY é crucial para o cache.
COPY pom.xml .

# 2. Baixa todas as dependências listadas no pom.xml.
# Esta camada de cache só será invalidada se o arquivo pom.xml for alterado.
# Isso economiza muito tempo em builds futuros.
RUN mvn dependency:go-offline -B

# 3. Agora, copia o resto do código-fonte da sua aplicação.
# Se você alterar apenas o código-fonte, o Docker reutilizará o cache
# das dependências já baixadas.
COPY src ./src

# 4. Compila a aplicação e a empacota em um arquivo .jar.
# O -B (batch mode) evita logs de download extensos.
# O -DskipTests pula a execução de testes, tornando o build mais rápido.
RUN mvn clean package -B -DskipTests

# --- Estágio 2: Imagem Final de Produção ---
# Usamos uma imagem leve, que contém apenas o necessário para executar Java (JRE).
# Isso torna a imagem final menor e mais segura.
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Cria um usuário não-root (appuser) para rodar a aplicação.
# É uma boa prática de segurança para evitar rodar como administrador (root).
RUN useradd -ms /bin/bash appuser
USER appuser

# Copia o artefato .jar que foi gerado no estágio 'build'.
# Renomeamos para 'app.jar' para simplificar o comando de execução.
# IMPORTANTE: Verifique se o nome do seu JAR corresponde ao padrão abaixo.
# Se o nome for diferente, ajuste o caminho aqui.
COPY --from=build /app/target/*.jar app.jar


# Expõe a porta em que a aplicação Spring Boot roda (padrão 8080).
# A Railway detecta isso e direciona o tráfego para esta porta.
EXPOSE 8080

# Comando final para executar a aplicação quando o contêiner iniciar.
# Incluímos os limites de memória que você estava usando.
# -Xmx256m: Limita a memória RAM máxima para 256MB.
# -Xss512k: Define o tamanho da pilha (stack) por thread.
ENTRYPOINT ["java", "-Xmx256m", "-Xss512k", "-jar", "app.jar"]

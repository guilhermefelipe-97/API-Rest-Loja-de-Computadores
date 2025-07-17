# Etapa de build: utilizando a imagem do OpenJDK 24 para compilar o projeto com Maven Wrapper
FROM openjdk:24-jdk AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo de configuração do Maven (pom.xml) para o diretório de trabalho
COPY pom.xml .

# Copia o diretório com o código-fonte da aplicação
COPY src src

# Copia o Maven Wrapper (scripts e configurações) para garantir que o Maven funcione corretamente sem precisar estar instalado no host
COPY mvnw .
COPY .mvn .mvn

# Dá permissão de execução ao script mvnw
RUN chmod +x ./mvnw

# Executa o build da aplicação, pulando os testes
RUN ./mvnw clean package -DskipTests

# Etapa final: cria a imagem definitiva com a aplicação empacotada, agora com OpenJDK 24 Slim
FROM openjdk:24-jdk-slim

# Cria um volume temporário para arquivos que possam ser escritos pela aplicação (ex: arquivos temporários)
VOLUME /tmp

# Copia o arquivo .jar gerado na etapa de build para o contêiner final
COPY --from=build /app/target/*.jar app.jar

# Define o ponto de entrada para executar a aplicação Java
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Expõe a porta 8080 para acesso externo à aplicação
EXPOSE 8080
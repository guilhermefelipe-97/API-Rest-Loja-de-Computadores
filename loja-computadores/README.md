# 🖥️ Loja de Computadores - API REST

Uma API REST completa para gerenciamento de uma loja de computadores, desenvolvida em Spring Boot com autenticação JWT, relacionamentos complexos e funcionalidades avançadas.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Executando o Projeto](#executando-o-projeto)
- [Documentação da API](#documentação-da-api)
- [Autenticação JWT](#autenticação-jwt)
- [Endpoints da API](#endpoints-da-api)
- [Relacionamentos](#relacionamentos)
- [Funcionalidades Especiais](#funcionalidades-especiais)
- Testes](#testes)
- [Deploy com Docker](#deploy-com-docker)
- [Contribuição](#contribuição)
- [Licença](#licença)

## 🎯 Sobre o Projeto

Este projeto implementa uma API REST completa para uma loja de computadores, oferecendo:

- **Autenticação JWT** com segurança stateless
- **CRUD completo** para todas as entidades
- **Relacionamentos complexos** (1-11N)
- **Soft Delete** para preservação de dados
- **Paginação e ordenação** em todas as listagens
- **Logging detalhado** para auditoria
- **Validação de dados** com Bean Validation
- **Documentação automática** com OpenAPI/Swagger
- **Tratamento de erros** centralizado

## ✨ Funcionalidades

### 🔐 Autenticação e Segurança
- Login com JWT (JSON Web Token)
- Controle de acesso baseado em roles
- Senhas criptografadas com BCrypt
- Tokens com expiração configurável

### 👥 Gestão de Usuários
- **Admin**: Acesso total ao sistema
- **Clientes**: Gestão de perfil e pedidos
- Soft delete para preservação de dados

### 🛍️ Gestão de Produtos
- Cadastro de produtos com categorias
- Relacionamento N-N entre Produto e Categoria
- Controle de estoque
- Preços com precisão decimal

### 📦 Gestão de Pedidos
- Criação de pedidos com múltiplos produtos
- Cálculo automático de valores totais
- Controle de status (PENDENTE, APROVADO, ENTREGUE, CANCELADO)
- Histórico de compras automático

### 📊 Histórico de Compras
- Relacionamento1-1 com Cliente
- Cálculo automático do valor total gasto
- Contagem de pedidos realizados
- Datas da primeira e última compra
- Categorias e marcas preferidas

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** com JWT
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL** (banco principal)
- **H2** (banco de testes)

### Ferramentas e Bibliotecas
- **Maven** (gerenciador de dependências)
- **Lombok** (redução de boilerplate)
- **MapStruct** (mapeamento de objetos)
- **Bean Validation** (validação de dados)
- **OpenAPI3* (documentação da API)
- **SLF4J + Logback** (logging)

### DevOps
- **Docker** e **Docker Compose**
- **Git** (controle de versão)

## 📁 Estrutura do Projeto

```
src/main/java/ufrn/br/lojacomputadores/
├── controller/           # Controllers REST
│   ├── AdminController.java
│   ├── AuthController.java
│   ├── ClienteController.java
│   ├── PedidoController.java
│   └── ProdutoController.java
├── domain/              # Entidades JPA
│   ├── Admin.java
│   ├── Cliente.java
│   ├── HistoricoCompras.java
│   ├── Pedido.java
│   └── Produto.java
├── dto/                 # Data Transfer Objects
│   ├── AdminRequestDto.java
│   ├── AdminResponseDto.java
│   ├── ClienteRequestDto.java
│   ├── ClienteResponseDto.java
│   ├── PedidoRequestDto.java
│   ├── PedidoResponseDto.java
│   ├── ProdutoRequestDto.java
│   └── ProdutoResponseDto.java
├── mapper/              # Mappers MapStruct
│   ├── AdminMapper.java
│   ├── ClienteMapper.java
│   ├── PedidoMapper.java
│   └── ProdutoMapper.java
├── repository/          # Repositórios JPA
│   ├── AdminRepository.java
│   ├── ClienteRepository.java
│   ├── HistoricoComprasRepository.java
│   ├── PedidoRepository.java
│   └── ProdutoRepository.java
├── service/             # Camada de serviço
│   ├── AdminService.java
│   ├── ClienteService.java
│   ├── HistoricoComprasService.java
│   ├── PedidoService.java
│   └── ProdutoService.java
├── security/            # Configurações de segurança
│   ├── SecurityConfig.java
│   ├── TokenService.java
│   └── RsaKeyProperties.java
└── core/                # Configurações e utilitários
    ├── base/
    ├── documentation/
    ├── errorhandling/
    └── exception/
```

## ⚙️ Pré-requisitos

- **Java 17* ou superior
- **Maven 3.6+**
- **PostgreSQL 12* (ou Docker)
- **Git**

## 🚀 Instalação e Configuração

### 1lone o repositório
```bash
git clone https://github.com/seu-usuario/loja-computadores.git
cd loja-computadores
```

### 2 Configure o banco de dados
Edite o arquivo `src/main/resources/application.properties`:

```properties
# Configuração do PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/loja_computadores
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuração JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuração JWT
jwt.secret=sua_chave_secreta_jwt_aqui
jwt.expiration=86400000``

### 3. Execute o projeto
```bash
# Compilar o projeto
mvn clean compile

# Executar
mvn spring-boot:run
```

## 🗄️ Configuração do Banco de Dados

### Usando PostgreSQL local
```sql
-- Criar banco de dados
CREATE DATABASE loja_computadores;

-- Criar usuário (opcional)
CREATE USER loja_user WITH PASSWORDloja_password';
GRANT ALL PRIVILEGES ON DATABASE loja_computadores TO loja_user;
```

### Usando Docker
```bash
# Executar PostgreSQL com Docker
docker run --name postgres-loja \
  -e POSTGRES_DB=loja_computadores \
  -e POSTGRES_USER=loja_user \
  -e POSTGRES_PASSWORD=loja_password \
  -p 543232\
  -d postgres:13
```

## ▶️ Executando o Projeto

### Opção 1 Maven
```bash
mvn spring-boot:run
```

### Opção 2
```bash
mvn clean package
java -jar target/loja-computadores-00SNAPSHOT.jar
```

### Opção 3Docker Compose
```bash
docker-compose up -d
```

## 📚 Documentação da API

A documentação da API está disponível através do Swagger UI:

- **URL**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:80803-docs

### Exemplo de uso do Swagger:1e http://localhost:8080/swagger-ui.html
2. Faça login usando o endpoint `/auth/login`
3 Copie o token JWT retornado
4. Clique emAuthorize" no Swagger e cole o token
5ora você pode testar todos os endpoints

## 🔐 Autenticação JWT

### Login
```bash
POST /auth/login
Content-Type: application/json
[object Object]username": "admin,password": admin123
```

### Resposta
```json[object Object]
  token:eyJhbGciOiJIUzI1NiIsInR5cCI6XVCJ9
 type": "Bearer"
}
```

### Usar o token
```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6kpXVCJ9...
```

## 🌐 Endpoints da API

### Autenticação
- `POST /auth/login` - Login de usuário

### Administradores
- `GET /admin` - Listar admins (paginação)
- `GET /admin/{id}` - Buscar admin por ID
- `POST /admin` - Criar admin
- `PUT /admin/{id}` - Atualizar admin
- `DELETE /admin/{id}` - Deletar admin

### Clientes
- `GET /clientes` - Listar clientes (paginação)
- `GET /clientes/{id}` - Buscar cliente por ID
- `POST /clientes` - Criar cliente
- `PUT /clientes/{id}` - Atualizar cliente
- `DELETE /clientes/{id}` - Deletar cliente

### Produtos
- `GET /produtos` - Listar produtos (paginação)
- `GET /produtos/{id}` - Buscar produto por ID
- `POST /produtos` - Criar produto
- `PUT /produtos/{id}` - Atualizar produto
- `DELETE /produtos/{id}` - Deletar produto

### Pedidos
- `GET /pedidos` - Listar pedidos (paginação)
- `GET /pedidos/{id}` - Buscar pedido por ID
- `POST /pedidos` - Criar pedido
- `PUT /pedidos/{id}` - Atualizar pedido
- `DELETE /pedidos/{id}` - Cancelar pedido

### Parâmetros de Paginação
Todos os endpoints de listagem suportam paginação:

```
GET /clientes?page=0&size=10&sort=nome,asc
```

- `page`: Número da página (0based)
- `size`: Tamanho da página
- `sort`: Campo para ordenação (campo,direção)

## 🔗 Relacionamentos

### 1-1liente ↔ HistóricoCompras
- Cada cliente tem um histórico de compras único
- Histórico é criado automaticamente com o primeiro pedido
- Contém valor total gasto, total de pedidos, datas, etc.

### 1 Cliente → Pedidos
- Um cliente pode ter múltiplos pedidos
- Pedidos são vinculados ao cliente via `cliente_id`

### N-N: Produto ↔ Categoria
- Produtos podem pertencer a múltiplas categorias
- Categorias podem ter múltiplos produtos
- Implementado via tabela intermediária

### N-N: Pedido ↔ Produto (via ItemPedido)
- Pedidos podem conter múltiplos produtos
- Produtos podem estar em múltiplos pedidos
- Quantidade e preço unitário por item

## ⭐ Funcionalidades Especiais

### Histórico de Compras Automático
- Criado automaticamente com o primeiro pedido
- Valor total gasto calculado em tempo real
- Atualização automática após criação/cancelamento de pedidos

### Soft Delete
- Todas as entidades usam soft delete
- Dados não são perdidos, apenas marcados como deletados
- Filtro automático para não exibir registros deletados

### Validação de Dados
- Bean Validation em todos os DTOs
- Mensagens de erro personalizadas
- Validação de CPF, email, etc.

### Logging Detalhado
- Logs de todas as operações CRUD
- Informações de auditoria
- Configuração via SLF4J + Logback

## 🧪 Testes

### Executar testes unitários
```bash
mvn test
```

### Executar testes de integração
```bash
mvn verify
```

### Cobertura de testes
```bash
mvn jacoco:report
```

## 🐳 Deploy com Docker

### Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/loja-computadores-01APSHOT.jar app.jar
ENTRYPOINT ["java,-jar","/app.jar]
```

### Docker Compose
```yaml
version:3.8
services:
  app:
    build: .
    ports:
      - "8080:8080
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/loja_computadores
      - SPRING_DATASOURCE_USERNAME=loja_user
      - SPRING_DATASOURCE_PASSWORD=loja_password
    depends_on:
      - db
  
  db:
    image: postgres:13
    environment:
      - POSTGRES_DB=loja_computadores
      - POSTGRES_USER=loja_user
      - POSTGRES_PASSWORD=loja_password
    ports:
      - "5432:5432    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### Executar com Docker
```bash
# Build da imagem
docker build -t loja-computadores .

# Executar com Docker Compose
docker-compose up -d
```

## 📝 Exemplos de Uso

### Criar um cliente
```bash
curl -X POST http://localhost:8080clientes" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d {nome":João Silva",
 email:joao@email.com,   cpf:1234567891,    telefone: (1199999999
  }'
```

### Criar um produto
```bash
curl -X POST http://localhost:8080produtos" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '[object Object]nome": "Notebook Dell Inspiron",
   descricao": "Notebook com processador Intel i5",
    preco": 2999.99    quantidadeEstoque": 10
    categoriaIds": [1, 2]
  }'
```

### Criar um pedido
```bash
curl -X POST http://localhost:8080/pedidos" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d[object Object]
  clienteId": 1,
    status": "PENDENTE"
  }'
```

## 🤝 Contribuição

1um fork do projeto
2ie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3mmit suas mudanças (`git commit -mAdd some AmazingFeature`)
4.Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Padrões de Código
- Use Java 17features
- Siga as convenções do Spring Boot
- Mantenha a cobertura de testes
- Documente APIs novas

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Seu Nome**
- GitHub: [@seu-usuario](https://github.com/seu-usuario)
- LinkedIn: [Seu LinkedIn](https://linkedin.com/in/seu-perfil)

## 🙏 Agradecimentos

- Spring Boot Team
- Comunidade Java
- Professores e colegas da UFRN

---

⭐ Se este projeto te ajudou, considere dar uma estrela no repositório! 
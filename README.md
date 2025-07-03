# 💰 Sistema Financeiro Pessoal

Este é um projeto pessoal de backend desenvolvido com **Java + Spring Boot**, utilizando **autenticação JWT** e persistência com **PostgreSQL**, com o objetivo de servir como base para um sistema de controle financeiro pessoal.

---

## 🚀 Tecnologias utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Security
- Spring Data JPA
- PostgreSQL
- Lombok
- JWT (Java JWT - Auth0)
- Maven

---

## 🧩 Como executar localmente

### ⚙️ Pré-requisitos

- Java 17+
- PostgreSQL
- Maven

### 📦 Clone o repositório

```bash
git clone https://github.com/seu-usuario/sistemafinanceiro.git
cd sistemafinanceiro
```

### 🛠️ Configure o banco de dados

Crie um banco de dados:

```sql
CREATE DATABASE sistemafinanceiro;
```

### ✏️ Edite o `application.properties`

No arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sistemafinanceiro
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=suaChaveSecretaMinimo32Caracteres
jwt.expiration=3600000
jwt.refreshExpiration=86400000

# Chave de acesso para registro
app.access-key=MINHA_CHAVE_SEGURA
```

### ▶️ Execute a aplicação

```bash
./mvnw spring-boot:run
```

> A API estará disponível em: `http://localhost:8080`

---

## 📡 Endpoints disponíveis

### 🔐 Registro de Usuário

- **POST** `/auth/register`

#### Requisição

```json
{
  "username": "claudemirmendes",
  "password": "minhaSenha123",
  "accessKey": "MINHA_CHAVE_SEGURA"
}
```

#### Resposta

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp...",
  "message": "Usuário registrado com sucesso!"
}
```

---

### 🔑 Login

- **POST** `/auth/login`

#### Requisição

```json
{
  "username": "claudemirmendes",
  "password": "minhaSenha123"
}
```

#### Resposta

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6Ikp...",
  "message": "Login realizado com sucesso!"
}
```

---

## 📁 Estrutura do Projeto

```
src/
├── controller/
│   └── AuthController.java
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   └── JwtResponse.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── service/
│   └── JwtService.java
└── SistemafinanceiroApplication.java
```

---

## ✅ Funcionalidades

- Registro de usuários com chave de acesso segura
- Autenticação com JWT (Token e Refresh Token)
- Senhas criptografadas com `PasswordEncoder`
- Validações básicas e tratamento de exceções

---

## 🧪 Rodar os testes

```bash
./mvnw test
```

---

## 📌 Observações

- O campo `accessKey` protege o endpoint de registro contra acessos indevidos.
- JWT e Refresh Token são retornados após registro e login.
- Tokens possuem tempo de expiração configurável via `application.properties`.

---

## 👨‍💻 Autor

**Claudemir Mendes**  
Projeto pessoal com fins de aprendizado e demonstração de habilidades com Java e Spring Boot.

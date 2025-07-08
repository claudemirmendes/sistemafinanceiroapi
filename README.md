# 💰 Sistema Financeiro - API REST com Spring Boot

API desenvolvida em Java com Spring Boot para controle de transações financeiras (receitas e despesas), com autenticação baseada em JWT.

---

## 🔧 Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

---

## ⚙️ Configuração do projeto

No arquivo `application.properties`, configure:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financeiro
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

jwt.secret=sua_chave_secreta
jwt.expiration=86400000
jwt.refreshExpiration=604800000
```

---

## ✅ Funcionalidades

### 🔐 Autenticação

| Método | Rota           | Descrição             |
|--------|----------------|-----------------------|
| POST   | `/auth/register` | Cadastra novo usuário |
| POST   | `/auth/login`    | Retorna token JWT     |

---

### 💸 Transações

| Método | Rota          | Descrição                     |
|--------|---------------|-------------------------------|
| POST   | `/transacoes` | Cadastra nova transação       |

> ⚠️ Requer autenticação com JWT. Enviar o token no header:  
> `Authorization: Bearer <token>`

---

## 📜 Regras de Negócio

### 📥 Receita

- **Campos obrigatórios:**
    - `dataPrevistaRecebimento`
    - `confirmada`
- **Campos opcionais:**
    - `dataRecebida`

### 📤 Despesa

- **Campos obrigatórios:**
    - `dataVencimento`
    - `paga`
- **Campos opcionais:**
    - `dataPagamento`

> A validação é condicional com base no tipo da transação (`RECEITA` ou `DESPESA`).

---

## 🔐 Segurança

- Autenticação com JWT
- Endpoints `/auth/**` são públicos
- Demais rotas exigem autenticação
- `JwtAuthenticationFilter` intercepta requisições, valida o token e define o contexto de autenticação

---

## 🧪 Teste Rápido com cURL

```bash
# Cadastro
curl -X POST http://localhost:8080/auth/register   -H "Content-Type: application/json"   -d '{"username": "claudemirmendes", "password": "minhaSenha123"}'

# Login
curl -X POST http://localhost:8080/auth/login   -H "Content-Type: application/json"   -d '{"username": "claudemirmendes", "password": "minhaSenha123"}'
```

---

## 🗂️ Estrutura do Projeto

- `model` – Entidades JPA (`User`, `Transacao`)
- `dto` – Requisições de entrada (`LoginRequest`, `RegisterRequest`, `TransacaoRequest`)
- `repository` – Acesso ao banco (`UserRepository`, `TransacaoRepository`)
- `service` – Regras de negócio
- `controller` – Endpoints REST
- `security` – Filtros e configuração JWT

---

## 📌 Observações

- O campo `usuario` é automaticamente vinculado à transação com base no token
- O valor da transação é salvo como `BigDecimal` no backend e `numeric` no banco
- Senhas são criptografadas com `BCrypt`
- JWT é validado a cada requisição via filtro personalizado
- A estrutura permite expansão para novas funcionalidades (ex: listagem, edição, gráficos etc.)

---

## 🚀 Futuras Implementações

- Listagem de transações por usuário autenticado
- Atualização e remoção de transações
- Resumo mensal (dashboard)
- Suporte a múltiplas contas

---

## 👨‍💻 Autor

Claudemir Mendes  
Desenvolvedor Java em transição a partir de Ruby on Rails  
LinkedIn: [linkedin.com/in/claudemirmendes](https://linkedin.com/in/claudemirmendes)

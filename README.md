# Books API - CRUD com Spring Boot e React

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen)
![H2](https://img.shields.io/badge/Database-H2-blue)
![HTTPS](https://img.shields.io/badge/Protocol-HTTPS-red)

Projeto fullstack para portfólio: **API REST de CRUD de livros** com Java + Spring Boot + H2 rodando em HTTPS, consumida por frontend React com Vite + TypeScript + TailwindCSS.

---

## 📋 Descrição

Sistema completo para gerenciamento de livros com:
- Backend RESTful em Java 17 + Spring Boot 3.x
- Banco de dados H2 em memória (modo PostgreSQL)
- Comunicação via HTTPS com certificado self-signed
- Documentação automática com Swagger UI
- Frontend React (em desenvolvimento)

---

## 🛠️ Tecnologias - Backend

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **Java** | 17 | Linguagem principal |
| **Spring Boot** | 3.2.4 | Framework web |
| **Spring Data JPA** | - | Persistência de dados |
| **Spring Validation** | - | Validação de dados |
| **H2 Database** | - | Banco em memória |
| **Lombok** | - | Redução de boilerplate |
| **springdoc-openapi** | 2.3.0 | Documentação Swagger/OpenAPI 3.0 |
| **Maven** | - | Gerenciamento de dependências |

---

## 📁 Estrutura do Projeto

```
books-api/
├── src/
│   ├── main/
│   │   ├── java/books/api/
│   │   │   ├── BooksApiApplication.java      # Classe principal
│   │   │   ├── bootstrap/
│   │   │   │   └── DataLoader.java           # Carga inicial de dados
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java           # Configuração CORS
│   │   │   │   └── SwaggerConfig.java        # Configuração Swagger
│   │   │   ├── domain/
│   │   │   │   └── Book.java                 # Entidade JPA
│   │   │   ├── dto/
│   │   │   │   ├── BookRequest.java          # DTO de requisição
│   │   │   │   └── BookResponse.java         # DTO de resposta
│   │   │   ├── mapper/
│   │   │   │   └── BookMapper.java           # Conversão Entity <-> DTO
│   │   │   ├── repository/
│   │   │   │   └── BookRepository.java       # Acesso a dados
│   │   │   ├── service/
│   │   │   │   └── BookService.java          # Lógica de negócio
│   │   │   └── web/
│   │   │       └── BookController.java       # Endpoints REST
│   │   └── resources/
│   │       ├── application.properties        # Configurações
│   │       └── keystore.p12                  # Certificado SSL
│   └── test/
│       ├── java/books/api/
│       │   ├── service/BookServiceTest.java
│       │   └── web/BookControllerTest.java
│       └── resources/
│           └── application-test.properties
├── pom.xml
└── README.md
```

---

## 🔒 Configuração HTTPS

O projeto usa um certificado SSL self-signed. Se ainda não existe, gere com:

```bash
keytool -genkeypair -alias booksapi -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore src/main/resources/keystore.p12 \
  -validity 3650 -storepass changeit \
  -dname "CN=localhost, OU=Dev, O=BooksAPI, L=City, ST=State, C=BR"
```

**Configuração atual** (`application.properties`):
- **Porta**: 8443
- **Keystore**: `classpath:keystore.p12`
- **Password**: `changeit`
- **Alias**: `booksapi`

---

## 🚀 Como Executar o Backend

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Executar

```bash
# Via Maven Wrapper (recomendado)
./mvnw spring-boot:run

# Ou via Maven instalado
mvn spring-boot:run
```

O backend estará disponível em: **https://localhost:8443**

> ⚠️ **Primeiro acesso**: Seu navegador mostrará aviso de segurança. Aceite o certificado para prosseguir.

---

## 📚 API Endpoints

**Base URL**: `https://localhost:8443/api/v1/books`

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| `GET` | `/api/v1/books` | Lista todos os livros (paginado) | 200 |
| `GET` | `/api/v1/books?q=clean` | Busca por título (case-insensitive) | 200 |
| `GET` | `/api/v1/books/{id}` | Busca livro por ID | 200 / 404 |
| `POST` | `/api/v1/books` | Cria novo livro | 201 |
| `PUT` | `/api/v1/books/{id}` | Atualiza livro existente | 200 / 404 |
| `DELETE` | `/api/v1/books/{id}` | Remove livro | 204 / 404 |

### Modelo de Dados (Book)

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "price": 120.00,
  "publishedAt": "2008-08-01",
  "status": "AVAILABLE"
}
```

**Status disponíveis**: `AVAILABLE`, `SOLD_OUT`

### Exemplo de Requisição (POST)

```bash
curl -k -X POST https://localhost:8443/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "price": 95.00,
    "publishedAt": "2018-01-06",
    "status": "AVAILABLE"
  }'
```

### Paginação

Query params disponíveis:
- `page`: Número da página (padrão: 0)
- `size`: Itens por página (padrão: 10)
- `sort`: Campo de ordenação (padrão: `id,desc`)

Exemplo: `GET /api/v1/books?page=0&size=5&sort=title,asc`

---

## 🧪 Swagger UI

Acesse a documentação interativa da API em:

**https://localhost:8443/swagger-ui/index.html**

ou simplesmente:

**https://localhost:8443/swagger**

![Swagger UI](https://via.placeholder.com/800x400?text=Swagger+UI+Screenshot)

A UI permite:
- Visualizar todos os endpoints
- Testar requisições diretamente no navegador
- Ver schemas de request/response
- Verificar validações

---

## 🗄️ H2 Console

Console web para visualizar o banco H2 em memória:

**https://localhost:8443/h2**

**Credenciais de acesso**:
- **JDBC URL**: `jdbc:h2:mem:booksdb`
- **Username**: `sa`
- **Password**: *(deixar vazio)*

---

## 🔧 Configurações (application.properties)

```properties
# Servidor HTTPS
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=booksapi

# H2 Database
spring.datasource.url=jdbc:h2:mem:booksdb;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2

# Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger

# CORS (permitir frontend em localhost:5173)
app.cors.allowed-origins=http://localhost:5173
```

---

## 🎨 Frontend (Em Desenvolvimento)

### Tecnologias Planejadas
- React 18
- Vite
- TypeScript
- TailwindCSS
- Axios
- Radix UI / shadcn/ui

### Como executar (quando disponível)
```bash
cd books-frontend
npm install
npm run dev
# Acesse: http://localhost:5173
```

---

## 📊 Dados Iniciais

O projeto carrega automaticamente 2 livros ao iniciar (`DataLoader.java`):

1. **Clean Code** - Robert C. Martin (R$ 120,00)
2. **Refactoring** - Martin Fowler (R$ 150,00)

---

## ✅ Checklist de Funcionalidades

- [x] CRUD completo de livros
- [x] Validação de dados com Bean Validation
- [x] Paginação e busca por título
- [x] HTTPS com certificado self-signed
- [x] Swagger UI integrado
- [x] H2 Console habilitado
- [x] CORS configurado para frontend
- [x] DTOs para desacoplar entidades
- [x] Mapper manual (sem MapStruct)
- [x] Dados iniciais (bootstrap)
- [ ] Testes unitários implementados
- [ ] Testes de integração
- [ ] Frontend React
- [ ] Deploy (Heroku/Railway/Render)

---

## 🐛 Inconsistências Identificadas e Resolvidas

Durante a análise do projeto, foram identificadas e corrigidas:

1. ✅ **Imports duplicados** em `CorsConfig.java` e `SwaggerConfig.java` - **RESOLVIDO**
2. ⚠️ **Versão inconsistente**:
   - `pom.xml` → versão `0.2.0`
   - `SwaggerConfig.java` → estava como `1.0.0` (deve ser corrigido)
3. ⚠️ **@EnableScheduling** em `BooksApiApplication.java` sem uso de scheduling
4. ⚠️ **Testes vazios**: `BookServiceTest` e `BookControllerTest` sem implementação
5. ✅ **README básico** - **ATUALIZADO com documentação completa**

---

## 🔜 Próximos Passos

1. Implementar testes unitários e de integração
2. Adicionar tratamento global de exceções (ControllerAdvice)
3. Implementar frontend React completo
4. Adicionar autenticação (Spring Security + JWT)
5. Configurar CI/CD
6. Deploy em cloud (Heroku/Railway/Render)

---

## 📝 Notas Importantes

- O banco H2 é **em memória** - dados são perdidos ao reiniciar
- Certificado SSL é **self-signed** - apenas para desenvolvimento
- **Primeira requisição do frontend**: pode dar erro SSL. Acesse `https://localhost:8443` no navegador e aceite o certificado antes
- CORS está configurado apenas para `http://localhost:5173`

---

## 📞 Contato

Projeto desenvolvido para fins de portfólio e aprendizado.

---

## 📄 Licença

Este projeto é livre para uso educacional e portfólio.

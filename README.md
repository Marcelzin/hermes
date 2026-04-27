# Hermes PDV

Sistema de Ponto de Venda (PDV) desenvolvido com Spring Boot e SQL Server.

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven
- SQL Server
- Node.js e npm (para dependências frontend)

## 🚀 Configuração do Ambiente

### Banco de Dados

1. Crie um banco de dados no SQL Server
2. Atualize as configurações em `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=seu_banco;encrypt=true;trustServerCertificate=true
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Backend

```bash
# Clone o repositório
git clone https://github.com/Marcelzin/hermes.git

# Entre no diretório
cd hermes

# Instale as dependências
mvn clean install

# Execute o projeto
mvn spring-boot:run
```

## 🛠️ Tecnologias Utilizadas

- **Backend:**
  - Spring Boot
  - Spring Security
  - Spring Data JPA
  - SQL Server
  
- **Frontend:**
  - Thymeleaf
  - Bootstrap
  - jQuery
  - DataTables

## 📦 Estrutura do Projeto

```
hermes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tcc/pdv/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## 🔧 Funcionalidades

- [x] Autenticação de usuários
- [x] Gerenciamento de produtos
- [x] Controle de vendas
- [x] Gestão de equipe
- [ ] Relatórios
- [ ] Dashboard

## 👥 Autores

* **Marcel** - *Desenvolvimento* - [Marcelzin](https://github.com/Marcelzin)

## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE.md](LICENSE.md) para detalhes.

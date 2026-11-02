# Sistema de Aluguel de Carros

## Visão geral
Este projeto foi desenvolvido como protótipo do laboratório de Desenvolvimento e Integração de Aplicações Web, com base na especificação do sistema de aluguel de carros.

A aplicação implementa:
- autenticação por login com usuário e senha;
- CRUD de clientes;
- gestão de veículos;
- cadastro e visualização de pedidos de aluguel;
- arquitetura com backend Spring Boot e frontend React + Vite.

## Tecnologias
- Java 25
- Spring Boot 4.0.x
- Spring Security
- Spring Data JPA
- PostgreSQL (Supabase)
- React + Vite

## Execução local
Configure primeiro as variáveis de ambiente de conexão com o Supabase:

```powershell
$env:SUPABASE_DB_URL="jdbc:postgresql://<host>:5432/postgres"
$env:SUPABASE_DB_USERNAME="postgres"
$env:SUPABASE_DB_PASSWORD="<senha>"
$env:BOOTSTRAP_ADMIN_USERNAME="admin"
$env:BOOTSTRAP_ADMIN_PASSWORD="<uma-senha-forte>"
```

As variáveis `BOOTSTRAP_ADMIN_USERNAME` e `BOOTSTRAP_ADMIN_PASSWORD` criam um usuário
administrador inicial se ele ainda não existir. Defina ambas ou deixe ambas vazias.
Não envie senhas para o GitHub.

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
C:/Program Files/nodejs/npm.cmd install
C:/Program Files/nodejs/npm.cmd run dev
```

A interface ficará disponível em:
- http://localhost:5173

A API ficará disponível em:
- http://localhost:8080

## Endpoints principais
- POST /api/auth/login
- GET /api/auth/me
- GET /api/clients
- POST /api/clients
- GET /api/vehicles
- GET /api/rental-requests

## Observação
O backend depende das credenciais do projeto Supabase configuradas localmente; não coloque senhas no repositório.

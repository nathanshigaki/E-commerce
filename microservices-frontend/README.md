# PulseCommerce - Frontend (Single Page Application)

Aplicação client desenvolvida em **Angular 18** com estilização via **Tailwind CSS**, responsável pela interface de usuário da plataforma PulseCommerce.

## 🚀 Tecnologias e Recursos

- **Angular 18**: Standalone components, signals e fluxo reativo com RxJS.
- **Tailwind CSS**: Design responsivo e moderno.
- **Autenticação OIDC / OAuth2**: Integração com Keycloak via biblioteca `angular-auth-oidc-client`.
- **HTTP Interceptor**: Injeção automática do Bearer JWT em requisições para a API Gateway (porta `9000`).

## 💻 Funcionalidades da Interface

1. **Catálogo de Produtos**: Listagem com preço e quantidade em estoque em tempo real.
2. **Criação e Edição de Produtos**: Cadastro e atualização de SKUs e preços.
3. **Gerenciamento de Estoque**: Consulta e atualização manual de saldo por SKU.
4. **Checkout e Pedidos**: Submissão de ordens de compra autenticadas.
5. **Autenticação com Keycloak**: Login/Logout integrado ao servidor de identidade.

## 🛠️ Como Executar

### Pré-requisitos
- Node.js (v18+)
- Angular CLI instalado globalmente (`npm install -g @angular/cli`)

### Passos
```bash
# 1. Instalar dependências
npm install

# 2. Iniciar servidor de desenvolvimento
ng serve
```
Acesse a aplicação em [http://localhost:4200](http://localhost:4200).

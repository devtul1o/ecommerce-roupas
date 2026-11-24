# Loja de Roupas — E-commerce Full-Stack em Java

<p align="center">
  <strong>Uma aplicação de e-commerce de vestuário desenvolvida com Java e Spring Boot.</strong>
  <br>
  Catálogo, carrinho persistente, checkout integrado ao Mercado Pago e gerenciamento administrativo.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21%2B-ED8B00?logo=openjdk&logoColor=white" alt="Java 21+">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/PostgreSQL-15%2B-4169E1?logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Bootstrap-5-7952B3?logo=bootstrap&logoColor=white" alt="Bootstrap 5">
  <img src="https://img.shields.io/badge/Thymeleaf-Template-005F0F?logo=thymeleaf&logoColor=white" alt="Thymeleaf">
  <img src="https://img.shields.io/badge/Payment-Mercado%20Pago-00AEEF?logo=mercadopago&logoColor=white" alt="Mercado Pago">
</p>

---

## Sobre o projeto

Aplicação web completa de e-commerce de vestuário desenvolvida com **Java, Spring Boot, Spring Data JPA, Spring Security, PostgreSQL, Thymeleaf e Bootstrap 5**, integrada ao **Mercado Pago Checkout Pro (Sandbox)** e à API **ViaCEP**.

O projeto utiliza arquitetura em camadas (**MVC + Service + Repository + DTOs**) e modelagem relacional com 16 entidades JPA. A aplicação contempla o ciclo de compra de ponta a ponta: navegação pelo catálogo, gerenciamento de estoque por variação, carrinho persistente, checkout com endereço, processamento de pagamentos em ambiente Sandbox, acompanhamento de pedidos e painel administrativo.

## Sumário

- [Funcionalidades](#funcionalidades)
  - [Área do cliente](#área-do-cliente)
  - [Área administrativa](#área-administrativa)
- [Tecnologias](#tecnologias)
- [Arquitetura e organização](#arquitetura-e-organização)
- [Modelagem de dados](#modelagem-de-dados)
- [Pré-requisitos](#pré-requisitos)
- [Configuração e execução](#configuração-e-execução)
- [Credenciais de teste](#credenciais-de-teste)
- [Testando o pagamento](#testando-o-pagamento)
- [Integrações externas](#integrações-externas)
- [Segurança e observações](#segurança-e-observações)

---

## Funcionalidades

### Área do cliente

- **Vitrine e catálogo (`/`):** listagem de produtos com imagens, preços formatados e acesso às páginas de detalhes.
- **Detalhes do produto:** seleção de variações de tamanho e cor, com validação da disponibilidade em estoque.
- **Cadastro e autenticação (`/cadastro` e `/login`):** registro de clientes com senhas protegidas por `BCrypt` e tela de login personalizada.
- **Carrinho de compras (`/carrinho`):** adição, atualização de quantidades e remoção de itens, com persistência no banco de dados.
- **Checkout (`/checkout`):**
  - Cadastro de endereços com preenchimento automático por meio da API ViaCEP.
  - Campo para informações complementares do endereço.
  - Seleção de cartão de crédito, cartão de débito ou boleto.
  - Controle dinâmico de parcelamento, habilitado exclusivamente para cartão de crédito.
- **Pagamento com Mercado Pago (Sandbox):**
  - Redirecionamento para o Mercado Pago Checkout Pro.
  - Filtragem dos meios de pagamento conforme a escolha do cliente.
  - Limitação do número de parcelas conforme a configuração selecionada no checkout.
- **Meus pedidos (`/meus-pedidos`):**
  - Histórico de compras.
  - Acompanhamento dos status `AGUARDANDO_PAGAMENTO`, `APROVADA` e `NEGADA`.
  - Retomada de pagamentos pendentes pela opção **Pagar Agora**.
  - Cancelamento de pedidos pendentes.
  - Visualização de código de rastreio, transportadora e status da entrega.

### Área administrativa

A área administrativa (`/admin`) é restrita aos usuários com perfil `ROLE_FUNCIONARIO`.

- Criação automática de um administrador padrão durante a inicialização, por meio da classe `DataInitializer`.
- Gerenciamento de categorias e marcas.
- Cadastro e gerenciamento de produtos.
- Gerenciamento de variações (tamanho e cor) e respectivos estoques.
- Gerenciamento logístico de pedidos.
- Cadastro de códigos de rastreio e transportadoras.
- Atualização do status das entregas.

---

## Tecnologias

| Camada | Tecnologia | Finalidade |
|---|---|---|
| Linguagem | Java 21+ | Desenvolvimento do backend |
| Framework | Spring Boot | Estrutura e execução da aplicação |
| API Web | Spring Web | Controladores e rotas HTTP |
| Persistência | Spring Data JPA | Acesso aos dados |
| ORM | Hibernate / Jakarta Persistence | Mapeamento objeto-relacional |
| Banco de dados | PostgreSQL 15+ | Persistência relacional |
| Segurança | Spring Security | Autenticação e autorização |
| Criptografia de senhas | BCrypt | Armazenamento seguro de senhas |
| Validação | Spring Boot Validation | Validação de dados |
| Templates | Thymeleaf | Renderização das páginas |
| Interface | HTML5, Bootstrap 5 e JavaScript | Construção da interface |
| Comunicação assíncrona | Fetch API | Requisições no frontend |
| Pagamentos | SDK Java do Mercado Pago | Integração com Checkout Pro |
| Consulta de endereço | ViaCEP | Busca de endereço por CEP |
| Build | Maven Wrapper | Gerenciamento e execução do projeto |

---

## Arquitetura e organização

O sistema adota uma arquitetura em camadas, separando apresentação, controle de requisições, regras de negócio e persistência.

### Estrutura de pacotes

A estrutura principal está organizada em `src/main/java/br/com/lojaroupas`:

```text
br.com.lojaroupas
├── config
│   ├── SecurityConfig
│   └── DataInitializer
│
├── controller
│   └── Controllers MVC
│       ├── Home
│       ├── Cadastro
│       ├── Carrinho
│       ├── Compra
│       └── Admin
│
├── dto
│   └── Objetos de transferência de dados
│       └── CheckoutDTO
│
├── exception
│   └── Exceções de domínio
│       └── RegraNegocioException
│
├── model
│   ├── Entidades JPA
│   └── enums
│       ├── StatusCompra
│       ├── MetodoPagamento
│       └── StatusEntrega
│
├── repository
│   └── Interfaces Spring Data JPA
│
└── service
    └── Regras de negócio
        ├── CompraService
        ├── CarrinhoService
        ├── MercadoPagoService
        └── Demais serviços
```

### Responsabilidades das camadas

- **Config:** configurações de segurança e inicialização de dados.
- **Controller:** recebe requisições HTTP, coordena o fluxo da aplicação e retorna páginas ou respostas.
- **DTO:** transporta dados entre as camadas, evitando a exposição direta das entidades.
- **Exception:** concentra exceções relacionadas às regras de negócio.
- **Model:** representa as entidades persistidas e seus relacionamentos.
- **Repository:** abstrai operações de consulta e persistência.
- **Service:** concentra as regras de negócio e a lógica operacional.

---

## Modelagem de dados

A aplicação utiliza PostgreSQL como banco de dados relacional e Hibernate/JPA para o mapeamento objeto-relacional.

### Herança entre usuários

A entidade `Pessoa` funciona como classe-base para `Cliente` e `Funcionario`. Essa estrutura centraliza atributos e comportamentos comuns e permite autenticação unificada por meio do Spring Security.

A diferenciação dos perfis ocorre pelas autoridades:

- `ROLE_CLIENTE`
- `ROLE_FUNCIONARIO`

Essas autoridades são utilizadas para controlar o acesso às funcionalidades e direcionar os usuários conforme seu perfil.

### Principais relacionamentos

| Relacionamento | Cardinalidade |
|---|---|
| Cliente — Carrinho | 1:1 |
| Carrinho — ItemCarrinho | 1:N |
| Compra — ItemCompra | 1:N |
| Compra — Entrega | 1:1 |
| Compra — Pagamento | 1:N |
| Pagamento — Parcela | 1:N |

### Integridade dos dados

Foram configuradas constraints `CHECK` no PostgreSQL para restringir os estados permitidos de compra:

- `AGUARDANDO_PAGAMENTO`
- `APROVADA`
- `NEGADA`

Essas restrições complementam as validações da aplicação e contribuem para a consistência dos dados persistidos.

---

## Pré-requisitos

Antes de executar o projeto, tenha instalado:

- **Java JDK 21 ou superior**.
- **PostgreSQL 15 ou superior**, localmente ou via Docker.
- **Git** para clonar o repositório.
- Uma **conta de desenvolvedor do Mercado Pago**, com acesso às credenciais de teste (Sandbox).

---

## Configuração e execução

### 1. Clone o repositório

```bash
git clone https://github.com/devtul1o/ecommerce-roupas.git
cd ecommerce-roupas
```

### 2. Configure o banco de dados

Crie um banco de dados PostgreSQL com o nome `lojaroupas`.

Confira as configurações de conexão no arquivo:

```text
src/main/resources/application.properties
```

Ajuste endereço, porta, nome do banco e credenciais de acordo com seu ambiente.

### 3. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto, no mesmo diretório do `pom.xml`:

```ini
MP_ACCESS_TOKEN=APP_USR-seu-access-token-de-teste-aqui
APP_BASE_URL=http://localhost:8080
```

> **Importante:** utilize o Access Token de teste disponível na seção *Credenciais de teste* do painel Mercado Pago Developers. Não compartilhe nem versione esse arquivo.

### 4. Execute a aplicação

O projeto utiliza Maven Wrapper, portanto não é necessário instalar o Maven globalmente.

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

Após a inicialização, acesse:

```text
http://localhost:8080
```

---

## Credenciais de teste

### Administrador padrão

A classe `DataInitializer` realiza a criação automática de um usuário administrador durante a inicialização da aplicação.

| Campo | Valor |
|---|---|
| E-mail | `admin@lojaroupas.com` |
| Senha | `admin123` |
| Painel administrativo | `http://localhost:8080/admin` |

> **Atenção:** essas credenciais são destinadas exclusivamente ao ambiente de desenvolvimento e testes. Altere-as antes de qualquer implantação em produção.

---

## Testando o pagamento

### Mercado Pago Checkout Pro — Sandbox

Para testar o fluxo de compra e pagamento:

1. Acesse `/cadastro` e registre uma conta de cliente.
2. Faça login em `/login`.
3. Navegue pelo catálogo e adicione um produto ao carrinho.
4. Acesse `/checkout`.
5. Cadastre um endereço informando o CEP. Os dados serão preenchidos automaticamente pela API ViaCEP.
6. Selecione **Cartão de Crédito**, escolha o número de parcelas e clique em **Confirmar Pedido**.
7. O sistema redirecionará para o Mercado Pago Checkout Pro em ambiente Sandbox.
8. Utilize um cartão de teste disponibilizado na documentação oficial do Mercado Pago e informe `APRO` como nome do titular para simular uma aprovação.
9. Após concluir o pagamento, utilize a opção **Voltar para o site**. A aplicação redirecionará para `/pagamento/sucesso`, atualizará o pedido para `APROVADA` e permitirá sua visualização em `/meus-pedidos`.
10. Acesse o painel administrativo com as credenciais de teste.
11. Cadastre o código de rastreio e a transportadora do pedido.
12. Retorne à conta do cliente e acesse `/meus-pedidos` para acompanhar as informações de entrega.

Documentação oficial: [Mercado Pago — Cartões de teste](https://www.mercadopago.com.br/developers/pt/docs/checkout-pro/additional-content/test-cards).

---

## Integrações externas

### Mercado Pago Checkout Pro

A integração utiliza o SDK oficial do Mercado Pago para Java e permite simular pagamentos em ambiente Sandbox.

O fluxo contempla a criação da preferência de pagamento, o redirecionamento do cliente para o Checkout Pro e o retorno à aplicação após a finalização da operação.

### ViaCEP

A API ViaCEP consulta informações de endereço a partir do CEP informado pelo cliente, automatizando o preenchimento dos campos correspondentes durante o checkout.

Documentação: [ViaCEP](https://viacep.com.br/).

---

## Segurança e observações

- As senhas dos usuários são protegidas com `BCrypt`.
- O acesso à área administrativa é restrito ao perfil `ROLE_FUNCIONARIO`.
- As credenciais do Mercado Pago devem ser fornecidas por variáveis de ambiente.
- O arquivo `.env` não deve ser enviado ao controle de versão.
- O ambiente Sandbox deve ser utilizado para testes de pagamento. Credenciais e cartões de teste não devem ser utilizados em produção.

---

<p align="center">
  Desenvolvido como projeto final para a disciplina Programação Orientada a Objetos por Túlio Santos de Souza Silva e Pedro Henrique Pereira Ramos.
</p>

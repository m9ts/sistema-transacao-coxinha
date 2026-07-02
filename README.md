# Bankinha - Totem de Autoatendimento de Salgados

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![JPA](https://img.shields.io/badge/JPA-2.0-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
[![H2 Database](https://img.shields.io/badge/H2-2.0-003D7A?style=for-the-badge&logo=h2&logoColor=white)](https://www.h2database.com/)
[![MVC](https://img.shields.io/badge/MVC-Pattern-563D7C?style=for-the-badge)](https://pt.wikipedia.org/wiki/MVC)

---

## Sobre o projeto

Este projeto consiste num sistema completo de autoatendimento para um **totem de venda de salgados (coxinhas)**, desenvolvido como critério de avaliação acadêmica. A aplicação simula um **caixa eletrónico moderno**, integrando um back-end robusto focado em regras de negócio desacopladas e persistência de dados em memória.

O grande diferencial técnico deste projeto é a implementação rigorosa de **5 Padrões de Projeto (Design Patterns) do GoF**, demonstrando maturidade em Programação Orientada a Objetos Avançada e arquitetura limpa.

---

## Design Patterns implementados

Os padrões encontram-se isolados no pacote `com.sistemabankcoxinha.patterns` e estruturam todo o ciclo de vida do sistema:

### 1. Command Pattern (`/patterns/command`)
**Responsabilidade:** Encapsula cada operação transacional como um objeto independente.

**Mecanismo de Desfazer (Undo):** As classes `ComprarCoxinhaCommand`, `InserirCreditoCommand` e `EstornarCompraCommand` executam validações, atualizações de estado e persistência. O **estorno** (`EstornarCompraCommand`) permite reverter uma compra de forma cirúrgica: devolve o valor exato ao saldo do cliente, marca a transação original como estornada e cria um novo registo de estorno no extrato, mantendo a rastreabilidade completa sem corromper o histórico.

### 2. Strategy Pattern (`/patterns/strategy`)
**Responsabilidade:** Alterna algoritmos de cálculo de troco dinamicamente.

**Aplicação:** A interface `TrocoStrategy` permite que o sistema utilize diferentes estratégias de troco. A implementação concreta `TrocoPadraoStrategy` aplica um **algoritmo guloso** (notas de maior valor primeiro) para minimizar a quantidade de cédulas devolvidas. Caso a combinação exata não seja possível, o sistema recusa a transação com a mensagem: *"Transação impossível: falta de cédulas específicas"*.

### 3. Observer Pattern (`/patterns/observer`)
**Responsabilidade:** Define uma dependência um-para-muitos para notificações orientadas a eventos.

**Aplicação:** Desacopla as rotinas de auditoria do fluxo principal de compras. Sempre que uma compra é concluída, o `CompraDisparaEvento` dispara notificações para todos os observadores assinados. O `LogObserver` gera logs de depuração, o `ExtratoObserver` alimenta o histórico de transações e o `EstoqueObserver` monitora a movimentação de notas.

### 4. Decorator Pattern (`/patterns/decorator`)
**Responsabilidade:** Adicionar comportamentos extras a objetos sem modificar a classe base.

**Aplicação:** As classes `CoxinhaRecheioDecorator` e `DescontoDecorator` envolvem uma coxinha base (ex: `CoxinhaFrango`) para adicionar recheio (+R$2) ou aplicar desconto (20%). Este padrão permite combinações dinâmicas e evita a explosão de subclasses (ex: `FrangoEspecial`, `FrangoComDesconto`, `CarneEspecialComRecheio`…).

### 5. Factory Pattern (`/patterns/factory`)
**Responsabilidade:** Centraliza a lógica de instanciação de subclasses polimórficas.

**Aplicação:** A `CoxinhaFactory` recebe o nome do sabor e flags de decoração, encapsula a criação dos objetos (`CoxinhaFrango`, `CoxinhaCarne`, `CoxinhaCostela`, etc.) e aplica os decoradores quando solicitado. O restante do sistema fica protegido do acoplamento direto com os construtores da hierarquia.

---

## Arquitetura e estrutura do código

A aplicação adota o padrão arquitetural **MVC (Model-View-Controller)**:

- **`model`** – Contém as entidades mapeadas via JPA (`Cliente`, `Movimentacao`, `SlotNota`) e os relacionamentos de banco de dados.
- **`repository`** – Interfaces que estendem `JpaRepository`, responsáveis pelas consultas e persistência.
- **`service`** – Camada que orquestra as regras de negócio, recupera dados e aciona fábricas, comandos e estratégias.
- **`controller`** – Camada de exposição da API REST, manipulando payloads JSON via anotações Spring (`@PostMapping`, `@GetMapping`, etc.).
- **`view`** – Interface rica construída com HTML5, CSS3 (estilo moderno) e Vanilla JavaScript assíncrono (Fetch API), localizada em `src/main/resources/static/`.

---

## Principais endpoints da API REST

| Categoria | Método | Endpoint | Descrição |
|-----------|--------|----------|-----------|
| **Clientes** | GET | `/clientes` | Lista todos os clientes cadastrados. |
| **Clientes** | POST | `/clientes` | Cria um novo cliente com nome, senha e saldo inicial R$0. |
| **Clientes** | GET | `/clientes/{id}/extrato` | Retorna o extrato completo do cliente (movimentações com data/hora). |
| **Crédito** | POST | `/credito` | Simula a inserção de uma cédula (R$2,5,10,20,50,100,200), incrementando o slot e o saldo. |
| **Compras** | POST | `/compras` | Efetua a compra de uma coxinha (com flags para recheio e desconto), atualiza saldo e registra movimento. |
| **Troca** | PUT | `/trocar-sabor` | Transfere o valor de uma coxinha reservada para outro sabor. |
| **Estorno** | POST | `/estorno/{movimentacaoId}` | Reverte a compra via Command, devolvendo o valor ao saldo e criando registo de estorno. |
| **Login** | POST | `/login` | Autentica o cliente com senha, retornando os dados do cliente. |
| **Slots** | GET | `/slots` | Retorna a quantidade disponível de cada cédula no caixa. |

---

## Tecnologias e ferramentas

### Back-end
- **Java 17** (LTS)
- **Spring Boot 3.0** – framework principal
- **Spring Data JPA** – persistência de dados
- **H2 Database** – banco de dados em memória (facilita demonstração)
- **Lombok** – redução de código boilerplate
- **Swagger/OpenAPI** – documentação interativa da API (acessível em `/swagger-ui.html`)

### Front-end
- **HTML5** – estrutura semântica
- **CSS3** – estilização com variáveis, flexbox e grid
- **Vanilla JavaScript** – requisições assíncronas com Fetch API, manipulação DOM e toast notifications

### Ferramentas
- **Git & GitHub** – controlo de versão
- **Maven** – gestão de dependências e build

---

### Como executar localmente

#### Pré-requisitos
- JDK 17 instalado
- Maven 3.8+ (opcional, se usar o terminal)

#### Passos

1. **Clone o repositório**
```bash
git clone https://github.com/m9ts/sistema-transacao-coxinha.git
cd sistema-transacao-coxinha
```

2. **Abra o projeto na IDE escolhida**
- File → Open → selecione a pasta do projeto.
- Aguarde o download das dependências Maven.

3. **Execute a aplicação**
- Localize a classe principal:  
  `src/main/java/com/sistemabankcoxinha/SistemaBankCoxinhaApplication.java`
- Clique com o botão direito → **Run 'SistemaBankCoxinhaApplication.main()'**  
  (ou use o ícone de play verde ao lado do método `main`).

   **Alternativa via terminal (Maven Wrapper):**
   ```bash
   ./mvnw spring-boot:run
   ```
   (O banco H2 será criado automaticamente em memória com 5 unidades de cada nota, cortesia do `DataLoader`.)

4. **Acesse o front-end**
- Abra o navegador e vá para:
```
http://localhost:8080
```

5. **Use a aplicação**
- Cadastre um cliente (nome e senha).
- Faça login.
- Insira crédito (notas).
- Compre coxinhas (com ou sem recheio/desconto).
- Veja o extrato e estorne compras se desejar.

6. **Consulte o banco de dados (opcional)**
- Acesse o console H2 em:
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:bankcoxinha`
- Usuário: `sa`
- Senha: *(deixe em branco)*

7. **Documentação da API**
- Acesse o Swagger em:
```
http://localhost:8080/swagger-ui.html
```
---

## Demonstração do fluxo principal

1. **Login** → selecione o cliente e digite a senha.
2. **Inserir crédito** → clique em uma cédula e depois em "Inserir nota". O saldo é atualizado e o slot da nota incrementado.
3. **Comprar coxinha** → escolha um sabor. Para "Frango" é possível adicionar recheio (+R$2), para "Carne" aplicar desconto (20%). O troco é mantido como saldo (crédito).
4. **Extrato** → visualize todas as movimentações com data/hora, tipo e valor. Cada compra exibe um botão "Estornar".
5. **Estornar** → clique em "Estornar", confirme e veja o saldo ser restaurado e o estorno aparecer no extrato.
6. **Cédulas** → verifique a disponibilidade de notas no caixa.

---

## Estrutura de diretórios (Resumida)

```
src/
├── main/
│   ├── java/
│   │   └── com/sistemabankcoxinha/
│   │       ├── controller/         # Endpoints REST
│   │       ├── service/            # Regras de negócio
│   │       ├── model/              # Entidades JPA
│   │       ├── repository/         # Interfaces JPA
│   │       ├── dto/                # Objetos de transferência
│   │       └── patterns/           # Padrões de Projeto
│   │           ├── command/        # Command Pattern
│   │           ├── decorator/      # Decorator Pattern
│   │           ├── factory/        # Factory Pattern
│   │           ├── observer/       # Observer Pattern
│   │           └── strategy/       # Strategy Pattern
│   └── resources/
│       ├── static/                 # Front-end (HTML, CSS, JS, imagens)
│       └── application.properties  # Configurações (H2, logs, etc.)
└── pom.xml                         # Dependências Maven
```

---


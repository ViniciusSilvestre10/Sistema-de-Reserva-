# 🗓️ Sistema de Reserva de Salas — API REST

API REST robusta desenvolvida em **Java 21** e **Spring Boot** para o gerenciamento e reserva de salas. O projeto aplica conceitos avançados de Orientação a Objetos, arquitetura em camadas, persistência relacional com Spring Data JPA, tratamento padronizado de erros e conteinerização.

---

## 🚀 Tecnologias Utilizadas

* **Java 21** (JDK Eclipse Temurin)
* **Spring Boot 4.0.6**
    * Spring Data JPA
    * Spring Web (REST)
* **PostgreSQL 16** (Banco de dados relacional)
* **Maven** (Gerenciador de dependências)
* **Docker & Dockerfile** (Multi-stage build com imagem Alpine para camadas mínimas)

---

## 🏛️ Arquitetura e Domínio

A aplicação segue o padrão de arquitetura em camadas (**Controller -> Service -> Repository**), garantindo o isolamento das regras de negócio e testabilidade.

### 📑 Modelagem do Domínio
* **Sala:** Identificador, nome, capacidade (deve ser positiva) e status de ativação.
* **Usuário:** Identificador, nome e credenciais.
* **Reserva:** Vincula `Sala` e `Usuário`. Possui data/hora de início e fim, além de status (`ATIVA`, `CANCELADA`).

### 🛠️ Regras de Negócio Implementadas
* **Validação de Domínio:** Bloqueio de reservas para salas inativas ou com datas inválidas (fim anterior ao início).
* **Regra de Não Sobreposição:** Uma sala não pode ter duas reservas ativas sobrepostas. A validação utiliza **intervalo semiaberto** $[início, fim)$ — onde o horário de término de uma reserva pode coincidir exatamente com o início de outra.
* **Persistência e Transações:** Métodos de escrita anotados com `@Transactional` para garantir atomicidade (evitando condições de corrida ao checar conflitos e salvar).

---

## 🛣️ Endpoints da API (Versionada `/api/v1`)

Todas as rotas possuem tratamento global de exceções, retornando erros padronizados em JSON (Status `400` para validações e `404` para recursos não encontrados).

| Recurso | Método | Endpoint | Descrição |
| :--- | :--- | :--- | :--- |
| **Sala** | `GET` | `/api/v1/sala` | Lista todas as salas (Paginado) |
| **Sala** | `POST` | `/api/v1/sala` | Cadastra uma nova sala |
| **Reserva** | `POST` | `/api/v1/reserva` | Cria uma reserva validando conflitos |
| **Reserva** | `DELETE` | `/api/v1/reserva/{id}` | Cancela uma reserva (muda status, libera a sala) |

---

## 🐳 Execução com Docker (Local)

O projeto conta com um manifesto `Dockerfile` otimizado utilizando **Multi-stage build**, reduzindo drasticamente o tempo de deploy.

### ⚙️ Parametrização por Ambiente
A aplicação mapeia as configurações de infraestrutura via variáveis de ambiente. Altere os valores conforme seu cenário:

```properties
DB_HOST=127.0.0.1
DB_PORT=5432
DB_NAME=sala
DB_USER=postgres
DB_PASSWORD=123
```
### 🏃 Como Executar a Aplicação

1. **Build da Imagem Docker:**
 ```bash
docker build -t app-sala:1.0 .
```

2. **Executar o Contêiner na Rede Host (para conectar ao seu banco local do Ubuntu):**

```bash
docker run -d \
--name minha-app-sala \
--network="host" \
-e DB_HOST=127.0.0.1 \
-e DB_PORT=5432 \
-e DB_NAME=sala \
-e DB_USER= user \
-e DB_PASSWORD= password \  
app-sala:1.0 
 ```

3. **Validar Funcionamento:**

```bash
curl -v http://localhost:8080/api/v1/sala
```
## 🧪 Testes de Unidade

Foco em cobertura de código para validação de fluxos críticos de negócio:

* **Testes da regra de conflito de horários:** Cobertura detalhada para cenários de sobreposição de bordas e intervalos.
* **Testes de validação de payload inválido:** Garantia de uma DX (Developer Experience) aprimorada com retornos de erro limpos e padronizados.
* **Garantia de isolamento:** Utilização de mocks estruturados para validar o comportamento isolado da camada de serviço.
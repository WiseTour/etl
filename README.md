# Projeto de ETL - Dados Turísticos

## ⚙️ Funcionamento do Processo ETL

### 1. Extração (Extract)
- Download de arquivos **Excel** a partir de um bucket **AWS S3**.
- Arquivos são lidos utilizando **Apache POI**.
- As classes principais desta etapa estão em:  
  `src/main/java/tour/wise/etl/extract`

### 2. Transformação (Transform)
- Aplicação de regras de limpeza, validação e transformação de dados.
- Geração de objetos DTOs para transporte interno.
- As classes principais desta etapa estão em:  
  `src/main/java/tour/wise/etl/transform`

### 3. Carga (Load)
- Os dados transformados são inseridos no banco de dados via classes DAO.
- Operações seguras e otimizadas via **JDBC**.
- As classes principais desta etapa estão em:  
  `src/main/java/tour/wise/etl/load`

---

## 🗂️ Estrutura de Diretórios

```
src/
 └── main/
      ├── java/
      │    └── tour/wise/
      │          ├── dao/          -> Acesso ao banco de dados (CRUD)
      │          ├── dto/          -> Objetos de transporte de dados (DTOs)
      │          ├── etl/          -> Processo ETL (Extract, Transform, Load)
      │          ├── model/        -> Modelos de entidades persistentes
      │          └── util/         -> Utilitários gerais (conexão DB, S3, Slack etc.)
      └── resources/
           └── config.properties   -> Configurações de ambiente (Banco de Dados e AWS)
```

---

## 🧱 Configuração Necessária Antes de Rodar

Crie um arquivo chamado `config.properties` dentro da pasta `src/main/resources` com o seguinte conteúdo:

```
DB_HOST=
DB_PORT=
DB_NAME=
DB_USERNAME=
DB_PASSWORD=

AWS_BUCKET_NAME=
AWS_BUCKET_REGION=
aws_access_key_id=
aws_secret_access_key=
aws_session_token=
```

> **⚠️ Importante:** Nunca suba esse arquivo para repositórios públicos. Preencha os valores com suas credenciais locais.

---

## 📄 Regras sobre os Arquivos de Entrada (S3)

O ETL processa **exatamente 4 arquivos por vez**, sendo obrigatórios:

- **1 arquivo de Chegadas**  
  Exemplo: `chegadas_turistas_internacionais.xlsx`

- **3 arquivos de Fichas Síntese**  
  (Uma de cada tipo: **Brasil**, **Estado** e **País**)

Exemplos de nomes válidos:
```
Ficha_Sintese_Brasil.xlsx
Ficha_Sintese_Estado.xlsx
Ficha_Sintese_Pais.xlsx
```

> **Observação:** Caso deseje utilizar fichas de um **ano diferente** do ano das chegadas, **adicione ao final do nome do arquivo de ficha síntese a indicação `(versao atual indisponivel)`**. Isso indica ao ETL que ele pode fazer o match com o ano das chegadas.

Exemplo de uso de versão indisponível:
```
Ficha_Sintese_Pais_(versao atual indisponivel).xlsx
```

**Regras obrigatórias:**
- Sempre devem existir os 3 tipos de ficha (Brasil, Estado, País)
- Precisa haver pelo menos 1 arquivo de Chegadas
- Todos os 4 arquivos devem estar juntos no S3 no momento da execução

---

## 🛠️ Tecnologias Utilizadas

- **Java**
- **AWS SDK** (integração com S3)
- **Apache POI** (leitura de Excel)
- **JDBC** (acesso ao MySQL)
- **Slack Webhook** (opcional para logs)

## ⚙️ Como Rodar

Antes de executar o ETL, é necessário garantir que o arquivo de configuração `config.properties` esteja corretamente configurado. Esse arquivo contém as informações essenciais para a conexão com o banco de dados, além dos diretórios de entrada e saída dos dados.

O caminho padrão do arquivo de configuração é:

src/main/resources/config.properties

---

### 📌 Importante:

- O `.jar` gerado pelo Maven estará dentro da pasta `/target`.
- A execução do ETL deve ser feita a partir da pasta `target`, para que o caminho relativo para o `config.properties` funcione corretamente.

---

### ✅ Exemplo de execução:

```bash
cd target
java -jar etl-1.0-SNAPSHOT-jar-with-dependencies.jar ../src/main/resources/config.properties

```

---

# 🌎 WiseTour - Análise de Dados Turísticos (Visão Geral)

O **WiseTour** é um sistema completo de **análise de dados de turismo internacional**, criado para ajudar **agências de turismo** a entender o perfil e o fluxo de turistas internacionais que visitam o Brasil. A plataforma entrega **dashboards dinâmicos** e para apoiar o direcionamento de campanhas de marketing e tomada de decisão.

---

## 🎯 Objetivo

Fornecer uma solução integrada para:

✅ **Monitorar o volume de chegadas de turistas internacionais ao Brasil**  
✅ **Analisar o perfil demográfico e comportamental dos turistas**  
✅ **Apoiar estratégias de marketing mais direcionadas e assertivas para o setor de turismo**

---

## 🗂️ Estrutura do Projeto

O projeto **WiseTour** é dividido em quatro módulos principais:

### 1. WiseTour (Aplicação Web - Frontend + Backend)

- Site institucional
- Área de cadastro e login de usuários
- Dashboard com gráficos interativos
- Filtros dinâmicos para exploração de dados

🔗 Repositório: [WiseTour - Aplicação Web](https://github.com/WiseTour/wise-tour)

---

### 2. ETL (Extração, Transformação e Carga de Dados)

- Processos de extração dos datasets oficiais
- Limpeza, transformação e carregamento dos dados no banco
- Registro de logs por etapa (extração, transformação, finalização)

🔗 Repositório: [WiseTour - ETL](https://github.com/WiseTour/etl)

---

### 3. Database (Banco de Dados e Modelo MER)

- Modelagem do banco de dados relacional
- Scripts de criação de tabelas, constraints e relacionamentos
- Scripts de inserção inicial de dados

🔗 Repositório: [WiseTour - Database](https://github.com/WiseTour/database)

---

### 4. Shell Scripts (Infraestrutura AWS)

- Automatização da criação do ambiente em nuvem (AWS)
- Configuração de servidor, banco de dados, backend e frontend
- Scripts de deploy para facilitar a instalação completa

🔗 Repositório: [WiseTour - Shell Scripts](https://github.com/WiseTour/shell-scripts)

---

## 📊 Fontes Oficiais de Dados

O projeto WiseTour utiliza dois conjuntos de dados públicos fundamentais para o turismo brasileiro:

### 📌 1. Estimativas de Chegadas de Turistas Internacionais ao Brasil

Este conjunto de dados contém informações detalhadas sobre o número de turistas internacionais que chegam ao Brasil, por país de origem, período e ponto de entrada.

🔗 [Acessar o dataset oficial](https://www.gov.br/turismo/pt-br/assuntos/estudos-e-pesquisas/demanda-internacional/estimativas-de-chegadas-de-turistas-internacionais)

---

### 📌 2. Estudo da Demanda Turística Internacional

Documento técnico que analisa tendências de fluxo turístico, perfil dos visitantes, motivos de viagem e fatores que influenciam a vinda de turistas ao Brasil.

🔗 [Acessar o estudo completo](https://www.gov.br/turismo/pt-br/assuntos/estudos-e-pesquisas/demanda-internacional/estudo-da-demanda-turistica-internacional)

---

## 🚀 Tecnologias Utilizadas

- **MySQL** (Banco de Dados)
- **Node.js + Express** (Backend)
- **Sequelize** (ORM)
- **HTML + CSS + JavaScript** (Frontend)
- **AWS EC2 e RDS** (Infraestrutura)
- **Apache POI + Java** (Processos ETL)
- **Shell Scripts** (Automação de infraestrutura)

---

## 🛠️ Como Executar o Projeto

1. Configure o ambiente AWS com os **[Shell Scripts](https://github.com/WiseTour/shell-scripts)**
2. Ajuste o script de inserção do banco de dados, caso seja necessário **[Database](https://github.com/WiseTour/database)**
3. Rode os processos de ETL para carregar os dados via **[ETL](https://github.com/WiseTour/etl)**
4. Acesse a aplicação Web que já estará clonada em seu ambiente da AWS**[WiseTour](https://github.com/WiseTour/wise-tour)**

> **⚠️ Importante:** o shell script prepara todo o ambiente, não será necessário clonar um repositório por vez.

---
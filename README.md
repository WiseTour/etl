# 🌐 WiseTour - ETL ✈️

Sistema de **Extração, Transformação e Carga** de dados turísticos para análise estratégica do setor de turismo internacional no Brasil.

---

## 📋 Visão Geral

O módulo ETL do WiseTour é responsável por processar dados oficiais do governo brasileiro sobre turismo internacional, transformando planilhas Excel em dados estruturados para análise estratégica por agências de turismo.

### 🎯 Objetivo
Automatizar o processamento de dados turísticos oficiais, extraindo informações sobre:
- **Chegadas de turistas internacionais** por nacionalidade e estado
- **Meios de transporte** utilizados para entrada no país
- **Distribuição geográfica e temporal** da demanda turística

---

## ⚙️ Funcionamento do Processo ETL

### 1. **Extração (Extract)**
- Download automatizado de arquivos **Excel** do bucket **AWS S3**
- Leitura de planilhas utilizando **Apache POI**
- Classes principais: `src/main/java/tour/wise/etl/extract`

### 2. **Transformação (Transform)**
- Aplicação de regras de limpeza e validação de dados
- Normalização e estruturação das informações
- Geração de objetos DTOs para transporte interno
- Classes principais: `src/main/java/tour/wise/etl/transform`

### 3. **Carga (Load)**
- Inserção segura dos dados transformados no banco MySQL
- Operações otimizadas via **JDBC**
- Controle de transações e rollback em caso de erro
- Classes principais: `src/main/java/tour/wise/etl/load`

---

## 🗂️ Estrutura do Projeto

```
src/
 └── main/
      ├── java/
      │    └── tour/wise/
      │          ├── dao/          -> Acesso ao banco de dados (CRUD)
      │          ├── dto/          -> Objetos de transporte de dados
      │          ├── etl/          -> Processo ETL (Extract, Transform, Load)
      │          │    ├── extract/ -> Classes de extração
      │          │    ├── transform/ -> Classes de transformação
      │          │    └── load/    -> Classes de carga
      │          ├── model/        -> Modelos de entidades persistentes
      │          └── util/         -> Utilitários (conexão DB, S3, Slack)
      └── resources/
           └── config.properties   -> Configurações de ambiente
```

---

## 📄 Regras dos Arquivos de Entrada

O ETL processa **exatamente 4 arquivos por execução**:

### ✅ Arquivos Obrigatórios

**1 arquivo de Chegadas:**
```
chegadas_2018.xlsx
```

**3 arquivos de Fichas Síntese** (um de cada tipo):
```
Ficha Sintese Brasil 2015-2019.xlsx
Ficha Sintese Estado 2015-2019.xlsx
Ficha Sintese Pais 2015-2019.xlsx
```

### 📅 Arquivos de Anos Diferentes

Caso precise usar fichas de **ano diferente** do arquivo de chegadas, adicione ao final do nome:
```
Ficha_Sintese_Pais_(versao atual indisponivel).xlsx
```

> **⚠️ Importante:** Todos os 4 arquivos devem estar presentes no S3 no momento da execução.

---

## 🛠️ Configuração

### 1. Arquivo de Configuração

Crie o arquivo `src/main/resources/config.properties`:

```properties
# Banco de Dados
DB_HOST=
DB_PORT=
DB_NAME=
DB_USERNAME=
DB_PASSWORD=

# AWS S3
AWS_BUCKET_NAME=
AWS_BUCKET_REGION=
aws_access_key_id=
aws_secret_access_key=
aws_session_token=
```

> **🔐 Segurança:** Nunca suba este arquivo para repositórios públicos.

### 2. Execução

```bash
# Compile o projeto
mvn clean package

# Execute o ETL
cd target
java -jar etl-1.0-SNAPSHOT-jar-with-dependencies.jar ../src/main/resources/config.properties
```

---

## 🔧 Tecnologias Utilizadas

| Tecnologia | Finalidade |
|------------|------------|
| **Java** | Linguagem principal |
| **Apache POI** | Leitura de arquivos Excel |
| **AWS SDK** | Integração com S3 |
| **JDBC** | Acesso ao banco MySQL |
| **Maven** | Gerenciamento de dependências |
| **Slack Webhook** | Notificações (opcional) |

---

## 📊 Fontes de Dados Oficiais

### 🏛️ Ministério do Turismo - Governo Federal

**Estimativas de Chegadas de Turistas Internacionais:**
- 🔗 [Dataset oficial](https://www.gov.br/turismo/pt-br/assuntos/estudos-e-pesquisas/demanda-internacional/estimativas-de-chegadas-de-turistas-internacionais)

**Estudo da Demanda Turística Internacional:**
- 🔗 [Estudo completo](https://www.gov.br/turismo/pt-br/assuntos/estudos-e-pesquisas/demanda-internacional/estudo-da-demanda-turistica-internacional)

---

## 🌐 Contexto no Projeto WiseTour

Este ETL faz parte do **ecossistema WiseTour**, sistema completo de análise de dados turísticos composto por:

### 📦 Módulos do Projeto

| Módulo | Descrição | Repositório |
|--------|-----------|-------------|
| **WiseTour Web** | Frontend + Backend da aplicação | [wise-tour](https://github.com/WiseTour/wise-tour) |
| **ETL** | Processamento de dados (este módulo) | [etl](https://github.com/WiseTour/etl) |
| **Database** | Modelagem e scripts do banco | [database](https://github.com/WiseTour/database) |
| **Shell Scripts** | Automação da infraestrutura AWS | [shell-scripts](https://github.com/WiseTour/shell-scripts) |

### 🎯 Público-Alvo
**Agências de turismo estrangeiro** que desejam estratégias baseadas em dados para:
- Segmentação de campanhas por nacionalidade
- Análise de sazonalidade turística
- Criação de pacotes personalizados
- Decisões orientadas por dados oficiais

---

## 📈 Fluxo de Dados

```
Dados Oficiais (Excel) → AWS S3 → ETL → MySQL → Dashboard WiseTour
```

1. **Governo** publica dados em Excel
2. **Arquivos** são armazenados no S3
3. **ETL** processa e carrega no banco
4. **Dashboard** consome dados estruturados
5. **Agências** tomam decisões estratégicas

---

## 💡 Motivação

Projeto desenvolvido após **visita técnica à Agaxtur Viagens** com o executivo **Ricardo Braga**, identificando necessidades reais do setor:

- ❌ **Alta concorrência** entre agências
- ❌ **Dificuldade na previsão de sazonalidade**
- ❌ **Falta de dados estruturados** para decisões

- ✅ **Solução:** Transformar dados públicos em **inteligência de mercado**

---

## 📄 Licença

Projeto acadêmico desenvolvido para o **Projeto Integrador da SPTECH School**.
Todos os direitos reservados aos autores e à instituição.

> **WiseTour ETL — Transformando dados governamentais em insights estratégicos para o turismo.**

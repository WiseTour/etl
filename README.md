# Projeto de ETL - Dados Turísticos

## 📚 Visão Geral
Este projeto realiza um processo de **ETL (Extração, Transformação e Carga)** para arquivos de dados turísticos armazenados no **Amazon S3**.  
Após a transformação, os dados são inseridos em um **banco de dados relacional** para posterior análise e uso em aplicações.

Todo o fluxo é modularizado para facilitar a manutenção, escalabilidade e rastreamento de erros.

---

## ⚙️ Funcionamento do Processo

### 1. Extração (Extract)
- **Download** dos arquivos **Excel** armazenados no **AWS S3**.
- Utilização da classe `S3Extractor` para conexão e extração dos arquivos.
- Leitura dos dados através da classe `ExcelReader`, usando a biblioteca **Apache POI**.

### 2. Transformação (Transform)
- Processamento dos dados extraídos utilizando a classe `DataTransformer`.
- Aplicação de **limpeza**, **validação** e **conversão** de dados para garantir integridade e consistência.
- Dados tratados são encapsulados em **DTOs (Data Transfer Objects)**.

### 3. Carga (Load)
- Inserção dos dados transformados no **banco de dados** via classes `DAO`.
- Operações de **CRUD** realizadas de forma segura e eficiente.

---

## 🗂️ Estrutura de Pacotes

```plaintext
/src
 ├── /dao       -> Operações de CRUD no banco de dados
 ├── /dto       -> Classes de transporte e transformação de dados
 ├── /etl       -> Processo de extração, leitura, transformação e carga
 ├── /util      -> Funções utilitárias diversas
 ├── /service   -> Coordenação do processo ETL e gerenciamento de logs
 ├── /log       -> Registro e gestão de logs de execução
 └── Main.java  -> Ponto de entrada da aplicação
```
---

## 🛠️ Tecnologias Utilizadas

- **Java**
- **AWS SDK** (integração com S3)
- **Apache POI** (manipulação de arquivos Excel)
- **JDBC** (conexão com o banco de dados)
- **Banco de Dados Relacional** (MySQL, PostgreSQL ou outro)

---

## 🎯 Objetivo
Automatizar a ingestão e armazenamento de dados turísticos, garantindo alta qualidade da informação para suporte a análises estratégicas e desenvolvimento de aplicações.

---
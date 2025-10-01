# bootcampSquadra | API de Cadastro de Endereços
## Descrição
O repositório apresenta no desafio final do Bootcamp New Thinkers Java da empresa [Squadra Digital](https://www.squadra.com.br/). 

Este projeto consiste em uma **API RESTful** desenvolvida em **Java** com o framework **Spring Boot**, destinada ao gerenciamento de dados cadastrais de pessoas e suas informações de localização (endereços, bairros, municípios e UFs).  
A aplicação foi estruturada seguindo as melhores práticas de desenvolvimento, como a **arquitetura em camadas**, para garantir manutenibilidade e escalabilidade.  

A exclusão de registros é tratada de forma lógica (**soft delete**), utilizando um campo de status (`1` para ativo, `2` para inativo).

---

## Funcionalidades
A API oferece um conjunto completo de operações **CRUD** (Criar, Ler, Atualizar, "Excluir") para as seguintes entidades:

- **Pessoa:** Cadastro de informações pessoais como nome, sobrenome, idade, login e senha.  
- **Endereço:** Gerenciamento de múltiplos endereços associados a uma pessoa.  
- **Bairro:** Cadastro de bairros vinculados a um município.  
- **Município:** Cadastro de municípios vinculados a uma UF.  
- **UF:** Cadastro das Unidades Federativas.  

### Detalhes das Funcionalidades
- **Criação:** Permite adicionar novos registros para todas as entidades.  
- **Leitura:** Oferece endpoints para listar todos os registros de uma entidade ou buscar registros específicos por **ID** e por outros parâmetros de filtro (como login, nome, sigla, etc.).  
- **Atualização:** Permite a modificação de registros existentes.  
- **Validação de Dados:** Utiliza o **Bean Validation** para garantir a integridade e o formato correto dos dados de entrada.  
- **Tratamento de Exceções:** Sistema centralizado de tratamento de exceções que retorna mensagens de erro padronizadas e claras no formato **JSON** (`ApiErrorFormat`).  

---

## Tecnologias Utilizadas
- **Linguagem:** Java 21  
- **Framework:** Spring Boot 3.3.5  
- **Spring Web:** Construção de APIs RESTful  
- **Spring Data JPA:** Persistência de dados com banco relacional  
- **Spring Validation:** Validação declarativa dos dados  
- **Banco de Dados:** Oracle (conexão via JDBC)  
- **ORM:** Hibernate  
- **Gerenciador de Dependências:** Maven  

### Bibliotecas Adicionais
- **Lombok:** Reduz a verbosidade do código em classes de modelo (VOs e DTOs).  
- **MapStruct:** Conversão eficiente e segura entre objetos de diferentes camadas (DTOs e VOs).  

### Servidor de Aplicação
- **Tomcat:** Embutido, fornecido pelo Spring Boot.  

---

## Estrutura do Projeto
O projeto segue uma **arquitetura em camadas**, promovendo separação de responsabilidades e facilitando a manutenção do código.

- **Controller:** Exposição dos endpoints da API, recebimento de requisições HTTP e retorno de respostas.  
- **Service:** Contém a lógica de negócio da aplicação. Orquestra operações, realiza validações e interage com a camada de repositório.  
- **Repository:** Interfaces do Spring Data JPA que abstraem a comunicação com o banco de dados.  
- **VO (Value Objects):** Classes que representam as entidades do banco de dados (tabelas).  
- **DTO (Data Transfer Objects):** Objetos de transferência de dados entre camadas e comunicação externa (requests/responses da API).  
- **Mapper:** Interfaces do **MapStruct** que definem as regras de conversão entre VOs e DTOs.  
- **Exception Handling:** Pacote com exceções personalizadas e um `@RestControllerAdvice` global para tratamento centralizado de erros.  


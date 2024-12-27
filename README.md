# bootcampSquadra | Projeto de Sistema de Gerenciamento de Pessoas

## Descrição

Este é um projeto desenvolvido em Java utilizando o framework Spring Boot para o gerenciamento de dados de pessoas e seus respectivos endereços. Ele permite criar, ler, atualizar informações relacionadas a pessoas, incluindo a associação e manipulação de endereços. A exclusão dos registros é representada pelo status, sendo que 1 é ativado e 2 é desativado.

## Funcionalidades

- Cadastro de pessoas com informações básicas (nome, sobrenome, idade, etc.).
- Gerenciamento de endereços associados a cada pessoa.
- Validações em nível de Bean para garantir a integridade dos dados.
- Tratamento de exceções personalizadas para erros comuns.
- Integração com um banco de dados relacional para armazenamento persistente.
- Retorno de mensagens padronizadas no formato de erro com `ApiErrorFormat`.


- A API oferece as seguintes funcionalidades principais para as entidades Pessoa, Endereço, Bairro, Município e Estado:
  - **Criar:** Permite adicionar novos registros para as entidades.
  - **Ler:** Recupera informações detalhadas de uma entidade pelo seu código, também pode-se utilizar filtros para a busca, ou listar todos os registros cadastrados.
  - **Atualizar:** Atualiza as informações de um registro existente.

## Tecnologias Utilizadas

- **Linguagem de Programação:** Java 21
- **Framework:** Spring Boot 3.3.5
  - Spring Data JPA
  - Spring Web
  - Spring Validation
  - Spring JDBC
  - Lombok
  - Spring AOP
- **Banco de Dados:** Oracle XE 21
- **Maven:** Gerenciador de dependências.
- **Tomcat:** Servidor de aplicação embutido.

## Estrutura do Projeto

### Camadas

- **Controller:** Responsável por receber as requisições HTTP e retornar as respostas apropriadas.
- **Service:** Contém a lógica de negócio e a interação com o repositório.
- **Repository:** Interface para comunicação com o banco de dados.
- **DTO (Data Transfer Objects):** Objetos para transferência de dados entre camadas.
- **VO (Value Objects):** Representação imutável de dados usados para respostas.
- **Mapper:** Conversão entre entidades e DTOs/VOs.
- **Exception Handling:** Tratamento de exceções personalizadas e erros comuns.

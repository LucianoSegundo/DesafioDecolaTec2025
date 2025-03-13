# Desafio DecolaTec 2025

O projeto é um fórum simples onde os usuários podem:

- Se cadastrar no sistema.
- Criar ou participar de fóruns já existentes.
- Fazer postagens nos fóruns.
- Comentar nas postagens feitas por outros usuários.

Esse sistema permite a interação entre os usuários por meio de postagens e comentários, criando um ambiente colaborativo dentro do fórum.

## Diagrama de classes

```mermaid
classDiagram
    class Usuario {
        +int id
        +String nome
        +String email
        +Date dataNascimento
        
    }

    class Postagem {
        +int id
        +String conteudo
        +Date dataCriacao
        +int usuarioId
       
    }

    class Forum {
        +int id
        +String titulo
        +String descricao
        
    }

    class Comentario {
        +int id
        +String conteudo
        +Date dataCriacao
        +int usuarioId
        +int postagemId
        
    }

    Usuario "1" -- "0..*" Postagem : cria
    Postagem "1" -- "0..*" Comentario : tem
    Forum "1" -- "0..*" Postagem : contém
    Usuario "1" -- "0..*" Comentario : comenta
 ```   
    
## Funcionalidades

### Funcionalidades disponíveis para um usuário não autenticado.

#### Controlador de Usuário

- **/api/v1/usuario/**: Endpoint de cadastro, por onde o usuário não cadastrado pode criar uma conta para acessar o sistema.
- **/api/v1/usuario/login**: Endpoint de login, por onde um usuário já cadastrado pode logar no sistema. Ao fornecer credenciais corretas, é retornado um JSON com o token de acesso e o tempo de duração.

### Funcionalidades disponíveis para um usuário autenticado.

#### Controlador de Usuário

- **/api/v1/usuario/**: Endpoint que deve ser acessado com o método DELETE para excluir a própria conta do usuário. É necessário fornecer a senha correta para confirmar a exclusão. Em caso de sucesso, realiza a deleção das postagens e comentários vinculados ao usuário.
- **/api/v1/usuario/{idUsuario}**: Endpoint para consulta de informações de um usuário, fornecendo o idUsuario.
- **/api/v1/usuario/listar**: Endpoint de consulta que retorna uma lista paginada de usuários, com o padrão de 10 itens, mas podendo ser alterado através de variáveis opcionais na URL.

#### Controlador de Fórum

- **/api/v1/forum/**: Endpoint para criação de fóruns. Retorna o fórum recém-criado. O fórum não é vinculado ao usuário que o criou, logo ele não tem permissão para excluí-lo.
- **/api/v1/forum/{forumID}**: Endpoint para consulta de um fórum, retornando o fórum com o ID solicitado.
- **/api/v1/forum/listar**: Endpoint de consulta paginada de fóruns, retornando uma lista paginada com o padrão de 10 itens, podendo ser alterada através de variáveis opcionais na URL.

#### Controlador de Postagens

- **/api/v1/postagem/{idForum}**: Endpoint para criação de postagens, recebe o idForum e retorna a postagem recém-criada.
- **/api/v1/postagem/{postagemId}**: Endpoint para deleção de postagens, acessível através do método DELETE. Deleta a postagem com o postagemId fornecido caso ela pertença ao usuário que solicitou a exclusão. Em caso de sucesso, também deleta os comentários vinculados à postagem.
- **/api/v1/postagem/{postagemId}**: Endpoint para consulta de postagem, acessível através de uma requisição GET, retornando a postagem com o postagemId fornecido.
- **/api/v1/postagem/listar/{forumId}**: Endpoint de consulta paginada de postagens associadas ao fórum com o forumId fornecido na URL. O padrão é 10 itens, mas pode ser alterado através de variáveis opcionais na URL.

#### Controlador de Comentários

- **/api/v1/comentarios/{postagemId}**: Endpoint para criação de comentários, recebe o postagemId e retorna o comentário recém-criado.
- **/api/v1/comentarios/{comentarioId}**: Endpoint para deleção de comentários, acessível através do método DELETE. Deleta o comentário com o comentarioId fornecido caso ele pertença ao usuário que solicitou.
- **/api/v1/comentarios/listar/{postagemId}**: Endpoint de consulta paginada de comentários associados à postagem com o postagemId fornecido na URL. O padrão é 10 itens, mas pode ser alterado através de variáveis opcionais na URL.

## Banco de Dados

Foi utilizado um banco de dados **H2** durante o período de desenvolvimento e o banco **PostgreSQL** para o perfil de produção.

## Tecnologias Empregadas

- Spring Web
- Spring Security
- Spring Data JPA
- H2 Database
- PostgreSQL

# DesafioDecolaTec2025

O projeto se trata de um forum simples, nele o usuário se cadastra, pode criar ou participar de um forum já existente, e fazer postagens nesse forum, e essas postagem podem ser comentadas por outros usuários;

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
    
    ```mermaid

# DesafioDecolaTec2025

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

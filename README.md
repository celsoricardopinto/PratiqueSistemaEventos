classDiagram
    class Usuario {
        -String id
        -String nome
        -String email
        -String cidade
        -List~Evento~ eventosConfirmados
        +Usuario(id, nome, email, cidade)
        +confirmarPresenca(Evento evento)
        +cancelarPresenca(Evento evento)
        +visualizarEventosConfirmados()
        +getEmail(): String
        +getId(): String
        +getNome(): String
        +getEventosConfirmados(): List~Evento~
        +equals(Object obj): boolean
        +hashCode(): int
    }

    class Evento {
        -String id
        -String nome
        -String endereco
        -CategoriaEvento categoria
        -LocalDateTime horario
        -String descricao
        +Evento(id, nome, endereco, categoria, horario, descricao)
        +getNome(): String
        +getEndereco(): String
        +getCategoria(): CategoriaEvento
        +getHorario(): LocalDateTime
        +getDescricao(): String
        +getId(): String
        +toString(): String
        +equals(Object obj): boolean
        +hashCode(): int
    }

    class CategoriaEvento {
        <<enum>>
        FESTA
        ESPORTIVO
        SHOW
        CULTURAL
        EDUCACIONAL
        GASTRONOMICO
        OUTROS
    }

    class SistemaEventos {
        -List~Usuario~ usuarios
        -List~Evento~ eventos
        -Scanner scanner
        -Usuario currentUser
        +SistemaEventos()
        +main(String[] args)
        +inicializar()
        +salvarEventos()
        +cadastrarUsuario()
        +loginUsuario()
        +cadastrarEvento()
        +consultarEventosDisponiveis()
        +confirmarParticipacao()
        +cancelarParticipacao()
        +visualizarMeusEventos()
        +ordenarEventosPorHorario()
        +verificarStatusEventos()
        +exibirMenuPrincipal()
        -listarEventos(List~Evento~ eventos)
    }

    class GerenciadorArquivo {
        +static carregarEventos(String nomeArquivo): List~Evento~
        +static salvarEventos(String nomeArquivo, List~Evento~ eventos): void
    }

    SistemaEventos --> Usuario : gerencia
    SistemaEventos --> Evento : gerencia
    Evento o-- CategoriaEvento : tem
    Usuario "1" -- "0..*" Evento : confirma presença em
    SistemaEventos ..> GerenciadorArquivo : usa

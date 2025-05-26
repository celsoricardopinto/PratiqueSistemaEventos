Estrutura do Projeto e Paradigma Orientado a Objetos

Organização o projeto em classes bem definidas, encapsulando dados e comportamentos, conforme o paradigma orientado a objetos.

Classes Principais
Usuario: Representa um usuário do sistema.

Atributos:

id (String - gerado automaticamente, UUID)

nome (String)

email (String)

cidade (String) - assumindo que a cidade de residência é a mesma dos eventos

eventosConfirmados (List&lt;Evento>) - lista de eventos que o usuário confirmou presença.

Métodos:

Construtor


Getters para todos os atributos

confirmarPresenca(Evento evento)

cancelarPresenca(Evento evento)

visualizarEventosConfirmados()

equals() e hashCode() (para comparação baseada no id ou email)

Evento: Representa um evento na cidade.

Atributos:

id (String - gerado automaticamente, UUID)

nome (String)

endereco (String)

categoria (CategoriaEvento - um enum)

horario (LocalDateTime - ideal para data e hora)

descricao (String)

Métodos:

Construtor

Getters para todos os atributos

toString() (para exibição amigável)

equals() e hashCode() (para comparação baseada no id)

CategoriaEvento: enum para delimitar as categorias de eventos.

Valores sugeridos: 

FESTA, ESPORTIVO, SHOW, CULTURAL, EDUCACIONAL, GASTRONOMICO, OUTROS.

SistemaEventos: 

Classe principal que orquestra as operações do sistema.

Atributos:

usuarios (List&lt;Usuario>)

eventos (List&lt;Evento>)

scanner (Scanner - para entrada do usuário)

currentUser (Usuario - o usuário logado no momento)

Métodos:

inicializar(): Carrega dados do arquivo events.data.

salvarEventos(): Salva os eventos no arquivo events.data.

cadastrarUsuario()

loginUsuario()

cadastrarEvento()

consultarEventosDisponiveis()

confirmarParticipacao()

cancelarParticipacao()

visualizarMeusEventos()

ordenarEventosPorHorario()

verificarStatusEventos() (Eventos em andamento, futuros, passados)

exibirMenuPrincipal()

GerenciadorArquivo: Classe utilitária para lidar com a leitura e escrita do arquivo events.data.

Métodos estáticos:

carregarEventos(String nomeArquivo): Retorna uma List<Evento>.

salvarEventos(String nomeArquivo, List<Evento> eventos): Salva a lista de eventos.

(Opcional) Métodos para salvar/carregar usuários, se o cadastro de usuário precisar de persistência além da sessão. Por simplicidade inicial, podemos focar a persistência apenas em eventos.

Diagrama de Classes (Esboço)

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

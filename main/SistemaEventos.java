package com.seuprojeto.main;

import com.seuprojeto.model.CategoriaEvento;
import com.seuprojeto.model.Evento;
import com.seuprojeto.model.Usuario;
import com.seuprojeto.service.GerenciadorArquivo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SistemaEventos {

    private List<Usuario> usuarios; // Simplesmente para demonstração de login, não persistido
    private List<Evento> eventos;
    private Scanner scanner;
    private Usuario currentUser; // Usuário atualmente logado

    public SistemaEventos() {
        this.usuarios = new ArrayList<>(); // Em um sistema real, usuários seriam persistidos
        this.eventos = GerenciadorArquivo.carregarEventos(); // Carrega eventos ao iniciar
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }

    public static void main(String[] args) {
        SistemaEventos sistema = new SistemaEventos();
        sistema.exibirMenuPrincipal();
    }

    public void exibirMenuPrincipal() {
        int opcao;
        do {
            if (currentUser == null) {
                System.out.println("\n--- Bem-vindo ao Sistema de Eventos ---");
                System.out.println("1. Cadastrar Usuário");
                System.out.println("2. Fazer Login");
                System.out.println("0. Sair");
            } else {
                System.out.println("\n--- Menu Principal (Usuário: " + currentUser.getNome() + ") ---");
                System.out.println("1. Cadastrar Novo Evento");
                System.out.println("2. Consultar Eventos Disponíveis");
                System.out.println("3. Ver Meus Eventos Confirmados");
                System.out.println("4. Cancelar Participação em Evento");
                System.out.println("5. Verificar Status dos Eventos (ocorrendo, futuros, passados)");
                System.out.println("6. Logout");
                System.out.println("0. Sair");
            }

            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            if (currentUser == null) {
                switch (opcao) {
                    case 1: cadastrarUsuario(); break;
                    case 2: loginUsuario(); break;
                    case 0: System.out.println("Saindo do sistema..."); break;
                    default: System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                switch (opcao) {
                    case 1: cadastrarEvento(); break;
                    case 2: consultarEventosDisponiveis(); break;
                    case 3: currentUser.visualizarEventosConfirmados(); break;
                    case 4: cancelarParticipacao(); break;
                    case 5: verificarStatusEventos(); break;
                    case 6: logoutUsuario(); break;
                    case 0: System.out.println("Saindo do sistema..."); break;
                    default: System.out.println("Opção inválida. Tente novamente.");
                }
            }
            GerenciadorArquivo.salvarEventos(eventos); // Salva eventos após cada operação relevante
        } while (opcao != 0);

        scanner.close();
    }

    private void cadastrarUsuario() {
        System.out.println("\n--- Cadastro de Usuário ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Cidade de residência: ");
        String cidade = scanner.nextLine();

        Usuario novoUsuario = new Usuario(nome, email, cidade);
        // Verifica se o email já existe
        if (usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            System.out.println("Email já cadastrado. Por favor, faça login ou use outro email.");
        } else {
            usuarios.add(novoUsuario);
            System.out.println("Usuário " + nome + " cadastrado com sucesso!");
        }
    }

    private void loginUsuario() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        // Procura o usuário pelo email
        Usuario foundUser = usuarios.stream()
                                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                                    .findFirst()
                                    .orElse(null);

        if (foundUser != null) {
            currentUser = foundUser;
            System.out.println("Login realizado com sucesso! Bem-vindo(a), " + currentUser.getNome() + "!");
        } else {
            System.out.println("Email não encontrado. Por favor, cadastre-se.");
        }
    }

    private void logoutUsuario() {
        currentUser = null;
        System.out.println("Logout realizado com sucesso.");
    }

    private void cadastrarEvento() {
        if (currentUser == null) {
            System.out.println("Você precisa estar logado para cadastrar eventos.");
            return;
        }

        System.out.println("\n--- Cadastro de Novo Evento ---");
        System.out.print("Nome do Evento: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        CategoriaEvento categoria = null;
        while (categoria == null) {
            System.out.println("Categorias disponíveis: ");
            for (CategoriaEvento cat : CategoriaEvento.values()) {
                System.out.print(cat.name() + " ");
            }
            System.out.print("\nCategoria: ");
            try {
                categoria = CategoriaEvento.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Categoria inválida. Por favor, escolha uma das opções listadas.");
            }
        }

        LocalDateTime horario = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        while (horario == null) {
            System.out.print("Horário (DD/MM/AAAA HH:MM): ");
            String horarioStr = scanner.nextLine();
            try {
                horario = LocalDateTime.parse(horarioStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Use DD/MM/AAAA HH:MM.");
            }
        }

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Evento novoEvento = new Evento(nome, endereco, categoria, horario, descricao);
        eventos.add(novoEvento);
        System.out.println("Evento '" + nome + "' cadastrado com sucesso!");
    }

    private void consultarEventosDisponiveis() {
        if (eventos.isEmpty()) {
            System.out.println("Não há eventos cadastrados no momento.");
            return;
        }

        // Ordena por horário antes de exibir
        ordenarEventosPorHorario();

        System.out.println("\n--- Eventos Disponíveis ---");
        listarEventos(eventos);

        if (currentUser != null) {
            System.out.print("Deseja confirmar participação em algum evento? (sim/nao): ");
            String escolha = scanner.nextLine().toLowerCase();
            if (escolha.equals("sim")) {
                confirmarParticipacao();
            }
        }
    }

    private void listarEventos(List<Evento> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum evento para listar.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private void confirmarParticipacao() {
        if (currentUser == null) {
            System.out.println("Você precisa estar logado para confirmar participação.");
            return;
        }

        System.out.print("Digite o ID do evento que deseja confirmar participação: ");
        String idEvento = scanner.nextLine();

        Evento eventoSelecionado = eventos.stream()
                                         .filter(e -> e.getId().equals(idEvento))
                                         .findFirst()
                                         .orElse(null);

        if (eventoSelecionado != null) {
            currentUser.confirmarPresenca(eventoSelecionado);
        } else {
            System.out.println("Evento com ID " + idEvento + " não encontrado.");
        }
    }

    private void cancelarParticipacao() {
        if (currentUser == null) {
            System.out.println("Você precisa estar logado para cancelar participação.");
            return;
        }

        currentUser.visualizarEventosConfirmados(); // Mostra os eventos confirmados primeiro
        if (currentUser.getEventosConfirmados().isEmpty()) {
            return; // Sai se não houver eventos confirmados
        }

        System.out.print("Digite o ID do evento que deseja cancelar participação: ");
        String idEvento = scanner.nextLine();

        Evento eventoParaCancelar = currentUser.getEventosConfirmados().stream()
                                               .filter(e -> e.getId().equals(idEvento))
                                               .findFirst()
                                               .orElse(null);

        if (eventoParaCancelar != null) {
            currentUser.cancelarPresenca(eventoParaCancelar);
        } else {
            System.out.println("Evento com ID " + idEvento + " não encontrado na sua lista de confirmados.");
        }
    }

    private void ordenarEventosPorHorario() {
        eventos.sort(Comparator.comparing(Evento::getHorario));
    }

    private void verificarStatusEventos() {
        if (eventos.isEmpty()) {
            System.out.println("Não há eventos cadastrados para verificar o status.");
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Eventos Ocorrendo Agora
        List<Evento> ocorrendoAgora = eventos.stream()
                .filter(e -> agora.isAfter(e.getHorario()) && agora.isBefore(e.getHorario().plusHours(2))) // Exemplo: duração de 2h
                .collect(Collectors.toList());
        System.out.println("\n--- Eventos Ocorrendo Agora ---");
        if (ocorrendoAgora.isEmpty()) {
            System.out.println("Nenhum evento ocorrendo no momento.");
        } else {
            ocorrendoAgora.forEach(System.out::println);
        }

        // Eventos Futuros (ordenados)
        List<Evento> futuros = eventos.stream()
                .filter(e -> e.getHorario().isAfter(agora))
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
        System.out.println("\n--- Próximos Eventos ---");
        if (futuros.isEmpty()) {
            System.out.println("Nenhum evento futuro cadastrado.");
        } else {
            futuros.forEach(System.out::println);
        }

        // Eventos Passados (ordenados por mais recente primeiro)
        List<Evento> passados = eventos.stream()
                .filter(e -> e.getHorario().isBefore(agora))
                .sorted(Comparator.comparing(Evento::getHorario).reversed())
                .collect(Collectors.toList());
        System.out.println("\n--- Eventos Já Ocorreram ---");
        if (passados.isEmpty()) {
            System.out.println("Nenhum evento passado registrado.");
        } else {
            passados.forEach(System.out::println);
        }
    }
}
package com.seuprojeto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String cidade; // Cidade de residência do usuário
    private List<Evento> eventosConfirmados;

    public Usuario(String nome, String email, String cidade) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
        this.eventosConfirmados = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCidade() { return cidade; }
    public List<Evento> getEventosConfirmados() { return eventosConfirmados; }

    public void confirmarPresenca(Evento evento) {
        if (!eventosConfirmados.contains(evento)) {
            eventosConfirmados.add(evento);
            System.out.println("Presença confirmada no evento: " + evento.getNome());
        } else {
            System.out.println("Você já confirmou presença neste evento.");
        }
    }

    public void cancelarPresenca(Evento evento) {
        if (eventosConfirmados.remove(evento)) {
            System.out.println("Presença cancelada no evento: " + evento.getNome());
        } else {
            System.out.println("Você não estava confirmado neste evento.");
        }
    }

    public void visualizarEventosConfirmados() {
        if (eventosConfirmados.isEmpty()) {
            System.out.println("Você não confirmou presença em nenhum evento ainda.");
        } else {
            System.out.println("\n--- Seus Eventos Confirmados ---");
            eventosConfirmados.forEach(System.out::println);
            System.out.println("--------------------------------");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email); // Compara por e-mail para login
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
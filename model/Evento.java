package com.seuprojeto.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Evento implements Serializable {
    private static final long serialVersionUID = 1L; // Necessário para serialização
    private String id;
    private String nome;
    private String endereco;
    private CategoriaEvento categoria;
    private LocalDateTime horario;
    private String descricao;

    public Evento(String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao) {
        this.id = UUID.randomUUID().toString(); // Gera um ID único
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
    }

    // Construtor para carregar de arquivo, se o ID já existir
    public Evento(String id, String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public CategoriaEvento getCategoria() { return categoria; }
    public LocalDateTime getHorario() { return horario; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "ID: " + id +
               "\n  Nome: " + nome +
               "\n  Endereço: " + endereco +
               "\n  Categoria: " + categoria +
               "\n  Horário: " + horario.format(formatter) +
               "\n  Descrição: " + descricao +
               "\n--------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(id, evento.id); // Compara por ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
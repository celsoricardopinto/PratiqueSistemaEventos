package com.seuprojeto.service;

import com.seuprojeto.model.Evento;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivo {

    private static final String NOME_ARQUIVO_EVENTOS = "events.data";

    // Salva a lista de eventos em um arquivo binário
    public static void salvarEventos(List<Evento> eventos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO_EVENTOS))) {
            oos.writeObject(eventos);
            System.out.println("Eventos salvos com sucesso em " + NOME_ARQUIVO_EVENTOS);
        } catch (IOException e) {
            System.err.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    // Carrega a lista de eventos de um arquivo binário
    @SuppressWarnings("unchecked") // Ignora o warning de type cast inseguro
    public static List<Evento> carregarEventos() {
        File file = new File(NOME_ARQUIVO_EVENTOS);
        if (!file.exists() || file.length() == 0) { // Verifica se o arquivo existe e não está vazio
            System.out.println("Arquivo de eventos não encontrado ou vazio. Iniciando com lista vazia.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO_EVENTOS))) {
            return (List<Evento>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar eventos: " + e.getMessage());
            return new ArrayList<>(); // Retorna lista vazia em caso de erro
        }
    }
}
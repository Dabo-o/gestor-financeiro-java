package br.com.seufinancas.model;

import java.time.LocalDate;

public class Despesa extends Transacao {

    private Categoria categoria; 

    // Construtor para CRIAR nova despesa
    public Despesa(double valor, String descricao, Categoria categoria) {
        super(valor, descricao); // Chama o construtor da classe Transacao
        this.categoria = categoria;
    }

    // Construtor para LER despesa do banco
    public Despesa(int id, double valor, String descricao, LocalDate data, Categoria categoria) {
        super(id, valor, descricao, data); // Chama o construtor completo de Transacao
        this.categoria = categoria;
    }

    @Override
    public String getTipo() {
        return "Despesa";
    }

    // Getters e Setters específicos de Despesa
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
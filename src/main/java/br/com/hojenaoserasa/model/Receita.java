package br.com.seufinancas.model;

import java.time.LocalDate;

public class Receita extends Transacao {

    private Categoria categoria;

    // Construtor para CRIAR nova receita
    public Receita(double valor, String descricao, Categoria categoria) {
        super(valor, descricao); // Chama o construtor da classe Transacao
        this.categoria = categoria;
    }

    // Construtor para LER receita do banco
    public Receita(int id, double valor, String descricao, LocalDate data, Categoria categoria) {
        super(id, valor, descricao, data); // Chama o construtor completo de Transacao
        this.categoria = categoria;
    }

    @Override
    public String getTipo() {
        return "Receita";
    }

    // Getters e Setters específicos de Receita
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
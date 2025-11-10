package br.com.hojenaoserasa.service;

import br.com.hojenaoserasa.model.Categoria;
import br.com.hojenaoserasa.model.Despesa;
import br.com.hojenaoserasa.model.Receita;
import br.com.hojenaoserasa.model.Transacao;
import br.com.hojenaoserasa.util.ConexaoBanco;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoService {

    // CREATE
    public void salvarNovaTransacao(Transacao t) {
        String sql = "INSERT INTO transacoes(tipo, valor, descricao, data, categoria) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String tipo;
            Categoria categoria;
            if (t instanceof Despesa) {
                tipo = "DESPESA";
                categoria = ((Despesa) t).getCategoria();
            } else {
                tipo = "RECEITA";
                categoria = ((Receita) t).getCategoria();
            }

            pstmt.setString(1, tipo);
            pstmt.setDouble(2, t.getValor());
            pstmt.setString(3, t.getDescricao());
            pstmt.setString(4, t.getData().toString());
            pstmt.setString(5, categoria.name());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    t.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar transação: " + e.getMessage());
        }
    }

    // READ
    public List<Transacao> listarTodasTransacoes() {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM transacoes";

        try (Connection conn = ConexaoBanco.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                double valor = rs.getDouble("valor");
                String descricao = rs.getString("descricao");
                LocalDate data = LocalDate.parse(rs.getString("data"));
                Categoria categoria = Categoria.valueOf(rs.getString("categoria"));

                Transacao t;
                if (tipo.equals("RECEITA")) {
                    t = new Receita(id, valor, descricao, data, categoria);
                } else {
                    t = new Despesa(id, valor, descricao, data, categoria);
                }
                transacoes.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar transações: " + e.getMessage());
        }
        return transacoes;
    }

    // --- CRUD: Update ---
    public void atualizarTransacao(Transacao t) {
        String sql = "UPDATE transacoes SET valor = ?, descricao = ?, data = ?, categoria = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            Categoria categoria;
            if (t instanceof Despesa) {
                categoria = ((Despesa) t).getCategoria();
            } else {
                categoria = ((Receita) t).getCategoria();
            }
            pstmt.setDouble(1, t.getValor());
            pstmt.setString(2, t.getDescricao());
            pstmt.setString(3, t.getData().toString());
            pstmt.setString(4, categoria.name());
            pstmt.setInt(5, t.getId());
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar transação: " + e.getMessage());
        }
    }

    // --- CRUD: Delete ---
    public void deletarTransacao(int id) {
        String sql = "DELETE FROM transacoes WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar transação: " + e.getMessage());
        }
    }

    // --- Lógica de Relatório ---
    public double calcularSaldoAtual() {
        double saldo = 0;
        List<Transacao> transacoes = listarTodasTransacoes(); // Reutiliza o método de listagem

        for (Transacao t : transacoes) {
            if (t instanceof Receita) {
                saldo += t.getValor();
            } else {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }
}
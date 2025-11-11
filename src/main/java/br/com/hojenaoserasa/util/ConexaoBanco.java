package br.com.hojenaoserasa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBanco {


    private static final String URL = "jdbc:sqlite:financas.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do SQLite não encontrado.", e);
        }
        return DriverManager.getConnection(URL);
    }

    public static void criarTabelas() {
        String sqlTransacoes = "CREATE TABLE IF NOT EXISTS transacoes (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " tipo TEXT NOT NULL," + 
                " valor REAL NOT NULL," +
                " descricao TEXT," +
                " data TEXT NOT NULL," + 
                " categoria TEXT NOT NULL" +
                ");";

        String sqlOrcamentos = "CREATE TABLE IF NOT EXISTS orcamentos (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " categoria TEXT NOT NULL UNIQUE," + 
                " valorLimite REAL NOT NULL" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlTransacoes);
            stmt.execute(sqlOrcamentos);
            System.out.println("Tabelas verificadas/criadas com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }
}
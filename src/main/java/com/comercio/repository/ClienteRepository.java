package com.comercio.repository;

import com.comercio.config.DBConfig;
import com.comercio.entity.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ClienteRepository {

    private final Properties credenciais = DBConfig.getCredenciais();

    public Cliente salvarCliente(Cliente cliente) throws SQLException {
        String url = credenciais.getProperty("url");
        String user = credenciais.getProperty("user");
        String senha = credenciais.getProperty("senha");

        String sql = "INSERT INTO clientes (nome, telefone, estabelecimento) VALUES (?, ?, ?)";

        try(Connection conexao = DriverManager.getConnection(url, user, senha);
            PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getTelefone());
            comando.setString(3, cliente.getEstabelecimento());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    Cliente c = new Cliente();
                    c.setId(resultado.getInt(1));
                    c.setNome(cliente.getNome());
                    c.setEstabelecimento(cliente.getEstabelecimento());

                    return c;
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Cliente buscaPorId(int idCliente) throws SQLException {
        String url = credenciais.getProperty("url");
        String user = credenciais.getProperty("user");
        String senha = credenciais.getProperty("senha");

        String sql = "SELECT * FROM clientes WHERE id = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, senha);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCliente);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Cliente c = new Cliente();
                    c.setId(resultado.getInt(1));
                    c.setNome(resultado.getString(2));
                    c.setTelefone(resultado.getString(3));
                    c.setEstabelecimento(resultado.getString(4));

                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Cliente atualizarCliente(Cliente cliente) throws SQLException {
        String url = credenciais.getProperty("url");
        String user = credenciais.getProperty("user");
        String senha = credenciais.getProperty("senha");

        String sql ="UPDATE clientes " +
                    "SET nome = ?, telefone = ?, estabelecimento = ? " +
                    "WHERE id = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, senha);
            PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getTelefone());
            comando.setString(3, cliente.getEstabelecimento());
            comando.setInt(4, cliente.getId());

            int resultado = comando.executeUpdate();

            return resultado > 0 ? cliente : null;
        }
    }

    public Cliente deletarCliente(Cliente cliente) throws SQLException {
        String url = credenciais.getProperty("url");
        String user = credenciais.getProperty("user");
        String senha = credenciais.getProperty("senha");

        String sql = "DELETE FROM clientes WHERE id = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, senha);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, cliente.getId());

            int resultado = comando.executeUpdate();

            return resultado > 0 ? cliente : null;
        }
    }
    public boolean deletarClientePorId(Integer id) throws SQLException {
        String url = credenciais.getProperty("url");
        String user = credenciais.getProperty("user");
        String senha = credenciais.getProperty("senha");

        String sql = "DELETE FROM clientes WHERE id = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, senha);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int resultado = comando.executeUpdate();

            return resultado > 0;
        }
    }

    public List<Cliente> listarClientes(int limite, int offset) throws SQLException {
        String url = credenciais.getProperty("url");
        String user = credenciais.getProperty("user");
        String senha = credenciais.getProperty("senha");

        String sql = "SELECT * FROM clientes LIMIT ? OFFSET ?";

        try(Connection conexao = DriverManager.getConnection(url, user, senha);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, limite);
            comando.setInt(2, offset);

            List<Cliente> clientes = new ArrayList<>(limite);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    Cliente c = new Cliente();
                    c.setId(resultado.getInt(1));
                    c.setNome(resultado.getString(2));
                    c.setTelefone(resultado.getString(3));
                    c.setEstabelecimento(resultado.getString(4));

                    clientes.add(c);
                }

                return clientes;
            }
        }
    }
}

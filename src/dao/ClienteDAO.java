package dao;

import model.Cliente;
import util.Conexao;
import util.SenhaUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ClienteDAO {
	
	public Cliente autenticar(String email, String senha) throws SQLException {

			  Connection conexao = null;
			  PreparedStatement stmt = null;
			  ResultSet rs = null;

			  try {
			    conexao = Conexao.abrir();

			    stmt = conexao.prepareStatement(
			      "SELECT * FROM cliente " +
			      "WHERE email=? AND ativo=1"
			    );

			    stmt.setString(1, email.trim());
			    rs = stmt.executeQuery();

			    if (rs.next()) {
			      Cliente cliente = mapear(rs);

			      if (
			        SenhaUtil.conferir(
			          senha,
			          cliente.getSenhaSalt(),
			          cliente.getSenhaHash()
			        )
			      ) {
			        return cliente;
			      }
			    }

			    return null;
			  } finally {
			    Conexao.fechar(conexao, stmt, rs);
			  }
			}

  public void salvar(Cliente cliente) throws SQLException {
    String sql =
      "INSERT INTO cliente (nome, cpf, email, data_nascimento, senha_hash, senha_salt, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";

    Connection conexao = null;
    PreparedStatement stmt = null;

    try {
      conexao = Conexao.abrir();
      stmt = conexao.prepareStatement(
        sql,
        Statement.RETURN_GENERATED_KEYS
      );

      preencher(stmt, cliente, false);
      stmt.executeUpdate();

      ResultSet chaves = null;

      try {
        chaves = stmt.getGeneratedKeys();

        if (chaves.next()) {
          cliente.setId(chaves.getInt(1));
        }
      } finally {
        Conexao.fechar(chaves);
      }
    } finally {
      Conexao.fechar(conexao, stmt, null);
    }
  }

  public void atualizar(Cliente cliente) throws SQLException {
    String sqlComSenha =
      "UPDATE cliente SET nome=?, cpf=?, email=?, data_nascimento=?, senha_hash=?, senha_salt=?, ativo=? WHERE id=?";

    String sqlSemSenha =
      "UPDATE cliente SET nome=?, cpf=?, email=?, data_nascimento=?, ativo=? WHERE id=?";

    boolean mudarSenha =
      cliente.getSenhaHash() != null &&
      cliente.getSenhaHash().length() > 0;

    Connection conexao = null;
    PreparedStatement stmt = null;

    try {
      conexao = Conexao.abrir();
      stmt = conexao.prepareStatement(
        mudarSenha ? sqlComSenha : sqlSemSenha
      );

      if (mudarSenha) {
        preencher(stmt, cliente, true);
      } else {
        stmt.setString(1, cliente.getNome().trim());
        stmt.setString(2, cliente.getCpf().trim());
        stmt.setString(3, cliente.getEmail().trim());
        stmt.setDate(
          4,
          new java.sql.Date(cliente.getDataNascimento().getTime())
        );
        stmt.setBoolean(5, cliente.isAtivo());
        stmt.setInt(6, cliente.getId());
      }

      if (stmt.executeUpdate() == 0) {
        throw new SQLException("Cliente nao encontrado.");
      }
    } finally {
      Conexao.fechar(conexao, stmt, null);
    }
  }

  
  public void excluir(int id) throws SQLException {
    alterarAtivo(id, false);
  }

  public void alterarAtivo(int id, boolean ativo) throws SQLException {
    Connection conexao = null;
    PreparedStatement stmt = null;

    try {
      conexao = Conexao.abrir();
      stmt = conexao.prepareStatement(
        "UPDATE cliente SET ativo=? WHERE id=?"
      );

      stmt.setBoolean(1, ativo);
      stmt.setInt(2, id);

      if (stmt.executeUpdate() == 0) {
        throw new SQLException("Cliente nao encontrado.");
      }
    } finally {
      Conexao.fechar(conexao, stmt, null);
    }
  }

  public Cliente buscarPorId(int id) throws SQLException {
    List<Cliente> lista = consultar(
      "SELECT * FROM cliente WHERE id=?",
      Integer.valueOf(id)
    );

    return lista.isEmpty() ? null : lista.get(0);
  }

  public List<Cliente> buscarPorNome(String nome) throws SQLException {
    return consultar(
      "SELECT * FROM cliente WHERE TRIM(nome) LIKE ? ORDER BY nome",
      "%" + nome.trim() + "%"
    );
  }

  public List<Cliente> listarTodos() throws SQLException {
    return consultar(
      "SELECT * FROM cliente ORDER BY nome",
      null
    );
  }

  public List<Cliente> listarAtivos() throws SQLException {
    return consultar(
      "SELECT * FROM cliente WHERE ativo=1 ORDER BY nome",
      null
    );
  }

  private List<Cliente> consultar(String sql, Object parametro)
    throws SQLException {

    List<Cliente> lista = new ArrayList<Cliente>();
    Connection conexao = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      conexao = Conexao.abrir();
      stmt = conexao.prepareStatement(sql);

      if (parametro instanceof Integer) {
        stmt.setInt(
          1,
          ((Integer) parametro).intValue()
        );
      }

      if (parametro instanceof String) {
        stmt.setString(1, (String) parametro);
      }

      rs = stmt.executeQuery();

      while (rs.next()) {
        lista.add(mapear(rs));
      }

      return lista;
    } finally {
      Conexao.fechar(conexao, stmt, rs);
    }
  }

  private Cliente mapear(ResultSet rs) throws SQLException {
    Cliente cliente = new Cliente();

    cliente.setId(rs.getInt("id"));
    cliente.setNome(rs.getString("nome"));
    cliente.setCpf(rs.getString("cpf"));
    cliente.setEmail(rs.getString("email"));
    cliente.setDataNascimento(rs.getDate("data_nascimento"));
    cliente.setSenhaHash(rs.getString("senha_hash"));
    cliente.setSenhaSalt(rs.getString("senha_salt"));
    cliente.setAtivo(rs.getBoolean("ativo"));

    return cliente;
  }

  private void preencher(
    PreparedStatement stmt,
    Cliente cliente,
    boolean atualizacao
  ) throws SQLException {

    stmt.setString(1, cliente.getNome().trim());
    stmt.setString(2, cliente.getCpf().trim());
    stmt.setString(3, cliente.getEmail().trim());
    stmt.setDate(
      4,
      new java.sql.Date(cliente.getDataNascimento().getTime())
    );
    stmt.setString(5, cliente.getSenhaHash());
    stmt.setString(6, cliente.getSenhaSalt());
    stmt.setBoolean(7, cliente.isAtivo());

    if (atualizacao) {
      stmt.setInt(8, cliente.getId());
    }
  }
}
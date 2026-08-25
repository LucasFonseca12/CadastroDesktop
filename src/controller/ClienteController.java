package controller;

import dao.ClienteDAO;
import model.Cliente;
import util.SenhaUtil;
import util.Validador;
import view.TelaClientes;
import java.text.ParseException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;


public class ClienteController {

  private final TelaClientes tela;
  private final ClienteDAO dao;

  public ClienteController(TelaClientes tela) {
    this.tela = tela;
    this.dao = new ClienteDAO();
  }

  public void novo() {
    tela.limparFormulario();
    tela.definirEdicao(true);
    tela.getTxtNome().requestFocus();
  }

  public void limpar() {
    tela.limparFormulario();
    tela.definirEdicao(true);
  }

  public void carregarTabela() {
    consultar(false);
  }

  public void buscar() {
    consultar(true);
  }

  public void salvar() {
    try {
      validar();

      Cliente cliente = lerFormulario();
      
      String senha =
    		  new String(tela.getTxtSenha().getPassword());

    		if (senha.length() > 0) {
    		  String salt = SenhaUtil.gerarSalt();

    		  cliente.setSenhaSalt(salt);
    		  cliente.setSenhaHash(
    		    SenhaUtil.gerarHash(senha, salt)
    		  );
    		}

      if (cliente.getId() == 0) {
        dao.salvar(cliente);

        mensagem(
          "Cliente cadastrado com sucesso.",
          JOptionPane.INFORMATION_MESSAGE
        );
      } else {
        dao.atualizar(cliente);

        mensagem(
          "Cliente atualizado com sucesso.",
          JOptionPane.INFORMATION_MESSAGE
        );
      }

      limpar();
      carregarTabela();
    } catch (Exception e) {
      erro(e);
    }
  }

  public void excluir() {
    int id = tela.getIdSelecionado();

    if (id == 0) {
      mensagem(
        "Selecione um cliente.",
        JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    if (
      JOptionPane.showConfirmDialog(
        tela,
        "Deseja inativar este cliente?",
        "Confirmacao",
        JOptionPane.YES_NO_OPTION
      ) == JOptionPane.YES_OPTION
    ) {
      try {
        dao.excluir(id);

        mensagem(
          "Cliente inativado.",
          JOptionPane.INFORMATION_MESSAGE
        );

        limpar();
        carregarTabela();
      } catch (SQLException e) {
        erro(e);
      }
    }
  }

  public void selecionarLinha() {
    int linha = tela.getTabela().getSelectedRow();

    if (linha < 0) {
      return;
    }

    int id =
      ((Integer) tela.getTabela().getValueAt(linha, 0)).intValue();

    try {
      Cliente cliente = dao.buscarPorId(id);

      if (cliente != null) {
        tela.mostrarCliente(cliente);
      }
    } catch (SQLException e) {
      erro(e);
    }
  }

  private Cliente lerFormulario() throws ParseException {
    Cliente cliente = new Cliente();

    cliente.setId(tela.getIdSelecionado());
    cliente.setNome(tela.getTxtNome().getText().trim());
    cliente.setCpf(tela.getTxtCpf().getText().trim());
    cliente.setEmail(tela.getTxtEmail().getText().trim());
    cliente.setDataNascimento(
      Validador.converterData(
        tela.getTxtDataNascimento().getText()
      )
    );
    cliente.setAtivo(
      tela.getChkAtivo().isSelected()
    );


    return cliente;
  }

  private void validar() {
    if (
      Validador.vazio(tela.getTxtNome().getText())
    ) {
      throw new IllegalArgumentException(
        "Informe o nome."
      );
    }

    if (
      Validador.vazio(tela.getTxtCpf().getText())
    ) {
      throw new IllegalArgumentException(
        "Informe o CPF."
      );
    }

    if (
      Validador.vazio(tela.getTxtEmail().getText())
    ) {
      throw new IllegalArgumentException(
        "Informe o e-mail."
      );
    }

    if (
      !Validador.emailValido(tela.getTxtEmail().getText())
    ) {
      throw new IllegalArgumentException(
        "Informe um e-mail valido."
      );
    }

    if (
      Validador.vazio(
        tela.getTxtDataNascimento().getText()
      )
    ) {
      throw new IllegalArgumentException(
        "Informe a data de nascimento."
      );
    }

    try {
      Validador.converterData(
        tela.getTxtDataNascimento().getText()
      );
    } catch (ParseException e) {
      throw new IllegalArgumentException(
        "Informe uma data valida no formato dd/MM/yyyy."
      );
    }

    if (
      tela.getIdSelecionado() == 0 &&
      tela.getTxtSenha().getPassword().length == 0
    ) {
      throw new IllegalArgumentException(
        "Informe a senha do novo cliente."
      );
    }
  }

  private void consultar(boolean filtro) {
    try {
      List<Cliente> lista = filtro
        ? dao.buscarPorNome(
            tela.getTxtPesquisa().getText()
          )
        : dao.listarTodos();

      tela.preencherTabela(lista);
    } catch (SQLException e) {
      erro(e);
    }
  }

  private void mensagem(String mensagem, int tipo) {
    JOptionPane.showMessageDialog(
      tela,
      mensagem,
      "Cadastro de clientes",
      tipo
    );
  }

  private void erro(Exception e) {
    e.printStackTrace();

    mensagem(
      "Nao foi possivel concluir a operacao.\n" +
      e.getMessage(),
      JOptionPane.ERROR_MESSAGE
    );
  }
}
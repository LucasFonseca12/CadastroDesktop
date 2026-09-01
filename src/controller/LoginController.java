package controller;

import dao.ClienteDAO;
import model.Cliente;
import util.Validador;
import view.TelaLogin;
import view.TelaPrincipal;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginController {

  private final TelaLogin tela;
  private final ClienteDAO dao = new ClienteDAO();

  public LoginController(TelaLogin t) {
    tela = t;
  }

  public void entrar() {
	String email = tela.getTxtLogin().getText().trim();
    String senha = new String(tela.getTxtSenha().getPassword());
    if (Validador.vazio(email) || Validador.vazio(senha)) {
      JOptionPane.showMessageDialog(
        tela,
        "Informe e-mail e senha.",
        "Login",
        JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    try {
      Cliente cliente = dao.autenticar(email, senha);
      if (cliente == null) {
        JOptionPane.showMessageDialog(
          tela,
          "E-mail ou senha invalidos, ou cliente inativo.",
          "Login",
          JOptionPane.ERROR_MESSAGE
        );
        tela.getTxtSenha().setText("");
        tela.getTxtSenha().requestFocus();
        return;
      }
      tela.dispose();
      new TelaPrincipal(cliente).setVisible(true);
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
        tela,
        "Falha ao acessar o banco.\n" + e.getMessage(),
        "Login",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  public void sair() {
    if (
      JOptionPane.showConfirmDialog(
        tela,
        "Deseja encerrar o sistema?",
        "Sair",
        JOptionPane.YES_NO_OPTION
      ) == JOptionPane.YES_OPTION
    ) System.exit(0);
  }
}
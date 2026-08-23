package view;

import model.Cliente;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.view.TelaAutores;
import br.com.biblioteca.view.TelaCategorias;
import br.com.biblioteca.view.TelaClientes;
import br.com.biblioteca.view.TelaDevolucoes;
import br.com.biblioteca.view.TelaEditoras;
import br.com.biblioteca.view.TelaEmprestimos;
import br.com.biblioteca.view.TelaExemplares;
import br.com.biblioteca.view.TelaLivros;
import br.com.biblioteca.view.TelaPrincipal;
import br.com.biblioteca.view.TelaUsuarios;

public class TelaPrincipal extends JFrame {

	  private final JTabbedPane abas = new JTabbedPane();
	  private final Cliente cliente;

	  public TelaPrincipal(Cliente cliente) {
	    super("Sistema de Gestao de Biblioteca");
	    this.cliente = cliente;
	    montar();
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    setMinimumSize(new Dimension(920, 650));
	    setSize(1100, 760);
	    setLocationRelativeTo(null);
	    addWindowListener(
	      new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          sair();
	        }
	      }
	    );
	  }

	  private void montar() {
	    setJMenuBar(criarMenu());
	    JPanel inicio = new JPanel(new GridBagLayout());
	    JLabel texto = new JLabel("Selecione uma funcionalidade no menu acima, seu energumeno.");
	    texto.setFont(texto.getFont().deriveFont(Font.BOLD, 18f));
	    inicio.add(texto);
	    abas.addTab("Inicio", inicio);
	    add(abas, BorderLayout.CENTER);
	    JPanel rodape = new JPanel(new BorderLayout());
	    rodape.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
	    rodape.add(
	      new JLabel(
	        "Usuario: " + cliente.getNome() + " (" + cliente.getEmail() + ")"
	      ),
	      BorderLayout.WEST
	    );
	    rodape.add(
	      new JLabel("Sistema Clientes - Java Desktop(in the veins)"),
	      BorderLayout.EAST
	    );
	    add(rodape, BorderLayout.SOUTH);
	  }

	  private JMenuBar criarMenu() {
	    JMenuBar barra = new JMenuBar();
	    JMenu cadastros = new JMenu("Cadastros"),
	      movimentacoes = new JMenu("Movimentacoes"),
	      consultas = new JMenu("Consultas"),
	      sistema = new JMenu("Sistema");
	    JMenuItem clientes = item("Clientes", 1);
	    cliente.setEnabled("ADMIN".equals(cliente.getEmail()));
	    cadastros.add(clientes);
	    cadastros.addSeparator();
	    consultas.add(item("Clientes", 1));
	    JMenuItem sobre = new JMenuItem("Sobre"),
	      sair = new JMenuItem("Sair");
	    sobre.addActionListener(
	      new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          JOptionPane.showMessageDialog(
	            TelaPrincipal.this,
	            "Sistema de overdose de java na veia\nJava SE 6 + Swing + JDBC + MySQL",
	            "Sobre",
	            JOptionPane.INFORMATION_MESSAGE
	          );
	        }
	      }
	    );
	    sair.addActionListener(
	      new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          sair();
	        }
	      }
	    );
	    sistema.add(sobre);
	    sistema.addSeparator();
	    sistema.add(sair);
	    barra.add(cadastros);
	    barra.add(movimentacoes);
	    barra.add(consultas);
	    barra.add(sistema);
	    return barra;
	  }

	  private JMenuItem item(String titulo, final int modulo) {
	    JMenuItem item = new JMenuItem(titulo);
	    item.addActionListener(
	      new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          abrirModulo(modulo);
	        }
	      }
	    );
	    return item;
	  }

	  private void abrirModulo(int modulo) {
	    String titulo;
	    JPanel painel;
	    if (modulo == 1) {
	      titulo = "Clientes";
	      if (selecionar(titulo)) return;
	      painel = new TelaClientes();
	    } 
	    abrirAba(titulo, painel);
	  }

	  /** Reutiliza uma aba existente para impedir Clientes, Clientes, Clientes... */
	  public void abrirAba(String titulo, JPanel painel) {
	    int i = abas.indexOfTab(titulo);
	    if (i >= 0) {
	      abas.setSelectedIndex(i);
	      return;
	    }
	    abas.addTab(titulo, painel);
	    abas.setTabComponentAt(
	      abas.indexOfComponent(painel),
	      cabecalhoFechavel(titulo, painel)
	    );
	    abas.setSelectedComponent(painel);
	  }

	  private boolean selecionar(String titulo) {
	    int i = abas.indexOfTab(titulo);
	    if (i >= 0) {
	      abas.setSelectedIndex(i);
	      return true;
	    }
	    return false;
	  }

	  private JPanel cabecalhoFechavel(String titulo, final Component painel) {
	    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
	    p.setOpaque(false);
	    p.add(new JLabel(titulo));
	    JButton fechar = new JButton("x");
	    fechar.setMargin(new Insets(0, 4, 0, 4));
	    fechar.setToolTipText("Fechar aba");
	    fechar.addActionListener(
	      new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          abas.remove(painel);
	        }
	      }
	    );
	    p.add(fechar);
	    return p;
	  }

	  private void sair() {
	    if (
	      JOptionPane.showConfirmDialog(
	        this,
	        "Deseja encerrar o sistema?",
	        "Sair",
	        JOptionPane.YES_NO_OPTION
	      ) == JOptionPane.YES_OPTION
	    ) {
	      dispose();
	      System.exit(0);
	    }
	  }
	}

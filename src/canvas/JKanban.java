package canvas;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import util.Atributo;
import util.EventoMouse;
import util.TratarArquivo;

public class JKanban extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPopupMenu popMenu;

  public JKanban() {
    super(Atributo.TITULO);
    this.getContentPane().setBackground(new Color(43, 117, 63));
    this.setSize(940, 670);
    this.setLocationRelativeTo(null);

    // Objetos do Menu Interno
    popMenu = new JPopupMenu();
    JMenuItem itmTarefa = new JMenuItem("Tarefa");
    JMenuItem itmAbrir = new JMenuItem("Abrir");
    JMenuItem itmSalvar = new JMenuItem("Salvar");
    JMenuItem itmSalvarImg = new JMenuItem("Salvar Imagem");

    // Acoes do Menu
    itmTarefa.addActionListener(e -> adicionarTarefa());
    itmAbrir.addActionListener(e -> abrirArquivo());
    itmSalvar.addActionListener(e -> salvarArquivo());
    itmSalvarImg.addActionListener(e -> salvarImagem());
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent evt) {
        clkMouse(evt);
      }
    });
    JPanel pnInferior = new JPanel();
    pnInferior.add(new JLabel(Atributo.COPYRIGHT));
    this.add(pnInferior, BorderLayout.SOUTH);
    
    // Montagem do Menu
    popMenu.add(itmTarefa);
    popMenu.addSeparator();
    popMenu.add(itmAbrir);
    popMenu.add(itmSalvar);
    popMenu.add(itmSalvarImg);
    this.setVisible(true);
    JOptionPane.showMessageDialog(this, "Dica: Clique com o botão direito do mouse\npara criar as tarefas.", Atributo.TITULO, 1);
    this.repaint();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (this.isVisible()) {
      g.setColor(Color.WHITE);
      g.drawRect(10, 40, 300, 600);
      g.drawRect(320, 40, 300, 600);
      g.drawRect(630, 40, 300, 600);
      g.setFont(new Font(g.getFont().getFamily(), Font.BOLD, 18));
      g.drawString("A Fazer", 20, 60);
      g.drawString("Fazendo", 330, 60);
      g.drawString("Feito", 640, 60);
    }
  }

  // Metodos Eventos

  private void adicionarTarefa() {
    String nome = JOptionPane.showInputDialog(this, "Descrição (máx. 2 palavras significativas)", "Criar Tarefa", 3);
    if (nome != null) {
      criarPostIt(nome, 255, 255, 0, 50, 50);
    }
    this.repaint();
  }

  private void abrirArquivo() {
    TratarArquivo ta = new TratarArquivo(this);
    ta.abrirArquivo();
    this.repaint();
  }

  private void salvarArquivo() {
    TratarArquivo ta = new TratarArquivo(this);
    ta.salvarArquivo();
    this.repaint();
  }
  
  private void salvarImagem() {
    if (JOptionPane.showConfirmDialog(this, "Confirma salvar Imagem", "Confirmar?", 0) == 0) {
      try {
        Thread.sleep(2000L);
        BufferedImage bi = new Robot().createScreenCapture(new Rectangle(this.getX(), this.getY()+35, this.getWidth(), this.getHeight()-60));
        TratarArquivo ta = new TratarArquivo(this);
        ta.salvarImagem(bi);
        JOptionPane.showMessageDialog(this, "Imagem Salva sem problemas.", Atributo.TITULO, 1);
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
      } catch (AWTException e) {
        JOptionPane.showMessageDialog(this, "Ocorreu erro: " + e.getMessage(), "Erro Informa\347\343o", 0);
        e.printStackTrace();
      }
    }
  }

  private void clkMouse(MouseEvent e) {
    if (e.isMetaDown()) {
      popMenu.show(this, e.getX(), e.getY());
    }
  }

  // Metodos Auxiliares

  public void criarPostIt(String nome, int corR, int corG, int corB, int posX, int posY) {
    PostIt pi = new PostIt(this);
    pi.setBackground(new Color(corR, corG, corB));
    pi.setBounds(new Rectangle(posX, posY, 70, 40));
    new EventoMouse().insEvento(this, pi, nome, "Post-It");
    this.add(pi, null);
    this.repaint();
  }

  // Metodo Principal
  public static void main(String[] args) {
    new JKanban().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
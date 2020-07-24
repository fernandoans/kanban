package canvas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

import util.EventoMouse;
import util.TratarArquivo;

@SuppressWarnings("serial")
public class JKanban extends JFrame implements Runnable {

    private JPopupMenu popMenu;
    private Thread th;

    public JKanban() {
        super("jKanban - Versão 0.20 - Fernando Anselmo - 2013");
        this.getContentPane().setLayout(null);
        this.getContentPane().setBackground(new Color(0, 51, 0));
        this.setResizable(false);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new java.awt.Dimension(940, 640));
        this.setLocation((screenSize.width-940)/2,(screenSize.height-640)/2);

        // Objetos do Menu Interno
        popMenu = new JPopupMenu();
        JMenuItem itmTarefa = new JMenuItem("Tarefa");
        JMenuItem itmAbrir = new JMenuItem("Abrir");
        JMenuItem itmSalvar = new JMenuItem("Salvar");
        JMenuItem itmSalvarImg = new JMenuItem("Salvar Imagem");

        // Acoes do Menu
        itmTarefa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				adicionarTarefa();
			}
		});
        itmAbrir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				abrirArquivo();
			}
		});
        itmSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				salvarArquivo();
			}
		});
        itmSalvarImg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				salvarImagem();
			}
		});
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                clkMouse(evt);
            }
        });
        
        // Montagem do Menu
        popMenu.add(itmTarefa);
        popMenu.addSeparator();
        popMenu.add(itmAbrir);
        popMenu.add(itmSalvar);
        popMenu.add(itmSalvarImg);
        this.setVisible(true);
        JOptionPane.showMessageDialog(this, "Dica: Clique com o botão direito do mouse\npara criar as tarefas.");
    }

    @Override
    public void paint(Graphics g) {
    	super.paint(g);
    	g.setColor(Color.WHITE);
    	
    	g.drawRect(10, 30, 300, 600);
    	g.drawRect(320, 30, 300, 600);
    	g.drawRect(630, 30, 300, 600);

    	g.setFont(new Font("Helvetica", Font.BOLD, 18));
    	g.drawString("A Fazer", 20, 55);
    	g.drawString("Fazendo", 330, 55);
    	g.drawString("Feito", 640, 55);
    }    
    
    // Metodos Eventos

    private void adicionarTarefa() {
    	String nome = JOptionPane.showInputDialog(this, "Descrição (máx. 2 palavras significativas)", "Conteúdo");
    	if (nome != null) {
    		criarPostIt(nome, 255, 255, 0, 50, 50);
    	}
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
    	this.repaint();
    	th = new Thread(this);
    	th.start();
    }

    private void clkMouse(MouseEvent e) {
        if (e.isMetaDown()) {
            popMenu.show(this, e.getX(), e.getY());
        }
    }    
    
    @SuppressWarnings("static-access")
	public void run() {
		try {
			th.sleep(200);
	        Robot r = new Robot();
			BufferedImage bi = r.createScreenCapture( 
					new Rectangle(this.getX(), this.getY(), 
							this.getWidth(), this.getHeight()));
	        
			TratarArquivo ta = new TratarArquivo(this);
			ta.salvarImagem(bi);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    // Metodos Auxiliares
    
    public void criarPostIt(String nome, int corR, int corG, int corB, int posX, int posY) {
        PostIt pi = new PostIt(this);
        pi.setBackground(new Color(corR, corG, corB));
        pi.setBounds(new Rectangle(posX, posY, 70, 40));
        new EventoMouse().insEvento(this, pi, nome, "Post-It");
        this.add(pi, null);
        this.refresh();
	}
    
    private void refresh() {
        this.repaint();
    }    

	// Metodo Principal
	public static void main(String [] args) {
        new JKanban().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }	
}
package canvas;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class PostIt extends JPanel {

	private String nome1;
	private String nome2;
	private JKanban board;
	private JPopupMenu popMenu;

	public PostIt(JKanban bmc) {
	    // do Menu do Objeto
		popMenu = new JPopupMenu();
        JMenuItem itmDescricao = new JMenuItem("Descrição");
        JMenuItem itmCor = new JMenuItem("Cor de Fundo");
        JMenuItem itmExcluir = new JMenuItem("Excluir");
        popMenu.add(itmDescricao);
        popMenu.add(itmCor);
        popMenu.add(itmExcluir);
        // dos Eventos
        itmDescricao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                descObjeto();
            }
        });
        itmCor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                corObjeto();
            }
        });
        itmExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excObjeto();
            }
        });
        // --- 
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                clkMouse(evt);
            }
        });
        this.board = bmc;
        this.setSize(70, 40);
    }
	
	// Desenhar o objeto
	
    public void paint(Graphics g) {
        super.paint(g);
        g.drawLine(60, 0, 70, 10);
        g.drawRect(0, 0, 70, 40);
    	quebrarNome();
        g.drawString(this.nome1, 3, 18);
        g.drawString(this.nome2, 3, 32);
    }

	private void corObjeto() {
        Color color = this.getBackground();
        color = JColorChooser.showDialog(null, "Escolha a Cor da Tarefa", color);
        if (color != null)
            this.setBackground(color);
	}

	private void descObjeto() {
    	String nome = JOptionPane.showInputDialog(this, "Descrição (máx. 2 palavras significativas)", nome1+nome2);
    	if (nome != null) {
    		this.setName(nome);
    		this.repaint();
    	}
    }
    
    private void excObjeto() {
        board.remove(this);
        board.repaint();
    }
    
    // Metodos Auxiliares
    
    private void clkMouse(MouseEvent e) {
        if (e.isMetaDown()) {
            popMenu.show(this, e.getX(), e.getY());
        }
    }    
    
    private void quebrarNome() {
    	String nome = this.getName();
    	if (nome != null && nome.indexOf(' ') > -1) {
    	    nome1 = nome.substring(0, nome.indexOf(' '));
    	    nome2 = nome.substring(nome.indexOf(' ')+1);
    	} else {
    		nome1 = nome;
    		nome2 = "";
    	}
    }
}
package util;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import canvas.JKanban;
import canvas.PostIt;

/**
 * Realiza todo o tratamento com arquivos do Projeto
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class TratarArquivo {

	// Referencia
	private JKanban principal;

	/**
	 * Construtor inicial
	 * 
	 * @param principal
	 *            Janela Principal
	 */
	public TratarArquivo(JKanban principal) {
		this.principal = principal;
	}

	/**
	 * Abre o arquivo do Projeto
	 * 
	 * @return Logico informando o sucesso ou fracasso da operacao
	 */
	public boolean abrirArquivo() {
		boolean ret = false;
		FileDialog dig = new FileDialog(principal, "Abrir Arquivo",
				FileDialog.LOAD);
		dig.setDirectory("");
		dig.setFile("nome.kan");
		dig.setVisible(true);
		String nomArq = dig.getDirectory() + dig.getFile();
		try {
			if (new File(nomArq).exists()) {
				BufferedReader arqEntrada = new BufferedReader(new FileReader(
						nomArq));
				String linMnt = "";
				while ((linMnt = arqEntrada.readLine()) != null)
					separarPostIt(linMnt);
				arqEntrada.close();
			}
			ret = true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}

	/**
	 * Salvar o arquivo do Projeto
	 * 
	 * @return Logico informando o sucesso ou fracasso da operacao
	 */
	public boolean salvarArquivo() {
		boolean ret = false;
		FileDialog dig = new FileDialog(principal, "Salvar Arquivo",
				FileDialog.SAVE);
		dig.setDirectory("");
		dig.setFile("nome.kan");
		dig.setVisible(true);
		String nomArq = dig.getDirectory() + dig.getFile();
		try {
			PrintWriter arqSaida = new PrintWriter(new FileWriter(nomArq));
			TextArea saida = gerarSaida();
			arqSaida.print(saida.getText());
			arqSaida.close();
			ret = true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}

	/**
	 * Salvar o Projeto como imagem
	 * 
	 * @return Logico informando o sucesso ou fracasso da operacao
	 */
	public boolean salvarImagem(BufferedImage bi) {
		boolean ret = false;
		FileDialog dig = new FileDialog(principal, "Salvar Imagem",
				FileDialog.SAVE);
		dig.setDirectory("");
		dig.setFile("nome.png");
		try {
			dig.setVisible(true);
			String nomArq = dig.getDirectory() + dig.getFile();
			File f = new File(nomArq);
			ImageIO.write(bi, "png", f);
			ret = true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return ret;
	}

	private void separarPostIt(String lin) {
		StringTokenizer strTok = new StringTokenizer(lin, "|");
		if (strTok.countTokens() == 6) {
			String nome = strTok.nextToken();
			int corR = Integer.parseInt(strTok.nextToken());
			int corG = Integer.parseInt(strTok.nextToken());
			int corB = Integer.parseInt(strTok.nextToken());
			int posX = Integer.parseInt(strTok.nextToken());
			int posY = Integer.parseInt(strTok.nextToken());
			// Criar o objeto na Janela
			principal.criarPostIt(nome, corR, corG, corB, posX, posY);
		}
	}

	private TextArea gerarSaida() {
		TextArea ret = new TextArea();
		Component[] cmps = principal.getContentPane().getComponents();
		for (Component cmp : cmps) {
			if (cmp instanceof PostIt) {
				ret.append(cmp.getName() + '|' + cmp.getBackground().getRed()
						+ '|' + cmp.getBackground().getGreen() + '|'
						+ cmp.getBackground().getBlue() + '|' + cmp.getX()
						+ '|' + cmp.getY() + '\n');
			}
		}
		return ret;
	}
}
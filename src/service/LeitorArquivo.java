package service;

import java.util.ArrayList;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LeitorArquivo {
	private String texto = null;
	private int qtdLinhas = 0;
	private File arquivo = null;

	/**
	 * Construtor da classe LeitorArquivo.
	 * 
	 * @param origem � o caminho do arquivo.
	 * @throws Exception - Mesmo throw do Scanner.
	 */
	public LeitorArquivo(String origem) throws Exception {
		this.arquivo = new File(origem);
		StringBuilder textoBuilder = new StringBuilder();

		// Guarda a quantidade de linhas do arquivo.
		qtdLinhas = 0;
		try (Scanner scanner = new Scanner(arquivo, Charset.forName("UTF-8"))) {
			while (scanner.hasNext()) {
				qtdLinhas++;
				textoBuilder.append(scanner.nextLine());
				textoBuilder.append('\n');
			}
			// textoBuilder.append("EOF");
		}
		texto = textoBuilder.toString();
	}

	/**
	 * Retorna o texto do arquivo.
	 * 
	 * @return String - Texto do arquivo lido.
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Retorna a quantidade de linhas do arquivo.
	 * 
	 * @return int - Quantidade de linhas do arquivo.
	 */
	public int getQtdLinhas() {
		return qtdLinhas;
	}

	/**
	 * // * Converte o texto para um HashMap, j� validando a gram�tica.
	 */
	// Chave->S�mbolo n�o terminal, Valor -> Array de S�mbolos N�o terminais,
	// terminais ou terminais e n�o terminais.
	public HashMap<String, List<String>> toHashMap() throws Exception {
		HashMap<String, List<String>> regras = new HashMap<String, List<String>>(this.qtdLinhas);// HashMap<esquerda,
																									// direita> | Uma
																									// chave s� pode ter
																									// um valor, por
																									// isso a Lista de
																									// Strings.

		// Vamos tentar criar um scanner para o texto.
		try (Scanner scanner = new Scanner(texto)) {
			int contLinha = 1; // Conta a linha.

			// Enquanto houver linhas no arquivo.
			while (scanner.hasNextLine()) {

				String linha = scanner.nextLine(); // Armazena uma linha dentro da vari�vel linha. Cada linha � uma
													// !regra de produ��o!.

				// Se a linha cont�m a seta � uma regra de produ��o.
				if (linha.contains("->")) {

					linha = linha.replaceAll("\\s+", "");// Como j� foi checada a exist�ncia da seta, pode tirar todos
															// os espa�os.

					String[] esquerdaDireita = linha.split("->");// Vamos separar a regra de produ��o em duas partes,
																	// esquerda e direita.

					// Se houver mais de 2 elementos no array, � porque a linha possui mais de uma
					// seta.
					if (esquerdaDireita.length > 2)
						throw new Exception(
								"Gram�tica Inv�lida, mais de um simbolo -> detectado; linha = " + contLinha);

					// Checamos se a esquerda possui somente !um s�mbolo! e que ele !seja n�o
					// terminal(ou seja, mai�sculo)!.
					// Olha que interessante, � s� o caractere � igual a ele mesmo em min�sculo,
					// ent�o ele � min�sculo.
					if (esquerdaDireita[0].equals(esquerdaDireita[0].toLowerCase()))
						throw new Exception(
								"Gram�tica Inv�lida, s�mbolos min�sculos na esquerda; linha = " + contLinha);
					if (esquerdaDireita[0].length() > 1)
						throw new Exception(
								"Gram�tica Inv�lida, mais de um s�mbolo detectado na esquerda; linha = " + contLinha);

					// H� tr�s casos para a direita: Ou � um terminal, ou � um n�o terminal, ou � um
					// n�o terminal seguido de um terminal.
					switch (esquerdaDireita[1].length()) {
						case 1: // Um caractere, pode ser um s�mbolo terminal ou n�o terminal
							break;
						case 2: // Se tiver dois caracteres, deve ser um n�o terminal seguido de um terminal.
	
							if (Character.isDigit(esquerdaDireita[1].charAt(0))) { // S�mbolo terminal � um digito?
								if (Character.isDigit(esquerdaDireita[1].charAt(1))) { // Segundo S�mbolo � um digito?
									throw new Exception(
											"Gram�tica Inv�lida, dois s�mbolos terminais na direita; linha = " + contLinha);
								}
							}
	
							// Os dois s�mbolos s�o terminais?
							else if (esquerdaDireita[1].equals(esquerdaDireita[1].toLowerCase())) { // Se s� tiver s�mbolos
																									// min�sculos(s�mbolos
																									// terminais)
								throw new Exception(
										"Gram�tica Inv�lida, dois s�mbolos terminais na direita; linha = " + contLinha);
							}
							
							// Os dois s�mbolos s�o n�o terminais?
							else if (esquerdaDireita[1].equals(esquerdaDireita[1].toUpperCase())) { // Se s� tiver s�mbolos
																									// mai�sculos(s�mbolos
																									// n�o-terminais)
								throw new Exception(
										"Gram�tica Inv�lida, dois s�mbolos n�o-terminais na direita; linha = " + contLinha);
							}
							break;
	
						default:
							throw new Exception("Gram�tica Inv�lida, n�mero inv�lido de s�mbolos na direita:" + esquerdaDireita[1].length() + "; linha = " + contLinha);
					}

					// Agora que j� validamos a regra de produ��o, vamos adicionar ela no HashMap.
					// A key ser� o s�mbolo n�o terminal da esquerda, enquanto o value ser� a parte direita da regra.
					
					// Se a chave j� existir, adicione a parte direita na lista de strings relacionada a chave.
					if (regras.containsKey(esquerdaDireita[0]))
						regras.get(esquerdaDireita[0]).add(esquerdaDireita[1]); 
					
					// Se a chave n�o existir, crie uma nova lista, adicione a parte direita nela e adicione a lista como valor da chave.
					else {
						List<String> listaDaDir = new ArrayList<String>();
						listaDaDir.add(esquerdaDireita[1]); // Cria uma lista s� com os valores da esquerda.
						regras.put(esquerdaDireita[0], listaDaDir);
					}
					
				
				} 
				
				// Else referente ao if que checa se a linha cont�m a seta.
				else {
					throw new Exception("Gram�tica Inv�lida, n�o foi encontrada o Simbolo -> em nenhuma linha; linha = "
							+ contLinha);
				}
				// Incrementa o contador de linhas.
				contLinha++;
			}
		}

		return regras;
	}

}

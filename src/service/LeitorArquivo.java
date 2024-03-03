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
	 * @param origem é o caminho do arquivo.
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
	 * // * Converte o texto para um HashMap, já validando a gramática.
	 */
	// Chave->Símbolo não terminal, Valor -> Array de Símbolos Não terminais,
	// terminais ou terminais e não terminais.
	public HashMap<String, List<String>> toHashMap() throws Exception {
		HashMap<String, List<String>> regras = new HashMap<String, List<String>>(this.qtdLinhas);// HashMap<esquerda,
																									// direita> | Uma
																									// chave só pode ter
																									// um valor, por
																									// isso a Lista de
																									// Strings.

		// Vamos tentar criar um scanner para o texto.
		try (Scanner scanner = new Scanner(texto)) {
			int contLinha = 1; // Conta a linha.

			// Enquanto houver linhas no arquivo.
			while (scanner.hasNextLine()) {

				String linha = scanner.nextLine(); // Armazena uma linha dentro da variável linha. Cada linha é uma
													// !regra de produção!.

				// Se a linha contém a seta é uma regra de produção.
				if (linha.contains("->")) {

					linha = linha.replaceAll("\\s+", "");// Como já foi checada a existência da seta, pode tirar todos
															// os espaços.

					String[] esquerdaDireita = linha.split("->");// Vamos separar a regra de produção em duas partes,
																	// esquerda e direita.

					// Se houver mais de 2 elementos no array, é porque a linha possui mais de uma
					// seta.
					if (esquerdaDireita.length > 2)
						throw new Exception(
								"Gramática Inválida, mais de um simbolo -> detectado; linha = " + contLinha);

					// Checamos se a esquerda possui somente !um símbolo! e que ele !seja não
					// terminal(ou seja, maiúsculo)!.
					// Olha que interessante, é só o caractere é igual a ele mesmo em minúsculo,
					// então ele é minúsculo.
					if (esquerdaDireita[0].equals(esquerdaDireita[0].toLowerCase()))
						throw new Exception(
								"Gramática Inválida, símbolos minúsculos na esquerda; linha = " + contLinha);
					if (esquerdaDireita[0].length() > 1)
						throw new Exception(
								"Gramática Inválida, mais de um símbolo detectado na esquerda; linha = " + contLinha);

					// Há três casos para a direita: Ou é um terminal, ou é um não terminal, ou é um
					// não terminal seguido de um terminal.
					switch (esquerdaDireita[1].length()) {
						case 1: // Um caractere, pode ser um símbolo terminal ou não terminal
							break;
						case 2: // Se tiver dois caracteres, deve ser um não terminal seguido de um terminal.
	
							if (Character.isDigit(esquerdaDireita[1].charAt(0))) { // Símbolo terminal é um digito?
								if (Character.isDigit(esquerdaDireita[1].charAt(1))) { // Segundo Símbolo é um digito?
									throw new Exception(
											"Gramática Inválida, dois símbolos terminais na direita; linha = " + contLinha);
								}
							}
	
							// Os dois símbolos são terminais?
							else if (esquerdaDireita[1].equals(esquerdaDireita[1].toLowerCase())) { // Se só tiver símbolos
																									// minúsculos(símbolos
																									// terminais)
								throw new Exception(
										"Gramática Inválida, dois símbolos terminais na direita; linha = " + contLinha);
							}
							
							// Os dois símbolos são não terminais?
							else if (esquerdaDireita[1].equals(esquerdaDireita[1].toUpperCase())) { // Se só tiver símbolos
																									// maiúsculos(símbolos
																									// não-terminais)
								throw new Exception(
										"Gramática Inválida, dois símbolos não-terminais na direita; linha = " + contLinha);
							}
							break;
	
						default:
							throw new Exception("Gramática Inválida, número inválido de símbolos na direita:" + esquerdaDireita[1].length() + "; linha = " + contLinha);
					}

					// Agora que já validamos a regra de produção, vamos adicionar ela no HashMap.
					// A key será o símbolo não terminal da esquerda, enquanto o value será a parte direita da regra.
					
					// Se a chave já existir, adicione a parte direita na lista de strings relacionada a chave.
					if (regras.containsKey(esquerdaDireita[0]))
						regras.get(esquerdaDireita[0]).add(esquerdaDireita[1]); 
					
					// Se a chave não existir, crie uma nova lista, adicione a parte direita nela e adicione a lista como valor da chave.
					else {
						List<String> listaDaDir = new ArrayList<String>();
						listaDaDir.add(esquerdaDireita[1]); // Cria uma lista só com os valores da esquerda.
						regras.put(esquerdaDireita[0], listaDaDir);
					}
					
				
				} 
				
				// Else referente ao if que checa se a linha contém a seta.
				else {
					throw new Exception("Gramática Inválida, não foi encontrada o Simbolo -> em nenhuma linha; linha = "
							+ contLinha);
				}
				// Incrementa o contador de linhas.
				contLinha++;
			}
		}

		return regras;
	}

}

package service;

import java.util.ArrayList;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LeitorArquivo {
	private String texto     = null;
	private int    qtdLinhas = 0;
	private File   arquivo   = null;

	public LeitorArquivo(String caminhoArquivo) throws Exception{
		this.arquivo = new File(caminhoArquivo);
		StringBuilder textoBuilder = new StringBuilder();
		
		int cont = 0;
		
		try (Scanner scanner = new Scanner(arquivo, Charset.forName("UTF-8"))) {
			while(scanner.hasNext()) {
				cont++;
				textoBuilder.append(scanner.nextLine());
				textoBuilder.append('\n');
			}
			//textoBuilder.append("EOF");
			qtdLinhas = cont;
		}
		texto = textoBuilder.toString();
	}

	public String getTexto() {
		return texto;
	}

	public int getQtdLinhas() {
		return qtdLinhas;
	}
	//Chave->Símbolo não terminal, Valor -> Array de Símbolos Não terminais, terminais ou terminais e não terminais. 
	public HashMap<String, List<String>> toHashMap() throws Exception{
		HashMap<String, List<String>> regras = new HashMap<String, List<String>>(this.qtdLinhas);//HashMap<esquerda, direita> | Uma chave só pode ter um valor, por isso a Lista de Strings.
		try (Scanner scanner = new Scanner(texto)){
			int contLinha = 1; //Conta a linha.
			while(scanner.hasNextLine()) {
			//while(true) {	
				String linha = scanner.nextLine(); //Armazena uma linha dentro de linha;
				if (linha.contains("->")) {
					linha = linha.replaceAll("\\s+", "");//Remove todos os espaços em branco.
					String[] esquerdaDireita = linha.split("->");//Valor da esquerda e da direita
					
					
					if(esquerdaDireita.length > 2)
						throw new Exception("Gramática Inválida, mais de um simbolo -> detectado; linha = "+contLinha);
					
					if(esquerdaDireita[0].equals(esquerdaDireita[0].toLowerCase())) //Se a esquerda for igual a (esquerda tudo minúsculo(símbolos terminais)).
						throw new Exception("Gramática Inválida, símbolos minúsculos na esquerda; linha = "+contLinha);
					if(esquerdaDireita[0].length() > 1)
						throw new Exception("Gramática Inválida, mais de um símbolo detectado na esquerda; linha = "+contLinha);
					
							
					switch (esquerdaDireita[1].length()) { //Switch case com o número de caracteres na direita.
					case 1: //Um caractere, pode ser um simbolo terminal ou não terminal
						break;
					case 2: //Se tiver dois caracteres
						if(Character.isDigit(esquerdaDireita[1].charAt(0))) { //Símbolo terminal é um digito?
							if(Character.isDigit(esquerdaDireita[1].charAt(1))) { //Segundo Símbolo é um digito?
								throw new Exception("Gramática Inválida, dois símbolos terminais na direita; linha = "+contLinha);
							}
						}
						else if(esquerdaDireita[1].equals(esquerdaDireita[1].toLowerCase())) { //Se só tiver símbolos minúsculos(símbolos terminais)
							throw new Exception("Gramática Inválida, dois símbolos terminais na direita; linha = "+contLinha);
						}
						else if(esquerdaDireita[1].equals(esquerdaDireita[1].toUpperCase())) { //Se só tiver símbolos maiúsculos(símbolos não-terminais)
							throw new Exception("Gramática Inválida, dois símbolos não-terminais na direita; linha = "+contLinha);
						}
						break;
					default:
						throw new Exception("Gramática Inválida, 0 ou n>2 símbolos encontrados na direita"+contLinha);
					}
					
					if(regras.containsKey(esquerdaDireita[0]))//Se já existir uma chave
						regras.get(esquerdaDireita[0]).add(esquerdaDireita[1]); //Adicione uma nova string na lista de strings relacionada chave;
					else {
						List<String> listaDaDir = new ArrayList<String>();
						listaDaDir.add(esquerdaDireita[1]); //Cria uma lista só com os valores da esquerda.
						regras.put(esquerdaDireita[0], listaDaDir);
					}	
				}
				else {
					throw new Exception("Gramática Inválida, não foi encontrada o Simbolo -> em nenhuma linha; linha = "+contLinha);
				}
				//if(!scanner.hasNextLine())
				//	break;
				contLinha++;
			}
		}
		
		return regras;
	}
	

}

package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Classe que representa uma gramática linear a direita.
 * 
 */
public class Gramatica {
	
	// HashMap que representa as regras de produção da gramática.
	private HashMap<String, List<String>> regrasDeProducao = null;
	
	// Ao verificar se uma palavra pode ser gerada pela gramática, as regras usadas são armazenadas nessa lista.
	// É importante lembrar de limpar essa lista toda vez que for verificar uma nova palavra.
	static ArrayList<String> regrasUsadas = new ArrayList<String>();
	
	/**
	 * Construtor da classe.
	 * 
	 * @param regrasDeProducao é um HashMap que contém as regras de produção da
	 *                         gramática.
	 */
	public Gramatica(HashMap<String, List<String>> regrasDeProducao) {
		this.regrasDeProducao = regrasDeProducao;
	}
	
	
	/**
	 * Devolve as regras em forma de String.
	 */
	public String regrasToString() {
		
		// Cria um StringBuilder para concatenar as regras.
		StringBuilder textoBuilder = new StringBuilder();
		
		// Para cada chave no HashMap, ou seja, para cada !símbolo não terminal da esquerda!
		for (String esquerda : regrasDeProducao.keySet()) { //keySet() nesse caso retorna uma lista de strings(chaves)
			for (String direita: regrasDeProducao.get(esquerda)) {
				textoBuilder.append(esquerda);
				textoBuilder.append("->");
				textoBuilder.append(direita);
				textoBuilder.append('\n');
			}
		}
		
		return textoBuilder.toString();
	}
	
	/**
	 * Verifica se uma palavra pode ser gerada pela gramática. O ideal seria refatorar o método para que ele use pilhas de caracteres. Também seria interessante
	 * transformar o método para iterativa, faze-lo retornar a lista de regras usadas e lançar uma exceção caso a palavra não seja gerada.
	 * @param palavra é a palavra a ser verificada.
	 * @param pilha é uma pilha que começa com o símbolo inicial da gramática.
	 * @return true se a palavra pode ser gerada pela gramática, false caso
	 *         contrário.
	 */
	
	// A pilha sempre começa com o não terminal inicial da gramática. O trabalho da função é fazer com que a pilha fique vazia, ou seja, que todos
	// os tokens sejam consumidos.
	public boolean verificarPalavra(String palavra, String pilha) {
		// Caso base: Se a palavra e a pilha estiverem vazias, então a palavra foi formada.
		if (palavra.isEmpty() && pilha.isEmpty()) {
			return true;
		}
		// Caso base: Caso a palavra esteja vazia e a pilha tenha conteúdo, então as regras não foram selecionadas corretamente.
		if (palavra.isEmpty() && !pilha.isEmpty()) {
			return false;
		}
		// Caso base: Se a pilha estiver vazia mas a palavra ainda não tiver sido formada(palavra não está vazia),
		// então não há mais regras suficientes para gerarem a palavra.
		if (pilha.isEmpty()) {
			return false;
		}

		// Se deixa essas inicializações em cima, vai eventualmente ocorrer NullPointerException
		char tokenAtual = palavra.charAt(0); 
		char topoPilha = pilha.charAt(0);
		String restoPalavra = palavra.substring(1);

		// Caso Recursivo: Se o topo da pilha é terminal(não pode gerar palavra) e a letra atual for igual a esse terminal atual.
		// Então a regra atual forma a letra atual.
		if (isTerminal(pilha.toCharArray()[0]) && tokenAtual == topoPilha) { 
			// Desempilha e consome a letra atual.
			return verificarPalavra(restoPalavra, pilha.substring(1));
		} 
		
		// Se o elemento atual da pilha é não-terminal, procure que regras esse não terminal gera. (Pode ser um terminal que não seja a letra atual também). 
		// Em outras palavras: Caso não tenha eliminado a letra, procura uma regra que elimine.
		else { 
			String naoTerminal = Character.toString(topoPilha);
			
			// Recupera todas as regras de produção para esse símbolo no lado esquerdo.
			List<String> regras = regrasDeProducao.get(naoTerminal);
			
			// Caso base: Se esse não terminal(ou um terminal que não seja a letra atual) não gera regras, então a palavra não existe.
			if (regras == null) { 
				return false;
			}
			
			for (String regra : regras) { 
				// Só retorna se chegar no final, se não tivesse esse IF, o código não ia percorrer por todas as regras.
				// Caso recursivo: Substitui o não terminal no topo da pilha pela regra de produção.
				if (verificarPalavra(tokenAtual + restoPalavra, regra + pilha.substring(1))) { 
					// Adiciona a regra usada na lista de regras usadas.
					regrasUsadas.add(naoTerminal + " -> " + regra);
					
					// Caso base: a palavra pode ser gerada por uma das regras de produção para esse símbolo não terminal
					return true; 
				}
			}
			return false; // Caso base: Se nenhuma regra gerar a palavra, retorne falso.
		}
	}
	
	
	/**
	 * Verifica se um símbolo é terminal ou não terminal.
	 * 
	 * @param c é o símbolo a ser verificado.
	 * @return true se o símbolo é terminal, false caso não terminal.
	 */
	private boolean isTerminal(Character c) {
		// É um terminal caso seja um dígito ou uma letra minúscula.
		if(Character.isDigit(c) || Character.isLowerCase(c)) { //Se for um dígito ou uma letra minúscula, então é um símbolo terminal
			return true;
		}
		// É um não terminal caso seja uma letra maiúscula.

		return false;

	}

	/**
	 * Retorna as regras de produção usadas para gerar a palavra.
	 * 
	 * @return lista de regras de produção usadas.
	 */
	public static List<String> getRegrasUsadas() {
		Collections.reverse(regrasUsadas);
		return regrasUsadas;
	}
	
	/**
	 * Limpa a lista de regras usadas.
	 */
	public static void clearRegrasUsadas() {
		regrasUsadas.clear();
	}
	

}

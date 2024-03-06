package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Classe que representa uma gram�tica linear a direita.
 * 
 */
public class Gramatica {
	
	// HashMap que representa as regras de produ��o da gram�tica.
	private HashMap<String, List<String>> regrasDeProducao = null;
	
	// Ao verificar se uma palavra pode ser gerada pela gram�tica, as regras usadas s�o armazenadas nessa lista.
	// � importante lembrar de limpar essa lista toda vez que for verificar uma nova palavra.
	static ArrayList<String> regrasUsadas = new ArrayList<String>();
	
	/**
	 * Construtor da classe.
	 * 
	 * @param regrasDeProducao � um HashMap que cont�m as regras de produ��o da
	 *                         gram�tica.
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
		
		// Para cada chave no HashMap, ou seja, para cada !s�mbolo n�o terminal da esquerda!
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
	 * Verifica se uma palavra pode ser gerada pela gram�tica. O ideal seria refatorar o m�todo para que ele use pilhas de caracteres. Tamb�m seria interessante
	 * transformar o m�todo para iterativa, faze-lo retornar a lista de regras usadas e lan�ar uma exce��o caso a palavra n�o seja gerada.
	 * @param palavra � a palavra a ser verificada.
	 * @param pilha � uma pilha que come�a com o s�mbolo inicial da gram�tica.
	 * @return true se a palavra pode ser gerada pela gram�tica, false caso
	 *         contr�rio.
	 */
	
	// A pilha sempre come�a com o n�o terminal inicial da gram�tica. O trabalho da fun��o � fazer com que a pilha fique vazia, ou seja, que todos
	// os tokens sejam consumidos.
	public boolean verificarPalavra(String palavra, String pilha) {
		// Caso base: Se a palavra e a pilha estiverem vazias, ent�o a palavra foi formada.
		if (palavra.isEmpty() && pilha.isEmpty()) {
			return true;
		}
		// Caso base: Caso a palavra esteja vazia e a pilha tenha conte�do, ent�o as regras n�o foram selecionadas corretamente.
		if (palavra.isEmpty() && !pilha.isEmpty()) {
			return false;
		}
		// Caso base: Se a pilha estiver vazia mas a palavra ainda n�o tiver sido formada(palavra n�o est� vazia),
		// ent�o n�o h� mais regras suficientes para gerarem a palavra.
		if (pilha.isEmpty()) {
			return false;
		}

		// Se deixa essas inicializa��es em cima, vai eventualmente ocorrer NullPointerException
		char tokenAtual = palavra.charAt(0); 
		char topoPilha = pilha.charAt(0);
		String restoPalavra = palavra.substring(1);

		// Caso Recursivo: Se o topo da pilha � terminal(n�o pode gerar palavra) e a letra atual for igual a esse terminal atual.
		// Ent�o a regra atual forma a letra atual.
		if (isTerminal(pilha.toCharArray()[0]) && tokenAtual == topoPilha) { 
			// Desempilha e consome a letra atual.
			return verificarPalavra(restoPalavra, pilha.substring(1));
		} 
		
		// Se o elemento atual da pilha � n�o-terminal, procure que regras esse n�o terminal gera. (Pode ser um terminal que n�o seja a letra atual tamb�m). 
		// Em outras palavras: Caso n�o tenha eliminado a letra, procura uma regra que elimine.
		else { 
			String naoTerminal = Character.toString(topoPilha);
			
			// Recupera todas as regras de produ��o para esse s�mbolo no lado esquerdo.
			List<String> regras = regrasDeProducao.get(naoTerminal);
			
			// Caso base: Se esse n�o terminal(ou um terminal que n�o seja a letra atual) n�o gera regras, ent�o a palavra n�o existe.
			if (regras == null) { 
				return false;
			}
			
			for (String regra : regras) { 
				// S� retorna se chegar no final, se n�o tivesse esse IF, o c�digo n�o ia percorrer por todas as regras.
				// Caso recursivo: Substitui o n�o terminal no topo da pilha pela regra de produ��o.
				if (verificarPalavra(tokenAtual + restoPalavra, regra + pilha.substring(1))) { 
					// Adiciona a regra usada na lista de regras usadas.
					regrasUsadas.add(naoTerminal + " -> " + regra);
					
					// Caso base: a palavra pode ser gerada por uma das regras de produ��o para esse s�mbolo n�o terminal
					return true; 
				}
			}
			return false; // Caso base: Se nenhuma regra gerar a palavra, retorne falso.
		}
	}
	
	
	/**
	 * Verifica se um s�mbolo � terminal ou n�o terminal.
	 * 
	 * @param c � o s�mbolo a ser verificado.
	 * @return true se o s�mbolo � terminal, false caso n�o terminal.
	 */
	private boolean isTerminal(Character c) {
		// � um terminal caso seja um d�gito ou uma letra min�scula.
		if(Character.isDigit(c) || Character.isLowerCase(c)) { //Se for um d�gito ou uma letra min�scula, ent�o � um s�mbolo terminal
			return true;
		}
		// � um n�o terminal caso seja uma letra mai�scula.

		return false;

	}

	/**
	 * Retorna as regras de produ��o usadas para gerar a palavra.
	 * 
	 * @return lista de regras de produ��o usadas.
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

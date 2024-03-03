package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Gramatica {
	private HashMap<String, List<String>> regrasDeProducao = null;
	static ArrayList<String> regrasUsadas = new ArrayList<String>();
	
	public Gramatica(HashMap<String, List<String>> regrasDeProducao) {
		this.regrasDeProducao = regrasDeProducao;
	}
	
	public String regrasToString() {
		StringBuilder textoBuilder = new StringBuilder();
		
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
	
		
	public boolean verificarPalavra(String palavra, String pilha) {
		if (palavra.isEmpty() && pilha.isEmpty()) { //Caso base: Se a palavra e a pilha estiver vazia, ent�o a palavra foi formada.
			return true;
		}
		if (palavra.isEmpty() && !pilha.isEmpty()) { //Caso a palavra esteja vazia e a pilha tenha conte�do, ent�o as regras n�o foram selecionadas certas
			return false;
		}
		if (pilha.isEmpty()) { // Caso base: Se a pilha estiver vazia mas a palavra ainda n�o tiver sido formada(palavra n�o esta vazia), ent�o n�o h� mais regras suficentes para gerarem a palavra.
			return false;
		}
		
		char letraAtual = palavra.charAt(0); //Se deixa essas inicializa��es em cima, vai eventualmente ocorrer NullPointerException
		String restoPalavra = palavra.substring(1);
		
		if (isTerminal(pilha.toCharArray()[0]) && letraAtual == pilha.charAt(0)) { // Caso Recursiva: Se o elemento atual da pilha � terminal(n�o pode gerar palavra) e a letra atual for igual a esse terminal atual. Ent�o a regra atual forma a letra atual
			return  verificarPalavra(restoPalavra, pilha.substring(1));			   // Desempilha o n�o terminal que formou a letra, e risca a letra da palavra formada.
		} else { // Caso recursivo:Se o elemento atual da pilha � n�o-terminal, procure que regras esse n�o terminal gera. (Pode ser um terminal que n�o seja a letra atual tambem)/em outras palavras:Caso n�o tenha eliminado a letra, procura uma regra que elimine.
			String naoTerminal = Character.toString(pilha.charAt(0));
			List<String> regras = regrasDeProducao.get(naoTerminal);
			if (regras == null) { // Caso base: Se esse n�o terminal(ou um terminal que n�o seja a letra atual) n�o gera regras, ent�o a palavra n�o existe.
				return false;
			}
			for (String regra : regras) { //Desempilha um elemento. Empilha a direita da regra na pilha e testa se ela funciona.
				if (verificarPalavra(letraAtual + restoPalavra, regra + pilha.substring(1))) { //S� retorna se chegar no final, se n�o tivesse esse IF, o c�digo n�o ia percorrer por todas as regras.
					regrasUsadas.add(naoTerminal + " -> " + regra);
					return true; // Caso base: a palavra pode ser gerada por uma das regras de produ��o para esse s�mbolo n�o terminal
				}
			}
			return false; // Caso base: Se nenhuma regra gerar a palavra, retorne falso.
		}
	}
	
	
	private boolean isTerminal(Character c) {
		if(Character.isDigit(c) || Character.isLowerCase(c)) { //Se for um d�gito ou uma letra min�scula, ent�o � um s�mbolo terminal
			return true;
		}

		return false;

	}

	public static ArrayList<String> getRegrasUsadas() {
		Collections.reverse(regrasUsadas);
		return regrasUsadas;
	}
	public static void clearRegrasUsadas() {
		regrasUsadas.clear();
	}
	

}

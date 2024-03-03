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
		if (palavra.isEmpty() && pilha.isEmpty()) { //Caso base: Se a palavra e a pilha estiver vazia, então a palavra foi formada.
			return true;
		}
		if (palavra.isEmpty() && !pilha.isEmpty()) { //Caso a palavra esteja vazia e a pilha tenha conteúdo, então as regras não foram selecionadas certas
			return false;
		}
		if (pilha.isEmpty()) { // Caso base: Se a pilha estiver vazia mas a palavra ainda não tiver sido formada(palavra não esta vazia), então não há mais regras suficentes para gerarem a palavra.
			return false;
		}
		
		char letraAtual = palavra.charAt(0); //Se deixa essas inicializações em cima, vai eventualmente ocorrer NullPointerException
		String restoPalavra = palavra.substring(1);
		
		if (isTerminal(pilha.toCharArray()[0]) && letraAtual == pilha.charAt(0)) { // Caso Recursiva: Se o elemento atual da pilha é terminal(não pode gerar palavra) e a letra atual for igual a esse terminal atual. Então a regra atual forma a letra atual
			return  verificarPalavra(restoPalavra, pilha.substring(1));			   // Desempilha o não terminal que formou a letra, e risca a letra da palavra formada.
		} else { // Caso recursivo:Se o elemento atual da pilha é não-terminal, procure que regras esse não terminal gera. (Pode ser um terminal que não seja a letra atual tambem)/em outras palavras:Caso não tenha eliminado a letra, procura uma regra que elimine.
			String naoTerminal = Character.toString(pilha.charAt(0));
			List<String> regras = regrasDeProducao.get(naoTerminal);
			if (regras == null) { // Caso base: Se esse não terminal(ou um terminal que não seja a letra atual) não gera regras, então a palavra não existe.
				return false;
			}
			for (String regra : regras) { //Desempilha um elemento. Empilha a direita da regra na pilha e testa se ela funciona.
				if (verificarPalavra(letraAtual + restoPalavra, regra + pilha.substring(1))) { //Só retorna se chegar no final, se não tivesse esse IF, o código não ia percorrer por todas as regras.
					regrasUsadas.add(naoTerminal + " -> " + regra);
					return true; // Caso base: a palavra pode ser gerada por uma das regras de produção para esse símbolo não terminal
				}
			}
			return false; // Caso base: Se nenhuma regra gerar a palavra, retorne falso.
		}
	}
	
	
	private boolean isTerminal(Character c) {
		if(Character.isDigit(c) || Character.isLowerCase(c)) { //Se for um dígito ou uma letra minúscula, então é um símbolo terminal
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

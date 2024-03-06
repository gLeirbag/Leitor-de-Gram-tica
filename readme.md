# Leitor De Gramática

## Tarefa

1. Desenvolva um programa que leia um arquivo texto contendo uma gramática linear à direita.
2. Leia uma palavra via ambiente gráfico.
3. Verifique se a palavra pertence à linguagem gerada pela gramática.
4. O sistema deve mostrar a sequência  de produções que geram a palavra.


- Os símbolos não-terminais devem estar em maiúsculas, enquanto os terminais devem estar em minúsculas.
- Pode haver espaços em branco dentro das produções. Mas não pode haver espaços em branco entre "->".
- O arquivo não pode conter produções repetidas.
- Toda produção deve ter a forma geral: "N -> tN". Onde no lado esquerdo está apenas um não-terminal, e no lado direito está um terminal seguido de um não-terminal (ou apenas um terminal).
- O arquivo pode conter linhas em branco.
- Havendo algum erro no arquivo, o sistema deve mostrar uma mensagem de erro.

### Exemplo de Arquivo Correto

```plaintext
S -> 0S
S    ->       1S


S ->   0
```

## Minha Solução

![imagem](/example/screenshot.png)

### Arquitetura

A minha solução considera que o não-terminal inicial é sempre "S".

A arquitetura foi dividida em 2 camadas: Uma visual e outra lógica.
A camada lógica conta com uma classe para ler o arquivo e outra para verificar a palavra.

Cada função está documentada no código fonte.
Na pasta * examples * há um exemplo de gramática correta, tente verificar a palavra "batata". Há também exemplos de arquivos errados.
Os arquivos de gramática são do formato **.txt**.



# Leitor De Gram�tica

## Tarefa

1. Desenvolva um programa que leia um arquivo texto contendo uma gram�tica linear � direita.
2. Leia uma palavra via ambiente gr�fico.
3. Verifique se a palavra pertence � linguagem gerada pela gram�tica.
4. O sistema deve mostrar a sequ�ncia  de produ��es que geram a palavra.


- Os s�mbolos n�o-terminais devem estar em mai�sculas, enquanto os terminais devem estar em min�sculas.
- Pode haver espa�os em branco dentro das produ��es. Mas n�o pode haver espa�os em branco entre "->".
- O arquivo n�o pode conter produ��es repetidas.
- Toda produ��o deve ter a forma geral: "N -> tN". Onde no lado esquerdo est� apenas um n�o-terminal, e no lado direito est� um terminal seguido de um n�o-terminal (ou apenas um terminal).
- O arquivo pode conter linhas em branco.
- Havendo algum erro no arquivo, o sistema deve mostrar uma mensagem de erro.

### Exemplo de Arquivo Correto

```plaintext
S -> 0S
S    ->       1S


S ->   0
```

## Minha Solu��o

### Arquitetura

A arquitetura foi dividida em 2 camadas: Uma visual e outra l�gica.
A camada l�gica conta com uma classe para ler o arquivo e outra para verificar a palavra.



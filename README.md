# Compilador de MiniJava

## Equipe:
- Daniel Rebouças de Queiroz - 421751 
- Eduardo Alcantara de Alencar Pinto - 428945
- Pedro Ernesto de Oliveira Primo - 418465

## Analisador Léxico e Sintático
### Instruções:

```sh
$ javacc analisador_lexico/analisador_lexico.jj
$ javac MiniJavaParser.java
$ java MiniJavaParser analisador_lexico/ProgramsTests/Program.java
$ // Program.java ∈ {BinarySearch.java, BinaryTree.java, BubbleSort.java, Factorial.java, LinearSearch.java, LinkedList.java, QuickSort.java, TreeVisitor.java} 
```

## Analisador Semântico
### Instruções:

```sh
$ javacc analisador_lexico/analisador_lexico.jj
$ javac Main.java
$ java Main analisador_lexico/ProgramsTests/Program.java
$ // Program.java ∈ {BinarySearch.java, BinaryTree.java, BubbleSort.java, Factorial.java, LinearSearch.java, LinkedList.java, QuickSort.java, TreeVisitor.java} 
```

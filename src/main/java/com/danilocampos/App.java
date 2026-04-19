package com.danilocampos;

import static com.danilocampos.model.DifficultyEnum.ADVANCED;
import static com.danilocampos.model.DifficultyEnum.BEGINNER;
import static com.danilocampos.model.DifficultyEnum.INTERMEDIATE;
import static com.danilocampos.model.DifficultyEnum.MASTER;
import static java.util.Objects.isNull;

import java.util.Scanner;

import com.danilocampos.model.Board;
import com.danilocampos.model.DifficultyEnum;

public class App {
  private final static Scanner scanner = new Scanner(System.in);

  private static Board board;

  public static void main(String[] args) {
    var option = -1;

    while (true) {
      System.out.println("\nSelecione uma das opções a seguir\n");
      System.out.println("1 - Iniciar um novo Jogo");
      System.out.println("2 - Colocar um número");
      System.out.println("3 - Remover um número");
      System.out.println("4 - Visualizar jogo atual");
      System.out.println("5 - Verificar status do jogo");
      System.out.println("6 - limpar todos os campos preenchidos");
      System.out.println("7 - Sair\n");

      option = scanner.nextInt();

      switch (option) {
        case 1 -> startGame();
        case 2 -> inputNumber();
        case 3 -> removeNumber();
        case 4 -> showCurrentGame();
        case 5 -> showGameStatus();
        case 6 -> clearGame();
        case 7 -> System.exit(0);
        default -> System.out.println("\nOpção inválida, selecione uma das opções do menu");
      }
    }
  }

  private static void clearGame() {
    if (isNull(board)) {
      System.out.println("\nO jogo ainda não foi iniciado");
      return;
    }

    board.clearUnfixeds();
    showCurrentGame();

  }

  private static void startGame() {
    DifficultyEnum difficulty;
    String question = """

        Selecione o nível de dificuldade

        1 - Iniciante
        2 - Intermediário
        3 - Avançado
        4 - Mestre

        """;
    int option = askNumber(question, 1, 4);

    difficulty = switch (option) {
      case 2 -> INTERMEDIATE;
      case 3 -> ADVANCED;
      case 4 -> MASTER;
      default -> BEGINNER;
    };

    board = new Board(difficulty);

    System.out.println("\nO jogo está pronto para começar!");
    showCurrentGame();
  }

  private static void showCurrentGame() {
    if (isNull(board)) {
      System.out.println("\nO jogo ainda não foi iniciado");
      return;
    }
    System.out.println(board);
  }

  private static int askNumber(final String text, final int min, final int max) {
    int number;
    do {
      System.out.println(text);
      number = scanner.nextInt();
    } while (number < min || number > max);
    return number;
  }

  private static void inputNumber() {
    if (isNull(board)) {
      System.out.println("\nO jogo ainda não foi iniciado");
      return;
    }

    var row = askNumber("\nInforma a linha em que o número será inserido (0-8)", 0, 8);
    var col = askNumber("\nInforme a coluna em que o número será inserido (0-8)", 0, 8);
    var value = askNumber("\nInforma o valor a ser inserido", 1, 9);

    if (!board.changeValue(row, col, value))
      System.out.printf("\nA posiçao [%s,%s] tem um valor fixo\n", col, row);
    showCurrentGame();

  }

  private static void removeNumber() {
    if (isNull(board)) {
      System.out.println("\nO jogo ainda não foi iniciado");
      return;
    }

    var row = askNumber("\nInforma a linha em que o número será inserido (0-8)", 0, 8);
    var col = askNumber("\nInforme a coluna em que o número será inserido (0-8)", 0, 8);
    if (!board.clearValue(row, col))
      System.out.printf("\nA posiçao [%s,%s] tem um valor fixo\n", col, row);
    showCurrentGame();
  }

  private static void showGameStatus() {
    if (isNull(board)) {
      System.out.println("\nO jogo ainda não foi iniciado");
      return;
    }
    System.out.println(board.getStatus().getLabel());
  }

}

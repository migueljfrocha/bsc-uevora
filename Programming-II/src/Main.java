import java.util.Scanner;

import controller.GameController;

/**
 * Main class of the game that is responsible for the start of the game.
 */
public class Main {

    /**
     * Main method of the game.
     * 
     * @param args - arguments of the main method
     */
    public static void main(String[] args) {
        GameController game = null;
        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("0: Começar um novo jogo \n" + "1: Continuar um já existente");
            int option = scanner.nextInt();
            scanner.nextLine();

            if(option == 0){
                System.out.print("Nome do jogador: ");
                String playerName = scanner.nextLine();

                System.out.println();
                System.out.println( "0: Jogar a partir da lista de jogos já definida \n" + 
                                    "1: Jogar a partir de jogos gerados com o dicionário \n" + 
                                    "Atenção: Cada utilizador só pode escolher um tipo de jogo.");
                option = scanner.nextInt();
                scanner.nextLine();
                if(option == 0){
                    game = new GameController(playerName, true);
                }else if(option == 1){
                    game = new GameController(playerName, false);
                }else{
                    System.out.println("Opção inválida");
                    System.exit(1);
                }

            }else if(option == 1){
                System.out.println();
                System.out.print("Nome do jogador: ");
                
                String playerName = scanner.nextLine();
                game = new GameController(playerName);

            }else{
                System.out.println("Opção inválida");
                System.exit(1);
            }
            System.out.println();
            System.out.println("0: Continuar no terminal \n" + "1: Continuar na interface gráfica");
            option = scanner.nextInt();
            scanner.nextLine();

            if(game != null){
                if(option == 0){
                    game.startGame(option);
                }
                else if(option == 1){
                    game.startGame(option);
                }else{
                    System.out.println("Opção inválida");
                }
            }else{
                System.out.println("Erro ao iniciar o jogo");
            }

            
        }
        
    }

}
/**********************************************************************************************************************************************************************************
 * 
 * Authors: Miguel Pombeiro (57829), Miguel Rocha (58501), Rafael Prezado (57355)
 * Course: Programação I
 * School Year: 2023/2024
 * 
 * This program simulates the game Oware (Ouri) on a Computer Terminal.
 * This simulation follows the rules described on the .pdf file provided by the teachers.
 * 
**********************************************************************************************************************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>                                                                                                               //Used for rand and srand in the computerMove function.
#include "ouri_func.h"


int main (int argc, char *argv[]) {
    int board [14] = {0,4,4,4,4,4,4,4,4,4,4,4,4,0};

    int move=0, landed=0, currentPlayer=2, win=0, mode=0;

    printf("\n\e[1;36mWelcome to Ouri!!\e[0m\n\n");

    loadGame(board, argc, argv);  

    do {                                                                                                                        //Asks the player for a a game mode until the selection is valid.
        printf("Available Gamemodes:\n \e[1m1\e[m: Player vs Player\n \e[1m2\e[m: Player vs Computer\n\nPlease select a gamemode: ");
        if (scanf ("%d", &mode)==1){                                                                                            //If the user inputs an invalid character (different from an int), it will not be accepted.
        }
        else {
            while (getchar() != '\n');                                                                                          //This loop will clear the player's illegal input from stdin. It will stop when it reaches the new line escape ('\n').
        }
        if (mode!=1 && mode!=2) {
            printf ("Invalid gamemode selected. Please select a valid gamemode.\n");
        }
    } while(mode!=1 && mode!=2);

    if (mode==1){                                                                                                               //If the player selects the Player vs Player mode, the game will start with the following loop.
        while (!win) {                                                                                                          //This loop calls upon the various functions that are needed to play the game. Will only stop when an end game condition is reached or the game is saved.

            currentPlayer=playerRotate (currentPlayer);                                                                         //Rotates the player between P1 and P2

            drawBoard (board, currentPlayer, mode);                                                                             //Draws the board

            move = askNextMove (board, currentPlayer, mode);                                                                    //Asks the player for a move                            

            if (move == 0) {                                                                                                    //If the player inputs 0, the game will be saved and the loop will stop.                    
                saveFile (board);
                break;
            }

            landed=theMove (board, move);                                                                                       //Performs the move and saves the position where the last rock was placed.

            capture (board, currentPlayer, landed);                                                                             //Captures rocks in case the conditions are met.

            if (checkForEnd (board, &currentPlayer))                                                                            //Checks if the game has ended.                 
                win=checkWin(board);                                                                                            //If the game has ended, it will save who won or if draw was reached.
        }
    }

    else {                                                                                                                      //If the player selects the Player vs Computer mode, the game will start with the following loop.
        while (!win) {                                                                                                          //This loop calls upon the various functions that are needed to play the game. Will only stop when an end game condition is reached or the game is saved.

            currentPlayer=playerRotate (currentPlayer);                                                                         //Rotates the player between P1 and P2

            drawBoard (board, currentPlayer, mode);                                                                             //Draws the board

            if(currentPlayer == 1) {                                                                                            //If the current player is P1, the game will ask for a move.
                move = askNextMove (board, currentPlayer, mode);
            }
            else if(currentPlayer == 2) {                                                                                       //If the current player is P2, the game will call the computerMove function which will return a move (playing against the "computer").
                move = computerMove(board, currentPlayer, mode);
            }

            if (move == 0) {                                                                                                    //If the player inputs 0, the game will be saved and the loop will stop.
                saveFile (board);
                break;
            }

            landed=theMove (board, move);                                                                                       //Performs the move and saves the position where the last rock was placed.

            capture (board, currentPlayer, landed);                                                                             //Captures rocks in case the conditions are met.

            if (checkForEnd (board, &currentPlayer))                                                                            //Checks if the game has ended.
                win=checkWin(board);                                                                                            //If the game has ended, it will save who won or if draw was reached.
        }
    }

    printWinner(board, mode, win);                                                                                              //Displays the state of the board at the end of the game and who won. In case a draw was reached, it will display that instead.

    return 0;                                                                     
    }
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>                                                                                                               //Used for rand and srand in the computerMove function.
#include "ouri_func.h"

/**********************************************************************************************************************************************************************************
 * 
 * This function redraws the board at every turn.
 * It also displays the board according to whcih player's turn it is. 
 * The current player's board is displayed at the bottom.
 * 
**********************************************************************************************************************************************************************************/
void drawBoard (int *pBoard, int player, int mode) {
    system ("clear");

    if (player==1 && mode==1) {
    printf ("\nPlayer 2              -               Player 1\n\n");
    printf ("┌──────┬────┬────┬────┬────┬────┬────┬──────┐\n");
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│      │ %2d │ %2d │ %2d │ %2d │ %2d │ %2d │      │\n", *(pBoard+12), *(pBoard+11), *(pBoard+10), *(pBoard+9), *(pBoard+8), *(pBoard+7));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│  %2d  ├────┼────┼────┼────┼────┼────┤  %2d  │\n", *(pBoard+13), *(pBoard+0));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│      │ %2d │ %2d │ %2d │ %2d │ %2d │ %2d │      │\n", *(pBoard+1), *(pBoard+2), *(pBoard+3), *(pBoard+4), *(pBoard+5), *(pBoard+6));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("└──────┴────┴────┴────┴────┴────┴────┴──────┘\n\n");
    }
    else if (player==2 && mode==1) {
    printf ("\nPlayer 1              -               Player 2\n\n");
    printf ("┌──────┬────┬────┬────┬────┬────┬────┬──────┐\n");
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│      │ %2d │ %2d │ %2d │ %2d │ %2d │ %2d │      │\n", *(pBoard+6), *(pBoard+5), *(pBoard+4), *(pBoard+3), *(pBoard+2), *(pBoard+1));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│  %2d  ├────┼────┼────┼────┼────┼────┤  %2d  │\n", *(pBoard+0), *(pBoard+13));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│      │ %2d │ %2d │ %2d │ %2d │ %2d │ %2d │      │\n", *(pBoard+7), *(pBoard+8), *(pBoard+9), *(pBoard+10), *(pBoard+11), *(pBoard+12));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("└──────┴────┴────┴────┴────┴────┴────┴──────┘\n\n");
    }
    else if(mode==2 && player==1){
    printf ("\nComputer              -               Player 1\n\n");
    printf ("┌──────┬────┬────┬────┬────┬────┬────┬──────┐\n");
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│      │ %2d │ %2d │ %2d │ %2d │ %2d │ %2d │      │\n", *(pBoard+12), *(pBoard+11), *(pBoard+10), *(pBoard+9), *(pBoard+8), *(pBoard+7));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│  %2d  ├────┼────┼────┼────┼────┼────┤  %2d  │\n", *(pBoard+13), *(pBoard+0));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("│      │ %2d │ %2d │ %2d │ %2d │ %2d │ %2d │      │\n", *(pBoard+1), *(pBoard+2), *(pBoard+3), *(pBoard+4), *(pBoard+5), *(pBoard+6));
    printf ("│      │    │    │    │    │    │    │      │\n");
    printf ("└──────┴────┴────┴────┴────┴────┴────┴──────┘\n\n");
    }
}

/***********************************************************************************************************************************************************************************
 * 
 * This function rotates between player 1 and player 2 after each turn.
 * 
 * THIS FUNCTION WILL INFLUENCE THE "CURRENT_PLAYER" VARIABLE.
 * 
**********************************************************************************************************************************************************************************/
int playerRotate (int player) {
    if (player==1) 
        player=2;

    else if (player==2) 
        player=1;
    
    return player;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function checks if a player has won the game at the end of each turn.
 * 
 * If one of the players has more than 25 rocks in their deposit, that player is returned.
 * 
**********************************************************************************************************************************************************************************/
int checkWin (int *pBoard) {
    int playerWon=0;

    if (*(pBoard+0)>=25)                                                                                                        //If player 1 has more than 25 rocks in their deposit, player 1 wins.
        playerWon=1;
    else if (*(pBoard+13)>=25)                                                                                                  //If player 2 has more than 25 rocks in their deposit, player 2 wins.
        playerWon=2;
    else if (*(pBoard+0)==24 && *(pBoard+13)==24)                                                                               //If both players have 24 rocks in their deposit, the game is a draw.
        playerWon=-1;
    return playerWon;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function asks the player for their next move.
 * Will also run checkIfValid function to check if the move is valid.
 * Returns the move.
 *
**********************************************************************************************************************************************************************************/
int askNextMove (int *pBoard, int player, int mode) {
    int move=-1, valid=0;

    while(!valid){                                                                                                              //Will ask the player for a new move until a valid move is selected.
        printf ("Player %d, please enter your next move (0 -> Save Game , 1-6 -> Select Pit): ", player);

        if (scanf ("%d", &move)==1){                                                                                            //If the user inputs an invalid character (different from an int), the program will not accept it.
        }
        else{
            while (getchar() != '\n');                                                                                          //This loop will clear the player's illegal input from stdin. It will stop when it reaches the new line escape ('\n').
        }
        
        if (move>0) {                                                                       
            if (player==2){                                                                                                     //If Player 2, the move selected will be adjusted to the board array index.
                move+=6;
            }
        }
        
        valid=checkIfValid (pBoard, player, move, mode);                                                                        //Checks if the move selected is valid.
    }
    return move;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function checks for invalid selection from player.
 * 
 * Situations considered:
 * - Player wants to save the game (0).
 * - Player selects a pit with no rocks.
 * - When there are no rocks on one of the sides of the board, and the current player selects a pit that will not place rocks on the other side of the board.
 * - Player selects a pit with only 1 rock.
 * - Player selects a pit that is outside of range.
 * 
 * Default is set to invalid.
 * 
 * Returns 1 if valid, 0 if invalid.
 *
 ***********************************************************************************************************************************************************************************/
int checkIfValid (int *pBoard, int player, int nextMove, int mode) {
    int valid = 0;
    int checkRocks = checkNRocks (pBoard);
    if (nextMove==0)                                                                                                            //Always Valid: Player wants to save the game.
        valid=1;

    else if (nextMove>0 && nextMove<=12) {                                                                                      //Checks if the move is within the board range.
        if ((player==1 && nextMove>6) || (player==2 && nextMove<7)) {
            valid=0;
            printf ("Invalid move. Please insert a valid move (1-6).\n");
        }
        else if (*(pBoard+nextMove)==0) {                                                                                       //Checks if the pit is empty.
            valid=0;

            if(mode==1 || (mode == 2 && player == 1)) {                                                                         //If the game is in Player vs Player mode or if the current player is P1 in Player vs Computer mode, the message will be displayed.    
                printf ("Invalid move: Empty pit selected. Please insert a valid move (1-6).\n");
            }
        }
        else if (checkRocks != 0){                                                                                              //If one of the players has no rocks on their side.
            if (checkRocks == 1) {                                                                                              //Player 1 has no rocks on their side.
                if (nextMove>=7 && nextMove<=12) {                                                                              //Guarantees that the move is made from Player 2 side.
                    for (int i=7, j=6; i<=12; i++, j-- ){                                                                       //Loop iterates through P2 pits and checks if the number of rocks on the selected pit will be enough to place rocks on the other side of the board.
                        if (nextMove==i && *(pBoard+nextMove)>=j) {                                                             //If the number of rocks on the selected pit is enough (j represents the minimum number of rocks on each pit), the move is valid.
                            valid=1;
                            break;
                        }
                        else if (nextMove==i && *(pBoard+nextMove)<j) {                                                         //If the number of rocks on the selected pit is not enough, the move is invalid.
                            valid=0;
                            if (mode==1) {                                                                                      //If the game is in Player vs Player mode, the message will be displayed. 
                                printf ("Invalid move: Pit selected will not place rocks on the other side of the board. Please insert a valid move (1-6).\n");
                            }       
                            break;
                        }
                    }
                }
            }
            else if (checkRocks == 2) {                                                                                         //Player 2 has no rocks on their side.
                if (nextMove>0 && nextMove<=6) {                                                                                //Guarantees that the move is made from Player 1 side.
                    for (int i=1, j=6; i<=6; i++, j-- ){                                                                        //Loop iterates through P1 pits and checks if the number of rocks on the selected pit will be enough to place rocks on the other side of the board.
                        if (nextMove==i && *(pBoard+nextMove)>=j) {                                                             //If the number of rocks on the selected pit is enough (j represents the minimum number of rocks on each pit), the move is valid.
                            valid=1;
                            break;
                        }
                        else if (nextMove==i && *(pBoard+nextMove)<j) {                                                         //If the number of rocks on the selected pit is not enough, the move is invalid.
                            valid=0;
                            printf ("Invalid move: Pit selected will not place rocks on the other side of the board. Please insert a valid move (1-6).\n");
                            break;
                        }
                    }
                }
            }
        }
        else if (*(pBoard+nextMove)==1) {                                                                                       //Checks if the selected pit has only 1 rock.
            valid = 1;                                                                                                          //Sets default to valid. The following check will be done to see if the move is invalid.
            if (nextMove>0 && nextMove<=6) {                                                                                    //Pits on Player 1 side.
                for (int i = 1; i<=6 ; i++) {                                                                                   //Loop iterates through P1 pits and checks if there are pits with more than 1 rock.
                    if (*(pBoard+i)>1) {                                                                                        //If there are pits with more than 1 rock, the move is invalid.
                        valid=0;
                        printf ("Invalid move: Pit selected has only 1 rock. Please insert a valid move (1-6).\n");
                        break;
                    }
                }
            }
            else if (nextMove>=7 && nextMove <=12) {                                                                            //Pits on Player 2 side.
                for (int i = 7; i<=12 ; i++) {                                                                                  //Loop iterates through P2 pits and checks if there are pits with more than 1 rock.
                    if (*(pBoard+i)>1) {                                                                                        //If there are pits with more than 1 rock, the move is invalid.
                        valid=0;
                        if(mode==1) {                                                                                           //If the game is in Player vs Player mode, the message will be displayed. 
                            printf ("Invalid move: Pit selected has only 1 rock. Please insert a valid move (1-6).\n");
                        }
                        break;
                    }
                }
            }
        }
        else{                                                                                                                   //Pit has more than 1 rock, there are rocks on the other side of the board and move is within accepted range (1-12).
            if (player==1 && nextMove>0 && nextMove<=6) {                                                                       //If Player 1, checks if move is within their range (1-6).
                valid=1;
            }
            else if (player==2 && nextMove>=7 && nextMove<=12) {                                                                //If Player 2, checks if move is within their range (7-12).
                valid=1;
            }
        }                                                                                     
    }
    else {                                                                                                                      //If the move is not within the range 1-12, the move is invalid.
        valid=0;
        printf ("Invalid move. Please insert a valid move (1-6).\n");
    }
    return valid;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function moves the rocks from the selected pit to the next pits.
 * 
 * THIS FUNCTION CAN MAKE CHANGES IN THE BOARD ARRAY: Registers moves in the board array.
 * 
 * Returns the number of the pit (board array index) where the last rock was placed.
 *
 ***********************************************************************************************************************************************************************************/
int theMove (int *pBoard, int nextMove) {  
    int landed=0, rocks=0;

    rocks = *(pBoard+nextMove);                                                                                                 //Stores the number of rocks in the selected pit in internal variable.
    *(pBoard+nextMove)=0;                                                                                                       //Empties the selected pit.
    for (landed=nextMove+1; rocks>0; rocks--, landed++){                                                                        //Loop iterates through the board array (pits) and places the rocks from selected pit.
        if(landed==nextMove)
            landed++;

        if (landed==13)                                                                                                         //If count index reaches 13 (it completed a cycle around the array pits), it will be reset to 1 (first pit on P1 side).
            landed = 1;

        (*(pBoard+landed))++;
    }
    return landed-1;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function checks if it is possible to capture any rocks from the opponent.
 * If the capture is possible, the function registers the capture in the board array.
 * 
 * THIS FUNCTION CAN MAKE CHANGES IN THE BOARD ARRAY: Registers captures in the board array deposits (0 and 13).
 * 
***********************************************************************************************************************************************************************************/
void capture (int *pBoard, int player, int landed) {
    if (player==1) {                                                                                                            //If Player 1, checks if the last rock landed on a pit on Player 2 side.
        if (landed>=7 && landed<=12){
            for (int i=landed; (*(pBoard+i)==2 || *(pBoard+i)==3) && i>=7; i--) {                                               //Loop checks if the last rock landed on a pit with 2 or 3 rocks. If it did, will iterate through all previous pits on P2 side until it reaches a pit with n of rocks != 2 or 3.
                (*(pBoard+0))+=(*(pBoard+i));                                                                                   //If previous conditions are met, will save the rocks on the present pit into P1's deposit (index 0).
                (*(pBoard+i))=0;                                                                                                //Empties the pit.
            }
        }
    }
    else {                                                                                                                      //If Player 2, checks if the last rock landed on a pit on Player 1 side.
        if (landed>=1 && landed <=6){
            for (int i = landed; (*(pBoard+i)==2 || *(pBoard+i)==3) && i>=1; i--) {                                             //Loop checks if the last rock landed on a pit with 2 or 3 rocks. If it did, will iterate through all previous pits on P1 side until it reaches a pit with n of rocks != 2 or 3.
                (*(pBoard+13))+=(*(pBoard+i));                                                                                  //If previous conditions are met, will save the rocks on the present pit into P2's the deposit (index 13).
                (*(pBoard+i))=0;                                                                                                //Empties the pit.
            }
        }
    } 
}

/***********************************************************************************************************************************************************************************
 * 
 * This function checks if any of the players has 0 rocks on their side of the board, at the end of each turn.
 * 
 * If one of the players, has 0 rocks on their side of the board, that player is returned. 
 * Otherwise, the function returns 0.
 * 
***********************************************************************************************************************************************************************************/
int checkNRocks (int *pBoard) {
    int p=0;

    if (*(pBoard+1)==0 && *(pBoard+2)==0 && *(pBoard+3)==0 && *(pBoard+4)==0 && *(pBoard+5)==0 && *(pBoard+6)==0) {             //Checks if Player 1 has 0 rocks on their side. 
        p=1;
    }
    else if (*(pBoard+7)==0 && *(pBoard+8)==0 && *(pBoard+9)==0 && *(pBoard+10)==0 && *(pBoard+11)==0 && *(pBoard+12)==0) {     //Checks if Player 2 has 0 rocks on their side.
        p=2;
    }
    return p;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function checks if there are any possible moves, in case one of the players has 0 rocks on their side (checkNRocks!=0)
 * 
 * Returns 1 if there are any possible moves that will place rocks on the other side of the board.
 * Otherwise, returns 0.
 *
 ************************************************************************************************************************************************************************************/
int whenNoRocks (int *pBoard) {
    int movePossible=0;
    int checkRocks = checkNRocks (pBoard);

        if (checkRocks==1) {                                                                                                    //If Player 1 has 0 rocks on their side.
            if (*(pBoard+7)>5 || *(pBoard+8)>4 || *(pBoard+9)>3 || *(pBoard+10)>2 || *(pBoard+11)>1 || *(pBoard+12)>=1) {       //Checks if there are any pits on P2 side that will aloow them to place at least a rock on P1 side.
                movePossible=1;
            }         
        }
        else if (checkRocks==2) {                                                                                               //If Player 2 has 0 rocks on their side.
            if (*(pBoard+1)>5 || *(pBoard+2)>4 || *(pBoard+3)>3 || *(pBoard+4)>2 || *(pBoard+5)>1 || *(pBoard+6)>=1) {          //Checks if there are any pits on P1 side that will aloow them to place at least a rock on P2 side.
                movePossible=1;
            }
        }
    return movePossible;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function will move all the rocks from the each player's pits to their respective deposits.
 * 
 * CAUTION: This function should only be called there are no more moves available:
 * - One of the players has no rocks on their side and the opponent has no possible moves that will place rocks on the other side of the board.
 * - The game enters an endless cycle when there are no more captures available (tipically when there are only 2 rocks on the board - one on each side).
 * 
 * THIS FUNCTION CAN MAKE CHANGES IN THE BOARD ARRAY!!
 *
 ************************************************************************************************************************************************************************************/
void allRocksToDeposit (int *pBoard) {
    for (int i=1; i<=6; i++) {                                                                                                  //Loop iterates through P1 pits and moves all their rocks to P1 deposit.
        *(pBoard+0)+=(*(pBoard+i));
        *(pBoard+i)=0;
    }
    for (int i=7; i<=12; i++) {                                                                                                 //Loop iterates through P2 pits and moves all their rocks to P2 deposit.
        *(pBoard+13)+=(*(pBoard+i));
        *(pBoard+i)=0;
    }
}

/***********************************************************************************************************************************************************************************
 * 
 * This function checks if the game has ended.
 * 
 * Situations considered for the game to end:
 * - One of the players has more than 25 rocks in their deposit or both players have 24 rocks in their deposit.
 * - One of the players has no rocks on their side and the opponent has no possible moves that will place rocks on the other side of the board.
 * - The game enters an endless cycle when there are no more captures available (only situation considered for this is when there are only 2 rocks on the board - one on each side).
 * 
 * If the game has ended, the function returns 1.
 * Otherwise, the function returns 0.
 *
 ************************************************************************************************************************************************************************************/
int checkForEnd (int *pBoard, int *player) {
    int end=0, checkRocks=checkNRocks (pBoard);
    int totalRocks=0;

    for (int i=1; i<13; i++) {
        totalRocks+=(*(pBoard+i));
    }
    if (!checkWin (pBoard)) {                                                                                                   //If none of the players has won the game.                                   
        if (checkRocks){                                                                                                        //If one of the players has no rocks on their side.
            if (whenNoRocks (pBoard)) {                                                                                         //If there are any moves that will allow the opponent to place rocks on the other side of the board.
                end=0;                                                                                                          //The game will continue.
                if (*player!=checkRocks) {                                                                                      //If the player with no rocks on their side is not the current player, the current player will rotate.
                    *player=playerRotate (*player);
                }
            }
            else {                                                                                                              //If there are no moves that will allow the opponent to place rocks on the other side of the board.
                end=1;                                                                                                          //The game will end.
                allRocksToDeposit (pBoard);                                                                                     //All the rocks on the board will be moved to their respective player's deposits.
            }
        }
        else if (totalRocks==2) {                                                                                               //If there are only 2 rocks on the board. -->> ENDLESS CYCLE
            for (int i=1, j=7; i<=6 && j<=12; i++, j++) {                                                                       //Loop will iterate through P1 and P2 pits.
                if (*(pBoard+i)==1 && *(pBoard+j)==1) {                                                                         //If rocks are on opposite pits, no capture will be possible 
                    end=1;
                    allRocksToDeposit (pBoard);
                }
            }
        }
    }
    else {                                                                                                                      //Check if one of the conditions for win/draw have been met.
        end=1;                                                                                                                  //If they have, the game should end.
    }
    return end;
}

/***********************************************************************************************************************************************************************************
 * 
 * This function requests the player for a name for the save file. 
 * This file will contain all the information of the board.
 * 
 * The name typed by the user doesn't need the extension ".txt", because strcat will add that.
 *
 ************************************************************************************************************************************************************************************/
void saveFile (int *pBoard){
    char fileName[100];
    fflush(stdin);                                                                                                              //Clears the stdin to make sure there are no characters left in the buffer.
    printf("Save file as: ");
    scanf("%s", fileName);                                                                                                      //Asks the user for a name for the save file.
    strcat(fileName, ".txt");                                                                                                   //Adds the ".txt" extension to the file name.
    FILE *fp;
    fp = fopen(fileName, "w");                                                                                                  //Opens the file with the name provided by the user.
    if (fp == NULL)
    {
        printf("Unable to open file, please try again\n");
        system("pause");
        exit(1);
    }
    fprintf(fp, "%d\n%d %d %d %d %d %d\n%d\n%d %d %d %d %d %d", pBoard[13],pBoard[12],pBoard[11],pBoard[10],pBoard[9],pBoard[8],pBoard[7],pBoard[0],pBoard[1],pBoard[2],pBoard[3],pBoard[4],pBoard[5],pBoard[6]);   //Prints the board array to the file.
    fclose(fp);
    printf("\n\nGame saved successfully!!\n\n");
}

/***********************************************************************************************************************************************************************************
 * 
 * This function calculates which is the "best" move for the computer.
 * 
 * It will check all the possible moves and evaluate the number of rocks captured on each move;
 * The best move is the one that captures the most rocks.
 * If there aren't any moves that capture it will return the a random move among the valid ones.
 *
 ************************************************************************************************************************************************************************************/
int computerMove(int *pBoard, int player, int mode) {
    srand(time(NULL));                                                                                                                                                                  
    int bestMove = 7;
    int maxCaptures = 0 , captures = 0;
    int tempBoard[14];                                                                                                          // Create a temporary board to test the moves.

    for (int i = 7; i <= 12; i++) {
        int move = i;
        if(!checkIfValid(pBoard, player, i, mode)){                                                                             // If the move isn't valid, go to the following iteration
            continue;
        }                                                                                                                       
        memcpy(tempBoard, pBoard, sizeof(tempBoard));                                                                           // Copy the board to a temporary board

        int landed = theMove(tempBoard, i);                                                                                     // Make the move on the temporary board
        capture(tempBoard, player, landed);                                                                                     // Capture the rocks on the temporary board

        captures = tempBoard[13] - *(pBoard+13);                                                                                //Evaluate the difference between the number of rocks captured on the temporary board and the original board

        if (captures > maxCaptures) {                                                                                           // If the number of captured rocks is greater than the current maximum, update the best move
            maxCaptures = captures;
            bestMove = move;
        }
    }

    if(bestMove==7 && captures == 0){                                                                                           // If there are no captures, choose a random move between the available ones
        int randomNum = (rand() % 6)+7;                                                                                         // Generate a random number between 7 and 12 
        while(!checkIfValid(pBoard, player, randomNum, mode)){                                                                  // Check if the random move is valid, if not choose another one untill it is valid
            randomNum = (rand() % 6)+7; 
        }
        bestMove = randomNum;    
    }

    return bestMove;
}

/**********************************************************************************************************************************************************************************
 * 
 * This function will check if the user has entered a valid command line argument.
 * This argument should correspond to a save file from this game in the same directory or to a valid path to a save file.
 * 
**********************************************************************************************************************************************************************************/
void loadGame(int *pBoard, int argc, char *argv[]){
    if(argc>2){                                                                                                                 //If the user has entered more than 2 arguments, the program will exit.
        printf("Failed to load the previous game file. Please try again\n ");
        exit(1);
    }
    else if(argc==2) {                                                                                                          //If the user has entered the correct number of arguments (2), the program will check if the second argument is a valid path to a save file.
        FILE *LoadGame;
        LoadGame = fopen(argv[1], "r");                                                                                         //Opens the file with the name provided by the user.

        if(LoadGame==NULL){                                                                                                     //If the file is invalid, the program will exit.
            printf("Unable to read the load game file. Please try again\n");
            system("pause");
            exit(1);
        }
        else{                                                                                                                   //If the file is valid, the program will load the saved game state.
            fscanf(LoadGame, "%d\n%d %d %d %d %d %d\n%d\n%d %d %d %d %d %d",(pBoard+13),(pBoard+12),(pBoard+11),(pBoard+10),(pBoard+9),(pBoard+8),(pBoard+7),(pBoard+0),(pBoard+1),(pBoard+2),(pBoard+3),(pBoard+4),(pBoard+5),(pBoard+6));
        }
        
        fclose(LoadGame);
    }
}

/**********************************************************************************************************************************************************************************
 * 
 * This function prints the winner of the game.
 * Must be called only when the game has ended.
 * 
 * Will print the state of the board at the end of the game.
 * Will print the winnner and provide the number of rocks captured by both players.
 * 
**********************************************************************************************************************************************************************************/
void printWinner(int *pBoard, int mode, int win){
    if (win==1 && mode==1) {                                                                                                    //If Player 1 won the game in Player vs Player mode.
        drawBoard (pBoard, win, mode);
        printf (">>>>>>>>>>>>>>> \e[1mPlayer 1 WON\e[m <<<<<<<<<<<<<<<");
        printf ("\n\nPlayer 1 captured %d rocks\nPlayer 2 captured %d rocks\n\n", *(pBoard+0), *(pBoard+13));
    }
    
    else if (win==2 && mode==1) {                                                                                               //If Player 2 won the game in Player vs Player mode.
        drawBoard (pBoard, win, mode);
        printf (">>>>>>>>>>>>>>> \e[1mPlayer 2 WON \e[m <<<<<<<<<<<<<<<");
        printf ("\n\nPlayer 1 captured %d rocks\nPlayer 2 captured %d rocks\n\n", *(pBoard+0), *(pBoard+13));
    }

    else if (win==-1) {                                                                                                         //If the game ended in a draw in either game mode.
        drawBoard (pBoard, 1, mode);
        printf (">>>>>>>>>>>>>>>>>>> DRAW <<<<<<<<<<<<<<<<<<<");
        printf ("\n\nBoth players captured 24 rocks\n\n");
    }

    else if(win == 1 && mode == 2) {                                                                                            //If Player 1 won the game in Player vs Computer mode.
        drawBoard(pBoard, win, mode);
        printf(">>>>>>>>>>>>>>> \e[1mPlayer 1 WON\e[m <<<<<<<<<<<<<<<");
        printf ("\n\nPlayer 1 captured %d rocks\nComputer captured %d rocks\n\n", *(pBoard+0), *(pBoard+13));
    }

    else if (win == 2 && mode == 2) {                                                                                           //If Computer won the game in Player vs Computer mode.
        drawBoard(pBoard, 1, mode); 
        printf(">>>>>>>>>>>>>>> \e[1mComputer WON\e[m <<<<<<<<<<<<<<<");
        printf ("\n\nPlayer 1 captured %d rocks\nComputer captured %d rocks\n\n", *(pBoard+0), *(pBoard+13));
    }
}
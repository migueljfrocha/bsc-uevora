#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "queue.h"
#include "list.h"
#include "process.h"
#include "scheduler.h"
#include "Input/inputs.h"
#include "auxiliary.h"



/**
 * Simulates the scheduler execution.
 * The simulation runs for a maximum of MAX_INSTANTS time instants.
 * 
 * @param scheduler The scheduler to be simulated.
 */
void simulate(Scheduler *scheduler)
{
    int instantTime = 1;
    printInfoLabel();
    schedulerNew(scheduler, 1);
    int currentInstruction;

    while(instantTime <= MAX_INSTANTS){
        if (scheduler->runningProcess == NULL && !isEmpty(scheduler->ready)) {
            schedulerDispatch(scheduler);
        }
        
        if (scheduler->runningProcess != NULL){
            currentInstruction = fetchNextInstruction(scheduler->runningProcess);
            InstructionType type = getInstructionType(currentInstruction);

            if(type == EXEC){
                executeInstruction(scheduler, currentInstruction);
                printCurrentStates(scheduler, instantTime);
            }else{
                printCurrentStates(scheduler, instantTime);
                executeInstruction(scheduler, currentInstruction);
            }
        }else{
            printCurrentStates(scheduler, instantTime);
        }

        timeDecrement(scheduler);
        instantTime++;
    }
}


/**
 * Initializes the program list with the given instructions.
 * 
 * @param nPrograms The number of programs.
 * @param nInstructions The number of instructions per program.
 * @param programas The matrix of instructions for the programs from the input.
 * 
 * @return A pointer to the initialized program list.
 */
int** initPrograms(int nPrograms, int nInstructions, int programas[][nPrograms])
{
    int **programList = (int**)malloc(nPrograms * sizeof(int*));
    for (int i = 0; i < nPrograms; i++){
        programList[i] = (int*)malloc(nInstructions * sizeof(int));
        for (int j = 0; j < nInstructions; j++){
            programList[i][j] = programas[j][i];
        }
    }
    return programList;
}


/**
 * Main function to run the scheduler simulation.
 * It takes an input number as a command line argument to select the input program matrix from the provided inputs.h file.
 * 
 * @param argc The number of command line arguments.
 * @param argv The command line arguments.
 * 
 * @return 0 on success, 1 on failure.
 */
int main(int argc, char const *argv[]) {
    int (*programas)[20];
    int nRows, nColumns;
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <input_number>\n", argv[0]);
        return EXIT_FAILURE;
    }

    int inputNumber = atoi(argv[1]);

    switch(inputNumber){
        case 0:
            programas = input00;
            nRows = sizeof(input00) / sizeof(input00[0]);
            nColumns = sizeof(input00[0]) / sizeof(input00[0][0]);
            break;
        case 1:
            programas = input01;
            nRows = sizeof(input01) / sizeof(input01[0]);
            nColumns = sizeof(input01[0]) / sizeof(input01[0][0]);
            break;
        case 2:
            programas = input02;
            nRows = sizeof(input02) / sizeof(input02[0]);
            nColumns = sizeof(input02[0]) / sizeof(input02[0][0]);
            break;
        case 3:
            programas = input03;
            nRows = sizeof(input03) / sizeof(input03[0]);
            nColumns = sizeof(input03[0]) / sizeof(input03[0][0]);
            break;
        case 4:
            programas = input04;
            nRows = sizeof(input04) / sizeof(input04[0]);
            nColumns = sizeof(input04[0]) / sizeof(input04[0][0]);
            break;
        case 5:
            programas = input05;
            nRows = sizeof(input05) / sizeof(input05[0]);
            nColumns = sizeof(input05[0]) / sizeof(input05[0][0]);
            break;
        
        /***************************************
         *  ADD YOUR EXTRA INPUTS HERE
        ***************************************/

        default:
            fprintf(stderr, "Invalid input number.\n");
            return EXIT_FAILURE;
    }


    int** programlist = initPrograms(nColumns, nRows, programas);
    Scheduler* scheduler = createScheduler(programlist, nColumns, nRows);

    simulate(scheduler);

    // Free allocated memory
    destroyScheduler(scheduler);
    scheduler = NULL;

    // Free program list
    destroyProgramList(programlist, nColumns);
    programlist = NULL;

    return EXIT_SUCCESS;
}

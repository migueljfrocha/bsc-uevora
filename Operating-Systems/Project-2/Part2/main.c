#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "queue.h"
#include "list.h"
#include "process.h"
#include "scheduler.h"
#include "Input/inputs_part2.c"
#include "auxiliary.h"



/**
 * Simulates the scheduler execution.
 * The simulation runs for a maximum of MAX_INSTANTS time instants.
 * 
 * @param scheduler The scheduler to be simulated.
 */
void simulate(Scheduler *scheduler) {

    printInfoLabel();
    schedulerNew(scheduler, 1);
    int currentInstruction;

    while(scheduler->instantTime <= MAX_INSTANTS && scheduler->nActiveProcesses > 0){
        if (scheduler->runningProcess == NULL && !isEmpty(scheduler->ready)) {
            schedulerDispatch(scheduler);
        }
        
        if (scheduler->runningProcess != NULL){
            currentInstruction = fetchNextInstruction(scheduler->runningProcess);
            InstructionType type = getInstructionType(currentInstruction);

            if (type == HALT || type == IO) {
                printCurrentStates(scheduler);
                executeInstruction(scheduler, currentInstruction);
            }
            else {
                executeInstruction(scheduler, currentInstruction);
                printCurrentStates(scheduler);
            }
            
        } else{
            printCurrentStates(scheduler);
        }

        timeDecrement(scheduler);
        scheduler->instantTime++;
    }
}


/**
 * Initializes the program list with the given instructions.
 * 
 * @param nPrograms The number of programs.
 * @param nInstructions The number of instructions per program.
 * @param programs The matrix of instructions for the programs from the input.
 * 
 * @return A pointer to the initialized program list.
 */
int** initPrograms(int nPrograms, int nInstructions, int programs[][nPrograms]) {

    int **programList = (int**)malloc(nPrograms * sizeof(int*));
    for (int i = 0; i < nPrograms; i++){
        programList[i] = (int*)malloc(nInstructions * sizeof(int));
        for (int j = 0; j < nInstructions; j++){
            programList[i][j] = programs[j + 1][i];
        }
    }
    return programList;
}


/**
 * Initializes the program address space with the first row of the program matrix.
 * 
 * @param nPrograms The number of programs.
 * @param programs The matrix of instructions for the programs from the input.
 * 
 * @return A pointer to the initialized program memory.
 */
int* initProgramAddressSpace(int nPrograms, int programs[][nPrograms]) {

    int *programMemory = (int*)malloc(nPrograms * sizeof(int));
    for (int i = 0; i < nPrograms; i++){
        programMemory[i] = programs[0][i];
    }
    return programMemory;
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

    int (*programs)[20];
    int nRows, nColumns;
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <input_number>\n", argv[0]);
        return EXIT_FAILURE;
    }

    int inputNumber = atoi(argv[1]);

    switch(inputNumber){
        case 0:
            programs = input00;
            nRows = sizeof(input00) / sizeof(input00[0]);
            nColumns = sizeof(input00[0]) / sizeof(input00[0][0]);
            break;
        case 1:
            programs = input01;
            nRows = sizeof(input01) / sizeof(input01[0]);
            nColumns = sizeof(input01[0]) / sizeof(input01[0][0]);
            break;
        case 2:
            programs = input02;
            nRows = sizeof(input02) / sizeof(input02[0]);
            nColumns = sizeof(input02[0]) / sizeof(input02[0][0]);
            break;
        case 3:
            programs = input03;
            nRows = sizeof(input03) / sizeof(input03[0]);
            nColumns = sizeof(input03[0]) / sizeof(input03[0][0]);
            break;
        case 4:
            programs = input04;
            nRows = sizeof(input04) / sizeof(input04[0]);
            nColumns = sizeof(input04[0]) / sizeof(input04[0][0]);
            break;
        case 5:
            programs = input05;
            nRows = sizeof(input05) / sizeof(input05[0]);
            nColumns = sizeof(input05[0]) / sizeof(input05[0][0]);
            break;
        case 6:
            programs = input06;
            nRows = sizeof(input06) / sizeof(input06[0]);
            nColumns = sizeof(input06[0]) / sizeof(input06[0][0]);
            break;
        case 7:
            programs = input07;
            nRows = sizeof(input07) / sizeof(input07[0]);
            nColumns = sizeof(input07[0]) / sizeof(input07[0][0]);
            break;
        case 8:
            programs = input08;
            nRows = sizeof(input08) / sizeof(input08[0]);
            nColumns = sizeof(input08[0]) / sizeof(input08[0][0]);
            break;
        case 9:
            programs = input09;
            nRows = sizeof(input09) / sizeof(input09[0]);
            nColumns = sizeof(input09[0]) / sizeof(input09[0][0]);
            break;
        case 10:
            programs = input10;
            nRows = sizeof(input10) / sizeof(input10[0]);
            nColumns = sizeof(input10[0]) / sizeof(input10[0][0]);
            break;
        case 11:
            programs = input11;
            nRows = sizeof(input11) / sizeof(input11[0]);
            nColumns = sizeof(input11[0]) / sizeof(input11[0][0]);
            break;
            
        /***************************************
         *  ADD YOUR EXTRA INPUTS HERE
        ***************************************/

        default:
            fprintf(stderr, "Invalid input number.\n");
            return EXIT_FAILURE;
    }


    int** programlist = initPrograms(nColumns, nRows - 1, programs);
    int* programsAddressSpace = initProgramAddressSpace(nColumns, programs);

    Scheduler* scheduler = createScheduler(programlist, programsAddressSpace, nColumns, nRows - 1);

    simulate(scheduler);

    // Free allocated memory
    destroyScheduler(scheduler);
    scheduler = NULL;

    // Free program list
    destroyProgramList(programlist, nColumns);
    programlist = NULL;

    return EXIT_SUCCESS;
}
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "queue.h"
#include "list.h"
#include "process.h"
#include "scheduler.h"
#include "auxiliary.h"



/**
 * Get the instruction type based on the instruction value.
 * 
 * @param instruction The instruction value.
 * 
 * @return The type of the instruction.
 */
InstructionType getInstructionType(int instruction)
{
    if (instruction == 0) {
        return HALT;
    } else if (instruction >= 101 && instruction <= 199) { 
        return JUMP;
    } else if (instruction >= 201 && instruction <= 299) {
        return EXEC;
    } else if (instruction < 0) {
        return IO;
    } else {
        return ANY;
    }
}


/**
 * Execute the instruction based on its type.
 * Changes the state of the process and the scheduler accordingly, if needed.
 * 
 * @param scheduler The scheduler to which the process belongs.
 * @param instruction The instruction to be executed.
 */
void executeInstruction(Scheduler* scheduler, int instruction)
{   
    InstructionType type = getInstructionType(instruction);
    Process* process = scheduler->runningProcess;

    switch (type) {
        case JUMP:
            changeCurrentInstruction(process, instruction - 99);
            break;
        case EXEC:
            schedulerNew(scheduler, instruction - 200);
            break;
        case IO:
            schedulerBlock(scheduler, -instruction);
            break;
        case HALT:
            schedulerRelease(scheduler);
            break;
        default:
            break;
    }
}


/**
 * Destroy the program list and free its resources.
 * This includes freeing each program's instructions and the program list itself.
 * 
 * @param programList The program list to be destroyed.
 * @param nPrograms The number of programs in the list.
 */
void destroyProgramList(int** programList, int nPrograms)
{
    for (int i = 0; i < nPrograms; i++){
        free(programList[i]);
    }
    free(programList);
}


/**
 * Print the information label for the process states.
 * This includes the time instant and the state of each process.
 */
void printInfoLabel()
{
    printf("%-7s  ", "time inst");
    for (int i = 0; i < MAX_PROCESSES; i++){
        printf(" %s%-2d         ", "proc", i + 1);
    }
    printf("\n");

}


/**
 * Print the current states of the processes in the scheduler.
 * This includes the time instant and the state of each process.
 * 
 * @param scheduler The scheduler from which to print the process states.
 * @param instantTime The current time instant.
 */
void printCurrentStates(Scheduler* scheduler, int instantTime)
{
    printf("%-7d", instantTime);
    for (int i = 0; i < MAX_PROCESSES; i++) {
        Process* p = scheduler->processes[i];
        if (p == NULL) {
            printf("     %-11s", "  ");
            continue;
        }
        switch (p->state)
        {
        case NEW:
            printf("     %-11s", "NEW");
            break;
        case READY:
            printf("     %-11s", "READY");
            break;
        case RUN:
            printf("     %-11s", "RUN");
            break;
        case BLOCKED:
            printf("     %-11s", "BLOCKED");
            break;
        case EXIT:
            printf("     %-11s", "EXIT");
            break;
        case TERMINATED:
            printf("     %-11s", "   ");
            break;
        }
    }
    printf("  \n");
}
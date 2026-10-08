#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "process.h"
#include "auxiliary.h"
#include "memory_management.h"


/**
 * Get the instruction type based on the instruction value.
 * 
 * @param instruction The instruction value.
 * 
 * @return The type of the instruction.
 */
InstructionType getInstructionType(int instruction) {

    if (instruction < 0) {
        return IO;
    } else if (instruction == 0) {
        return HALT;
    } else if(instruction >= 1 && instruction <= 100){
        return JUMPF;
    } else if (instruction >= 101 && instruction <= 199) { 
        return JUMPB;
    } else if (instruction >= 201 && instruction <= 299) {
        return EXEC;
    } else if(instruction >= 1000 && instruction <= 15999){
        return LOAD_STORE;
    } else if(instruction >= 1000000000 && instruction <= 2109999999){
        return SWAP_MEMCPY;
    } else {
        return ANY;
    }
}



/**
 * Format the associated frames of a process into a string.
 * The frames are formatted as "F<frame_number>" and enclosed in square brackets.
 * 
 * @param output The output string to store the formatted frames.
 * @param process The process whose frames are to be formatted.
 */
void formatProcessFrames(char* output, Process *process) {
    
    char frameStr[32];
    output[0] = '[';
    output[1] = '\0'; // initialize output as an empty string for safe concatenation
    for(int i = 0; i < process->maxPages; i++){
        if(process->pageTable[i] == EMPTY_FRAME){
            continue;
        } else{
            if (strlen(output) > 1) 
                strcat(output, ",");
            sprintf(frameStr, "F%d", process->pageTable[i]);
            strcat(output, frameStr);
        }
    }
    strcat(output, "]");
}


/**
 * Execute the instruction based on its type.
 * Changes the state of the process and the scheduler accordingly, if needed.
 * 
 * @param scheduler The scheduler to which the process belongs.
 * @param instruction The instruction to be executed.
 */
void executeInstruction(Scheduler* scheduler, int instruction) {   

    InstructionType type = getInstructionType(instruction);
    Process* process = scheduler->runningProcess;

    switch (type) {
        case JUMPB:
            changeCurrentInstruction(process, -(instruction - 99));
            break;
        case JUMPF:
            changeCurrentInstruction(process, (instruction % 100) - 1);
            break;
        case LOAD_STORE:
            handleLoadStore(scheduler, process, instruction - 1000);
            break;
        case SWAP_MEMCPY:
            swapMemcpyHandle(scheduler, process, instruction);
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
void destroyProgramList(int** programList, int nPrograms) {
    
    for (int i = 0; i < nPrograms; i++){
        free(programList[i]);
    }
    free(programList);
}


/**
 * Print the information label for the process states.
 * This includes the time instant and the process numbers.
 */
void printInfoLabel() {

    printf("%-9s", "time inst");
    for (int i = 0; i < MAX_PROCESSES; i++){
        printf("  %s%-2d                  ", "proc", i + 1);
    }
    printf("\n");
}


/**
 * Print the current states of the processes in the scheduler.
 * This includes the time instant, the state of each process and their associated frames (if any).
 * 
 * @param scheduler The scheduler from which to print the process states.
 */
void printCurrentStates(Scheduler* scheduler) {

    char frameBuffer[32] = {0};

    printf("%-8d", scheduler->instantTime);
    
    for (int i = 0; i < MAX_PROCESSES; i++) {
        Process* p = scheduler->processes[i];
        if (p == NULL) {
            printf("%-26s", "");
            continue;
        }
                
        formatProcessFrames(frameBuffer, p);

        switch (p->state) {
        
        case NEW:
            printf("   %-23s", "NEW");
            break;
        case READY:
            printf("   READY %-17s", frameBuffer);
            break;
        case RUN:
            printf("   RUN %-19s", frameBuffer);
            break;
        case BLOCKED:
            printf("   BLOCKED %-15s", frameBuffer);
            break;
        case EXIT:
            printf("   EXIT %-18s", frameBuffer);
            break;
        
        case SIG_ILL:
            printf("   SIGILL %-16s", frameBuffer);
            break;
        case SIG_SEGV:
            printf("   SIGSEGV %-15s", frameBuffer);
            break;
        case SIG_EOF:
            printf("   SIGEOF %-16s", frameBuffer);
            break;
        
        case TERMINATED:
            printf("%-26s", "");
            break;
        }

        frameBuffer[0] = '\0';
    }

    printf(" \n");
}
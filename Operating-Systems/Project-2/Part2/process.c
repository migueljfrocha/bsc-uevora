#include <limits.h>
#include <stdio.h>
#include <stdlib.h>

#include "queue.h"
#include "process.h"
#include "memory_management.h"


/**
 * Create a new process with the given id and instructions.
 * The process is initialized with the NEW state and a time of 0.
 * 
 * @param id The id of the process.
 * @param instructions The instructions of the process.
 * @param addressSpace The address space of the process.
 * @param nInstructions The number of instructions.
 * 
 * @return A pointer to the newly created process.
 */
Process* createProcess(int id, int* instructions, int addressSpace, int nInstructions) {
    
    Process *process = (Process *) malloc(sizeof(Process));

    process->id = id;
    process->instructions = instructions;
    process->nInstructions = nInstructions;
    process->addressSpace = addressSpace;
    process->current_instruction = 0;
    process->state = NEW;
    process->time = 0;

    process->maxPages = ((process->addressSpace - 1) / FRAME_SIZE) + 1;
    process->pageTable = (int*) malloc(process->maxPages * sizeof(int));
    if (process->pageTable == NULL) {
        fprintf(stderr, "Memory allocation failed for process page table.\n");
        exit(EXIT_FAILURE);
    }
    // Initialize the page table with EMPTY_FRAME values
    for (int i = 0; i < process->maxPages; i++) {
        process->pageTable[i] = EMPTY_FRAME;
    }
    
    return process;
}


/**
 * Fetch the next instruction from the process.
 * 
 * @param process The process from which to fetch the instruction.
 * 
 * @return The next instruction or 0 if there are no more instructions.
 */
int fetchNextInstruction(Process *process) {

    if (process->current_instruction >= process->nInstructions) {
        // If there are no more instructions, set the state to SIG_EOF and return 0
        fprintf(stderr, "Error: No more instructions to fetch on process %d.\n", process->id);
        process->state = SIG_EOF; 
        return 0;
    }

    int instruction = process->instructions[process->current_instruction];
    process->current_instruction++;
    return instruction;
}


/**
 * Destroy the process and free its resources.
 * 
 * @param process The process to be destroyed.
 */
void destroyProcess(Process *process) {
    free(process->pageTable);
    free(process);
}


/**
 * Change the current instruction of the process to a new value.
 * Sums the jump value to the current instruction.
 * 
 * @param process The process whose current instruction will be changed.
 * @param jump The value to change the current instruction to.
 */
void changeCurrentInstruction(Process *process, int jump) {
    int current_jump = process->current_instruction + jump;
    if(current_jump < 0 || current_jump >= process->nInstructions){
        process->state = SIG_ILL;
    } else {
        process->current_instruction = current_jump;
    }
}
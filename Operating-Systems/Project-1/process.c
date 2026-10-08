#include <limits.h>
#include "queue.h"
#include "process.h"
#include <stdio.h>
#include <stdlib.h>


/**
 * Create a new process with the given id and instructions.
 * The process is initialized with the NEW state and a time of 0.
 * 
 * @param id The id of the process.
 * @param instructions The instructions of the process.
 * @param nInstructions The number of instructions.
 * 
 * @return A pointer to the newly created process.
 */
Process* createProcess(int id, int* instructions, int nInstructions){
    Process *process = (Process *) malloc(sizeof(Process));

    process->id = id;
    process->instructions = instructions;
    process->nInstructions = nInstructions;
    process->current_instruction = 0;
    process->state = NEW;
    process->time = 0;
    return process;
}


/**
 * Fetch the next instruction from the process.
 * If there are no more instructions, it returns 0 to indicate HALT.
 * 
 * @param process The process from which to fetch the instruction.
 * 
 * @return The next instruction or 0 if there are no more instructions.
 */
int fetchNextInstruction(Process *process){
    if (process->current_instruction >= process->nInstructions){
        fprintf(stderr, "Error: No more instructions to fetch on process %d. The next instruction will be considered as HALT.\n", process->id);
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
void destroyProcess(Process *process){
    free(process);
}


/**
 * Change the current instruction of the process by a given jump.
 * The jump should always be a positive amount.
 * The jump is always decremented from the current instruction.
 * 
 * @param process The process whose current instruction will be changed.
 * @param jump The postive amount to decrement the current instruction.
 */
void changeCurrentInstruction(Process *process, int jump){
    process->current_instruction = process->current_instruction - jump;
}
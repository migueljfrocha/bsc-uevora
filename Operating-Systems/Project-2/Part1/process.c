#include <stdio.h>
#include <stdlib.h>

#include "process.h"
#include "memory_management.h"


/**
 * Resets the state of all processes to ACTIVE and clears their page table.
 *
 * @param processes The array of processes to reset.
 * @param numProcesses Number of processes in the array.
 */
void resetProcesses(Process *processes, int numProcesses) {
    for (int i = 0; i < numProcesses; i++) {
        processes[i].state = ACTIVE;
        for (int j = 0; j < processes[i].maxPages; j++) {
            processes[i].pageTable[j] = EMPTY_FRAME;
        }
    }
}


/**
 * Initializes the processes based on the input memory sizes.
 * Allocates memory for each process and initializes their properties.
 *
 * @param inputMem Array of integers representing the address space of each process.
 * @param numProcesses Number of processes to initialize.
 * 
 * @return Pointer to an array of initialized Process structures.
 */
Process* initializeProcesses(int* inputMem, int numProcesses) {
    Process *processes = malloc(numProcesses * sizeof(Process));
    if (processes == NULL) {
        fprintf(stderr, "Memory allocation failed for processes.\n");
        exit(EXIT_FAILURE);
    }
    
    for (int i = 0; i < numProcesses; i++) {
        processes[i].processId = i + 1;
        processes[i].addressSpace = inputMem[i];
        processes[i].state = ACTIVE;
        processes[i].maxPages = ((processes[i].addressSpace - 1) / FRAME_SIZE) + 1; // Address space is 0-based
        processes[i].pageTable = malloc(processes[i].maxPages * sizeof(int));
        if (processes[i].pageTable == NULL) {
            fprintf(stderr, "Memory allocation failed for process page table.\n");
            exit(EXIT_FAILURE);
        }
        // Initialize the page table with EMPTY_FRAME values
        for (int j = 0; j < processes[i].maxPages; j++) {
            processes[i].pageTable[j] = EMPTY_FRAME;
        }
    }

    return processes;
}
#ifndef PROCESS_H
#define PROCESS_H

//States of a process
typedef enum State{
    ACTIVE,
    SEGFAULT,
    INACTIVE,
} State;

typedef struct {
    int processId;
    int addressSpace;
    int *pageTable; // Array of frame indices associated with this process
    int maxPages;
    State state;
} Process;

void resetProcesses(Process *processes, int numProcesses);
Process* initializeProcesses(int* inputMem, int numProcesses);

#endif // PROCESS_H
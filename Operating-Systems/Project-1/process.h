#ifndef PROCESS_H
#define PROCESS_H

#include "queue.h"

typedef enum{
    NEW,
    READY,
    RUN,
    BLOCKED,
    EXIT,
    TERMINATED
} State;

// Process structure
typedef struct Process{
    int id;
    State state;
    int current_instruction;
    int *instructions;
    int nInstructions;
    int time;
} Process;


// Process Operations
Process *createProcess(int id, int* instructions, int nInstructions);
int fetchNextInstruction(Process *process); 
void destroyProcess(Process *process);
void changeCurrentInstruction(Process *process, int jump);

#endif /* PROCESS_H */
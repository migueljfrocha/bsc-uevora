#ifndef PROCESS_H
#define PROCESS_H

typedef enum{
    NEW,
    READY,
    RUN,
    BLOCKED,
    EXIT,
    TERMINATED,
    
    SIG_ILL = 100,
    SIG_SEGV = 101,
    SIG_EOF = 102
} State;

// Process structure
typedef struct Process{
    int id;
    State state;
    int current_instruction; 
    int *instructions;
    int nInstructions;
    int time;  // Time for which the process has to stay in the current state
    
    int addressSpace;
    int *pageTable; // Array of frame indices associated with this process
    int maxPages; 
} Process;


// Process Operations
Process *createProcess(int id, int* instructions, int addressSpace, int nInstructions);
int fetchNextInstruction(Process *process); 
void destroyProcess(Process *process);
void changeCurrentInstruction(Process *process, int value);

#endif /* PROCESS_H */
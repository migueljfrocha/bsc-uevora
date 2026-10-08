#ifndef AUXILIARY_H
#define AUXILIARY_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "queue.h"
#include "list.h"
#include "process.h"
#include "scheduler.h"



#define MAX_INSTANTS 100

typedef enum {
    HALT,
    JUMP,
    EXEC,
    IO,
    ANY
} InstructionType;



// Function prototypes
InstructionType getInstructionType(int instruction);
void executeInstruction(Scheduler* scheduler, int instruction);
void destroyProgramList(int** programList, int nPrograms);
void printInfoLabel();
void printCurrentStates(Scheduler* scheduler, int instantTime);


#endif /* AUXILIARY_H */
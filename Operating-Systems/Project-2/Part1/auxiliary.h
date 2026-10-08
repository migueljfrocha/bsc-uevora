#ifndef AUXILIAR_H
#define AUXILIAR_H

#include <stdio.h>

#include "process.h"
#include "memory_management.h"


#define FILENAME_SIZE 32 // Maximum size for filenames
#define SEGFAULT_PRINT "SIGSEGV"

// Methods for page replacement
typedef enum {
    FIFO = 1,
    LRU = 2
} ReplacementMethod;


FILE* createOutputFile(ReplacementMethod replacementMethod, int inputNumber);
void printHeader(FILE *outputFile, int numberOfProcesses);
void formatProcessFrames(char* output, Process *process);
void printInstant(FILE *outputFile, int time, Process *processes, int numberOfProcesses);

#endif // AUXILIAR_H
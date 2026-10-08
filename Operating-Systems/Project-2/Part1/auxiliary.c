#include <string.h>
#include <stdlib.h>

#include "auxiliary.h"


/**
 * Creates an output file based on the specified replacement method and input number.
 * The file is named according to the format "{method}{inputNumber}.out".
 * 
 * @param replacementMethod The replacement method used (FIFO or LRU).
 * @param inputNumber The input number used for naming the output file.
 * 
 * @return A pointer to the opened output file.
 */
FILE* createOutputFile(ReplacementMethod replacementMethod, int inputNumber){
    FILE *outputFile = NULL;
    char filename[FILENAME_SIZE];
    
    if(replacementMethod == FIFO){
        snprintf(filename, FILENAME_SIZE, "Output/fifo%02d.out", inputNumber);
    } else if (replacementMethod == LRU){
        snprintf(filename, FILENAME_SIZE, "Output/lru%02d.out", inputNumber);
    } else {
        fprintf(stderr, "Invalid replacement method specified.\n");
        exit(EXIT_FAILURE);
    }

    outputFile = fopen(filename, "w");
    if (outputFile == NULL) {
        fprintf(stderr, "Error opening output file %s\n", filename);
        exit(EXIT_FAILURE);
    }

    return outputFile;
}


/**
 * Prints the header of the output file, including the time and process labels.
 * 
 * @param outputFile The file pointer to the output file.
 * @param numberOfProcesses The number of processes to include in the header.
 */
void printHeader(FILE *outputFile, int numberOfProcesses) {
    
    fprintf(outputFile, "time inst ");

    char procLabel[20];
    for(int i = 0; i < numberOfProcesses; i++) {
        sprintf(procLabel, "proc%d", i + 1);
        fprintf(outputFile, "%-19s", procLabel);
    }
    fprintf(outputFile, "\n");
}


/**
 * Formats the frames associated with a process into a string.
 * The frames are represented as "F{frame_number}" and separated by commas.
 * 
 * @param output The output string to append the formatted frames to.
 * @param process The process whose frames are to be formatted.
 */
void formatProcessFrames(char* output, Process *process) {

    char frameStr[16];
    for(int j = 0; j < process->maxPages; j++){
        if(process->pageTable[j] == EMPTY_FRAME){
            continue;
        } else {
            if (strlen(output) > 0) 
                strcat(output, ",");
            sprintf(frameStr, "F%d", process->pageTable[j]);
            strcat(output, frameStr);
        }
    }
}


/**
 * Prints the current state of the system at a given time instant.
 * It prints the time, and for each process, it prints either the frames associated with it
 * or a message indicating a segmentation fault (SIGSEGV) if the process is in that state.
 * 
 * @param outputFile The file pointer to the output file.
 * @param time The current time instant.
 * @param processes The array of processes in the system.
 * @param numberOfProcesses The number of processes in the system.
 */
void printInstant(FILE *outputFile, int time, Process *processes, int numberOfProcesses) {

    char output[21] = {0};

    fprintf(outputFile, "%-10d", time);
    for(int i = 0; i < numberOfProcesses; i++){
        
        if(processes[i].state == SEGFAULT){
            fprintf(outputFile, "%-19s", SEGFAULT_PRINT);
            processes[i].state = INACTIVE;

        } else if(processes[i].state == INACTIVE){
            fprintf(outputFile, "%-19s", "");

        } else {
            formatProcessFrames(output, &processes[i]);
            fprintf(outputFile, "%-19s", output);
        }
        
        output[0] = '\0'; // Reset output for the next process
    }
    fprintf(outputFile, "\n");
}
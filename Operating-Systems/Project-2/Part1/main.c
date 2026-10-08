#include <stdio.h>
#include <stdlib.h>

#include "Input/inputs_part1.c"
#include "queue.h"
#include "memory_management.h"
#include "fifo_simulator.h"
#include "lru_simulator.h"
#include "process.h"


int main(int argc, char *argv[]) {

    int *inputP1Mem = NULL, *inputP1Exec = NULL;
    
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <input_number>\n", argv[0]);
        return EXIT_FAILURE;
    }
    int memSize = 0, execSize = 0;
    int inputNumber = atoi(argv[1]);

    
    switch(inputNumber){
        case 0:
            inputP1Mem = inputP1Mem00;
            inputP1Exec = inputP1Exec00;
            memSize = sizeof(inputP1Mem00) / sizeof(inputP1Mem00[0]);
            execSize = sizeof(inputP1Exec00) / sizeof(inputP1Exec00[0]);
            break;
        case 1:
            inputP1Mem = inputP1Mem01;
            inputP1Exec = inputP1Exec01;
            memSize = sizeof(inputP1Mem01) / sizeof(inputP1Mem01[0]);
            execSize = sizeof(inputP1Exec01) / sizeof(inputP1Exec01[0]);
            break;
        case 2:
            inputP1Mem = inputP1Mem02;
            inputP1Exec = inputP1Exec02;
            memSize = sizeof(inputP1Mem02) / sizeof(inputP1Mem02[0]);
            execSize = sizeof(inputP1Exec02) / sizeof(inputP1Exec02[0]);
            break;
        case 3:
            inputP1Mem = inputP1Mem03;
            inputP1Exec = inputP1Exec03;
            memSize = sizeof(inputP1Mem03) / sizeof(inputP1Mem03[0]);
            execSize = sizeof(inputP1Exec03) / sizeof(inputP1Exec03[0]);
            break;
        case 4:
            inputP1Mem = inputP1Mem04;
            inputP1Exec = inputP1Exec04;
            memSize = sizeof(inputP1Mem04) / sizeof(inputP1Mem04[0]);
            execSize = sizeof(inputP1Exec04) / sizeof(inputP1Exec04[0]);
            break;
        case 5:
            inputP1Mem = inputP1Mem05;
            inputP1Exec = inputP1Exec05;
            memSize = sizeof(inputP1Mem05) / sizeof(inputP1Mem05[0]);
            execSize = sizeof(inputP1Exec05) / sizeof(inputP1Exec05[0]);
            break;
        
        /***************************************
         *  ADD YOUR EXTRA INPUTS HERE
        ***************************************/

        default:
            fprintf(stderr, "Invalid input number.\n");
            exit(EXIT_FAILURE);
    }

    Frame *frames = initializeFrames(FRAME_COUNT);
    Process *processes = initializeProcesses(inputP1Mem, memSize);

    simulateFIFO(frames, processes, memSize, execSize, inputP1Exec, inputNumber);

    // Reset frames and processes for LRU simulation
    resetFrames(frames, FRAME_COUNT);
    resetProcesses(processes, memSize);
    
    simulateLRU(frames, processes, memSize, execSize, inputP1Exec, inputNumber);

    // Free allocated memory
    free(frames);
    
    for (int i = 0; i < memSize; i++) {
        free(processes[i].pageTable); 
    }
    
    free(processes);
}
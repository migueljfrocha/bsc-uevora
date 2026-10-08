#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#include "lru_simulator.h"
#include "auxiliary.h"

/**
 * Removes a frame using the Least Recently Used (LRU) algorithm.
 * It finds the frame with the oldest timestamp and removes it.
 * 
 * @param frames The array of frames.
 * 
 * @return Pointer to the removed frame, or NULL if no frame was found.
 */
Frame* removeFrameLRU(Frame* frames){
    Frame* removedFrame = NULL;
    int currentTimestamp, oldestTimestamp = INT_MAX;
    for(int i = 0; i < FRAME_COUNT; i++) {
        currentTimestamp = frames[i].timestamp;
        if (frames[i].processId != EMPTY_FRAME && currentTimestamp < oldestTimestamp) {
            oldestTimestamp = currentTimestamp;
            removedFrame = &frames[i];
        }
    }
    return removedFrame;
}


/**
 * Clears all frames associated with a process that was terminated or had a segmentation fault.
 * This function resets the pages in the process and updates the frames array.
 * 
 * @param process The process whose pages are to be cleared.
 * @param frames The array of frames to clear.
 */
void clearProcessFramesLRU(Process *process, Frame *frames) {
    for(int i = 0; i < process->maxPages; i++){
        int frameIndex = process->pageTable[i];
        if(frameIndex != EMPTY_FRAME){
            // Reset the page in the process
            process->pageTable[i] = EMPTY_FRAME;

            // Reset the frame in the frames array
            frames[frameIndex].processId = EMPTY_FRAME;
            frames[frameIndex].pageNumber = EMPTY_FRAME;
            frames[frameIndex].timestamp = EMPTY_FRAME; 
        }
    }
}


/**
 * Simulates the LRU page replacement algorithm.
 * This function processes a sequence of memory accesses, managing frames and processes according to the LRU strategy.
 * 
 * @param frames The array of frames representing the memory.
 * @param processes The array of processes that may access memory.
 * @param numProcesses The number of processes.
 * @param execSize The size of the execution input array.
 * @param inputExec The array containing the execution inputs (pairs of process ID and address).
 * @param inputNumber The input number used for naming the output file.
 */
void simulateLRU(Frame *frames, Process *processes, int numProcesses, int execSize, int *inputExec, int inputNumber) {

    FILE *outputFile = createOutputFile(LRU, inputNumber);

    int pid, address, index;

    printHeader(outputFile, numProcesses);

    int iterations = execSize / 2; // Each process has a pair of inputs (pid, address)
    for(int instant = 0; instant < iterations; instant++){
        
        index = instant * 2;
        pid = inputExec[index];
        address = inputExec[index + 1];
        Process *currentProcess = &processes[pid - 1];

        // Skip if process is inactive
        if (currentProcess->state != ACTIVE) {
            printInstant(outputFile, instant, processes, numProcesses);
            continue; 
        }
        
        // Check if the address is within the process's address space
        if(address >= currentProcess->addressSpace){
            currentProcess->state = SEGFAULT;
            clearProcessFramesLRU(currentProcess, frames);

        } else {
            
            int pageNumber = address / FRAME_SIZE;
            
            int frameIndex = currentProcess->pageTable[pageNumber];
            if (frameIndex != EMPTY_FRAME) {
                // Page already in memory, update its timestamp and do nothing else
                frames[frameIndex].timestamp = instant;
                printInstant(outputFile, instant, processes, numProcesses);
                continue;
            }

            // Check if there is an empty frame available and update it
            if (findUpdateEmptyFrame(frames, currentProcess, pageNumber, instant) != -1) {
                printInstant(outputFile, instant, processes, numProcesses);
                continue;
            }

            // If no empty frame found, remove a frame using LRU
            Frame *removedFrame = removeFrameLRU(frames);
            if (removedFrame == NULL) {
                fprintf(stderr, "Error: No frame found to remove.\n");
                exit(EXIT_FAILURE);
            }
            
            updateFrameRemove(frames, processes, removedFrame, currentProcess, pageNumber, instant);
        }

        printInstant(outputFile, instant, processes, numProcesses);
    }

    fclose(outputFile);
}
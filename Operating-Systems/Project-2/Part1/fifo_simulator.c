#include <stdio.h>
#include <stdlib.h>

#include "fifo_simulator.h"
#include "auxiliary.h"


/**
 * Clears all frames associated with a process that was terminated or had a segmentation fault.
 * This function removes the frames from the queue and resets the process's page table.
 * 
 * @param process The process whose pages are to be cleared.
 * @param queue The queue from which frames are to be removed.
 * @param frames The array of frames to be updated.
 */
void clearProcessFramesFIFO(Process *process, Queue *queue, Frame *frames) {
    for(int i = 0; i < process->maxPages; i++){
        int frameIndex = process->pageTable[i];
        if(frameIndex != EMPTY_FRAME){
            
            // Remove the frame from the queue
            removeNodeByData(queue, &frames[frameIndex]);

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
 * Simulates the FIFO page replacement algorithm.
 * It processes a sequence of memory accesses, managing frames and processes according to the FIFO strategy.
 * 
 * @param frames The array of frames representing the memory.
 * @param processes The array of processes that may access memory.
 * @param numProcesses The number of processes.
 * @param execSize The size of the execution input array.
 * @param inputExec The array containing the execution inputs (pairs of process ID and address).
 * @param inputNumber The input number used for naming the output file.
 */
void simulateFIFO(Frame *frames, Process *processes, int numProcesses, int execSize, int *inputExec, int inputNumber) {
    
    FILE *outputFile = createOutputFile(FIFO, inputNumber);

    Queue *queue = createQueue();
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
            clearProcessFramesFIFO(currentProcess, queue, frames);

        } else {

            int pageNumber = address / FRAME_SIZE;

            if (currentProcess->pageTable[pageNumber] != EMPTY_FRAME) {
                // Page already in memory, do nothing
                printInstant(outputFile, instant, processes, numProcesses);
                continue;
            }

            // There is an empty frame available
            if (queue->size < FRAME_COUNT) {
                int foundFrame = findUpdateEmptyFrame(frames, currentProcess, pageNumber, instant);

                enqueue(queue, &frames[foundFrame]); 
                printInstant(outputFile, instant, processes, numProcesses);
                continue; 
            }

            // If no empty frame found, we need to replace a frame
            Frame *removedFrame = (Frame*) dequeue(queue);
            if (removedFrame == NULL) {
                fprintf(stderr, "Error: No frame found to remove.\n");
                exit(EXIT_FAILURE);
            }

            updateFrameRemove(frames, processes, removedFrame, currentProcess, pageNumber, instant);
            enqueue(queue, removedFrame); 
            
        }

        printInstant(outputFile, instant, processes, numProcesses);
    }

    deleteQueue(queue);
    fclose(outputFile);
}
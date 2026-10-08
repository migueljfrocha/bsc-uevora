#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#include "scheduler.h"
#include "memory_management.h"


/**
 * Function to initialize the frames array.
 * Allocates memory for the frames and initializes each frame with EMPTY_FRAME values.
 * 
 * @param nFrames The number of frames to initialize.
 * 
 * @return A pointer to the initialized frames array.
 */
Frame* initializeFrames(int nFrames) {

    Frame *frames = malloc(nFrames * sizeof(Frame));
    if (frames == NULL) {
        fprintf(stderr, "Memory allocation failed for frames.\n");
        exit(EXIT_FAILURE);
    }

    for (int i = 0; i < nFrames; i++) {
        frames[i].processId = EMPTY_FRAME;
        frames[i].pageNumber = EMPTY_FRAME;
        frames[i].timestamp = EMPTY_FRAME;
    }

    return frames;
}


/**
 * Find an empty frame in the frames array.
 * 
 * @param frames The array of frames to search in.
 * 
 * @return The index of the first empty frame found, or -1 if no empty frame is found.
 */
int findEmptyFrame(Frame *frames) {

    for(int i = 0; i < FRAME_COUNT; i++){
        if(frames[i].processId == EMPTY_FRAME){
            return i;
        }
    }
    return -1;
}


/**
 * Update the frame and process information when a frame is removed.
 * This function updates the process's page table and the removed frame's information.
 * 
 * @param frames The array of frames.
 * @param processToUpdate The process whose frame is being updated.
 * @param removedFrame The frame that is being removed.
 * @param currentProcess The current process that is being loaded into the frame.
 * @param pageNumber The page number of the current process.
 * @param instant The current time instant.
 * 
 */
void updateFrameRemove (Frame *frames, Process *processToUpdate, Frame *removedFrame, Process *currentProcess, int pageNumber, int instant) {

    int removedPageNumber = removedFrame->pageNumber;

    // Remove the evicted frame from the process
    processToUpdate->pageTable[removedPageNumber] = EMPTY_FRAME;

    // Update the removed frame
    removedFrame->processId = currentProcess->id;
    removedFrame->pageNumber = pageNumber;
    removedFrame->timestamp = instant;

    // Update the current process's page table
    currentProcess->pageTable[pageNumber] = removedFrame - frames; // Pointer arithmetic to get the index of the frame
}


/**
 * Clear the frames associated with a process.
 * This function resets the pages in the process and clears the corresponding frames in the frames array.
 * 
 * @param process The process whose frames are to be cleared.
 * @param frames The array of frames to clear.
 */
void clearProcessFrames(Process *process, Frame *frames) {

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
 * LRU Page Replacement Algorithm.
 * This function handles the page replacement using the Least Recently Used (LRU) algorithm.
 * It checks if the page is already in memory, 
 * If not, finds an empty frame, or removes the least recently used frame.
 * 
 * @param scheduler The scheduler managing the processes and frames.
 * @param process The process that is performing the load/store operation.
 * @param pageNumber The page number corresponding to the address.
 * @param frameIndex The index of the frame associated with the page number in the process.
 */
void LRUPageReplacement(Scheduler *scheduler, Process *process, int pageNumber, int frameIndex) {

    Frame *framesArray = scheduler->framesArray;
    int instantTime = scheduler->instantTime;
    
    if (frameIndex != EMPTY_FRAME) {
        // Page already in memory, update its timestamp
        framesArray[frameIndex].timestamp = instantTime;
        return;
    }
    
    int foundEmptyFrame = findEmptyFrame(framesArray);
    if (foundEmptyFrame != -1) {
        // Found an empty frame, use it for the new page
        framesArray[foundEmptyFrame].processId = process->id;
        framesArray[foundEmptyFrame].pageNumber = pageNumber;
        framesArray[foundEmptyFrame].timestamp = instantTime; 

        // Store the frame index in the process
        process->pageTable[pageNumber] = foundEmptyFrame;
        return;
    }

    // If no empty frame found, we need to remove a frame using LRU
    Frame *removedFrame = NULL;
    int currentTimestamp, oldestTimestamp = INT_MAX;
    
    // Find the frame with the oldest timestamp (least recently used)
    // Goes through all the frames in order
    for(int i = 0; i < FRAME_COUNT; i++) {
        currentTimestamp = framesArray[i].timestamp;
        if (framesArray[i].processId != EMPTY_FRAME && currentTimestamp < oldestTimestamp) {
            oldestTimestamp = currentTimestamp;
            removedFrame = &framesArray[i];
        }
    }

    if (removedFrame == NULL) {
        fprintf(stderr, "Error: No frame found to remove.\n");
        exit(EXIT_FAILURE);
    }

    // Get the process associated with the removed frame
    Process *processToUpdate = scheduler->processes[removedFrame->processId - 1];
    updateFrameRemove(framesArray, processToUpdate, removedFrame, process, pageNumber, instantTime);
}


/**
 * Handle load/store operations for a process.
 * This function checks if the address is valid, finds the corresponding frame, and handles page replacement if necessary.
 * 
 * @param scheduler The scheduler managing the processes and frames.
 * @param process The process that is performing the load/store operation.
 * @param address The memory address being accessed by the process.
 */
void handleLoadStore(Scheduler *scheduler, Process *process, int address) {

    if(address >= process->addressSpace) {
        process->state = SIG_SEGV;
        return;
    }

    int pageNumber = address / FRAME_SIZE;
    int frameIndex = process->pageTable[pageNumber];
    
    LRUPageReplacement(scheduler, process, pageNumber, frameIndex);
}


/**
 * Handle memory copy operations between two addresses.
 * This function checks if the addresses are valid, finds the corresponding frames, and handles page replacement if necessary.
 *
 * @param scheduler The scheduler managing the processes and frames.
 * @param process The process that is performing the memory copy operation.
 * @param addresses The memory addresses being accessed by the process. It is expected to be in the format <address1><address2>.
 */
void swapMemcpyHandle(Scheduler *scheduler, Process *process, int addresses) {

    Frame *framesArray = scheduler->framesArray;
    int instantTime = scheduler->instantTime;

    // Extract the two addresses from the instruction
    int address1 = (addresses / 100000) - 10000;
    int address2 = addresses % 100000;

    // Check if the addresses are within the process's address space
    if(address1 >= process->addressSpace || address2 >= process->addressSpace) {
        process->state = SIG_SEGV;
        return;
    }
    // Calculate the page numbers and frame indices for both addresses
    int pageNumber1 = address1 / FRAME_SIZE;
    int frameIndex1 = process->pageTable[pageNumber1];

    int pageNumber2 = address2 / FRAME_SIZE;
    int frameIndex2 = process->pageTable[pageNumber2];


    if(frameIndex1 == EMPTY_FRAME && frameIndex2 != EMPTY_FRAME) {
        // Page 2 is already in memory, update its timestamp so it is not replaced
        framesArray[frameIndex2].timestamp = instantTime;
    }

    if(pageNumber1 == pageNumber2) {
        // Both addresses refer to the same page so we only need to handle one page
        LRUPageReplacement(scheduler, process, pageNumber1, frameIndex1);
        
    } else{
        LRUPageReplacement(scheduler, process, pageNumber1, frameIndex1); 
        LRUPageReplacement(scheduler, process, pageNumber2, frameIndex2);
    }

}
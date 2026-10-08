#include <stdio.h>
#include <stdlib.h>

#include "memory_management.h"


/**
 * Function to initialize the frames array.
 * Allocates memory for the frames and initializes each frame with EMPTY_FRAME values.
 * 
 * @param size The number of frames to initialize.
 * 
 * @return A pointer to the initialized frames array.
 */
Frame* initializeFrames(int size) {
    Frame *frames = malloc(size * sizeof(Frame));
    if (frames == NULL) {
        fprintf(stderr, "Memory allocation failed for frames.\n");
        exit(EXIT_FAILURE);
    }

    resetFrames(frames, size);

    return frames;
}


/**
 * Reset the frames to their initial state.
 * This function sets all frames to EMPTY_FRAME values.
 * 
 * @param frames The array of frames to reset.
 * @param size The number of frames in the array.
 */
void resetFrames(Frame *frames, int size) {
    for (int i = 0; i < size; i++) {
        frames[i].processId = EMPTY_FRAME;
        frames[i].pageNumber = EMPTY_FRAME;
        frames[i].timestamp = EMPTY_FRAME;
    }
}


/**
 * This function searches for an empty frame, updates it with the process ID and page number,
 * and sets the timestamp for LRU.
 * It also updates the process's page table with the index of the new frame.
 *
 * @param frames The array of frames.
 * @param process The process whose page is being loaded into the frame.
 * @param pageNumber The page number of the current process that is being loaded into memory.
 * @param instantTime The current time instant for LRU timestamping.
 *
 * @return The index of the updated frame, or -1 if no empty frame was found.
 */
int findUpdateEmptyFrame(Frame *frames, Process *process, int pageNumber, int instantTime) {
    for(int i = 0; i < FRAME_COUNT; i++){
        if(frames[i].processId == EMPTY_FRAME){
            // Found an empty frame
            frames[i].processId = process->processId;
            frames[i].pageNumber = pageNumber;
            frames[i].timestamp = instantTime; // Set the timestamp for LRU
            
            // Store the frame index in the process
            process->pageTable[pageNumber] = i;
            return i;
        }
    }
    return -1; // No empty frame found
}


/**
 * Update the frame when a page is removed from memory.
 * This function updates the frame that was removed, setting it to the new process and page number.
 * It also updates the timestamp for LRU and the page table of both old and current processes.  
 *
 * @param frames The array of frames.
 * @param processes The array of processes.
 * @param removedFrame The frame that was removed.
 * @param currentProcess The current process that is being updated.
 * @param pageNumber The page number of the current process.
 * @param instant The current time instant for LRU timestamping.
 */
void updateFrameRemove(Frame *frames, Process *processes, Frame *removedFrame, Process *currentProcess, int pageNumber, int instant) {

    int removedProcessIndex = removedFrame->processId - 1;
    int removedPageNumber = removedFrame->pageNumber;

    // Remove the evicted frame from the process
    processes[removedProcessIndex].pageTable[removedPageNumber] = EMPTY_FRAME;

    // Update the removed frame
    removedFrame->processId = currentProcess->processId;
    removedFrame->pageNumber = pageNumber;
    removedFrame->timestamp = instant; // Update timestamp for LRU

    // Update the current process's page table
    currentProcess->pageTable[pageNumber] = removedFrame - frames; // Pointer arithmetic to get the index of the frame
}
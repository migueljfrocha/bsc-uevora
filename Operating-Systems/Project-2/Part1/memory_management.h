#ifndef MEMORY_MANAGEMENT_H
#define MEMORY_MANAGEMENT_H

#include "memory_management.h"
#include "process.h"

#define MEMORY_SIZE 21000 // 21kB = 21000 bytes
#define FRAME_SIZE 3000 // 3kB = 3000 bytes

#define FRAME_COUNT (MEMORY_SIZE / FRAME_SIZE) // 7 frames of 3kB each
#define EMPTY_FRAME -1 // -1 indicates an empty frame


typedef struct {
    int processId;
    int pageNumber;
    int timestamp; // Used for LRU page replacement algorithm
} Frame;

Frame* initializeFrames(int size);
void resetFrames(Frame *frames, int size);
int findUpdateEmptyFrame(Frame *frames, Process *process, int pageNumber, int instantTime);
void updateFrameRemove(Frame *frames, Process *processes, Frame *removedFrame, Process *currentProcess, int pageNumber, int instant);

#endif // MEMORY_MANAGEMENT_H
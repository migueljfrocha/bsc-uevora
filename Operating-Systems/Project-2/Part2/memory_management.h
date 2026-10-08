#ifndef MEMORY_MANAGEMENT_H
#define MEMORY_MANAGEMENT_H

#include "process.h"

typedef struct Scheduler Scheduler; // Forward declaration

#define MEMORY_SIZE 21000 // 21kB = 21000 bytes
#define FRAME_SIZE 3000 // 3kB = 3000 bytes
#define FRAME_COUNT (MEMORY_SIZE / FRAME_SIZE)
#define EMPTY_FRAME -1 // -1 indicates an empty frame


typedef struct {
    int processId;
    int pageNumber;
    int timestamp;
} Frame;


Frame* initializeFrames(int size);

int findEmptyFrame(Frame *frames);

void updateFrameRemove(Frame *frames, Process *processToUpdate, Frame *removedFrame, Process *currentProcess, int pageNumber, int instant);
void clearProcessFrames(Process *process, Frame *frames);

void LRUPageReplacement(Scheduler *scheduler, Process *process, int pageNumber, int frameIndex);
void handleLoadStore(Scheduler *scheduler, Process *process, int address);
void swapMemcpyHandle(Scheduler *scheduler, Process *process, int addresses);

#endif /* MEMORY_MANAGEMENT_H */
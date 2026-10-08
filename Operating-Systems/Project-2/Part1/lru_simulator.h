#ifndef LRU_SIMULATOR_H
#define LRU_SIMULATOR_H

#include "memory_management.h"
#include "process.h"

Frame* removeFrameLRU(Frame* frames);
void clearProcessFramesLRU(Process *process, Frame *frames);
void simulateLRU(Frame *frames, Process *processes, int numProcesses, int execSize, int *inputExec, int inputNumber);

#endif // LRU_SIMULATOR_H
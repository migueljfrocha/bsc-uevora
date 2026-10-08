#ifndef FIFO_SIMULATOR_H
#define FIFO_SIMULATOR_H

#include "memory_management.h"
#include "queue.h"
#include "process.h"

void clearProcessFramesFIFO(Process *process, Queue *queue, Frame *frames);
void simulateFIFO(Frame *frames, Process *processes, int numProcesses, int execSize, int *inputExec, int inputNumber);

#endif // FIFO_SIMULATOR_H
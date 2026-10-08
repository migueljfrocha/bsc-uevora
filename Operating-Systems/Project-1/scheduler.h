#ifndef SCHEDULER_H
#define SCHEDULER_H

#include "queue.h"
#include "process.h"
#include "list.h"

//User-defined constants
#define MAX_PROCESSES 20
#define RUN_QUANTUM 3
#define NEW_TIME 2
#define EXIT_TIME 1

// Scheduler struct
typedef struct Scheduler{
    List new;
    Queue* ready;
    Process* runningProcess;
    List blocked;
    List exitingProcess;
    int numProcesses;
    Process** processes;
    int** instructions;
    int nInstructionsProgram;
    int nPrograms;
} Scheduler;

// Scheduler Operations
Scheduler* createScheduler(int** instructions, int nPrograms, int nInstructionsProgram);
void schedulerDispatch(Scheduler* scheduler);
void schedulerNew(Scheduler* scheduler, int programNumber);
void schedulerAdmit(Scheduler* scheduler, Process* process);
void schedulerTimeOut(Scheduler* scheduler);
void schedulerBlock(Scheduler* scheduler, int time);
void schedulerUnblock(Scheduler* scheduler, Process* process);
void schedulerRelease(Scheduler* scheduler);
void schedulerTerminate(Scheduler* scheduler, Process* process);
void timeDecrement(Scheduler* scheduler);
void timeDecrementBlocked(Scheduler* scheduler);
void timeDecrementNew(Scheduler* scheduler);
void timeDecrementRunning(Scheduler* scheduler);
void timeDecrementExiting(Scheduler* scheduler);
void destroyScheduler(Scheduler* scheduler);
#endif/* SCHEDULER_H */

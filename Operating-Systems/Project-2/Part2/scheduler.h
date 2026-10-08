#ifndef SCHEDULER_H
#define SCHEDULER_H

#include "queue.h"
#include "process.h"
#include "list.h"
#include "memory_management.h"

#define MAX_PROCESSES 20
#define RUN_QUANTUM 3
#define NEW_TIME 2
#define EXIT_TIME 3

// Scheduler struct
typedef struct Scheduler{
    // State management
    List new;
    Queue* ready;
    Process* runningProcess;
    List blocked;
    List exitingProcess;

    // Simulation management
    int instantTime; // Current time instant in the simulation
    int nActiveProcesses; // Number of active processes in the scheduler
    int numProcesses;
    int nInstructionsProgram;
    int nPrograms;

    // Process management
    Process** processes;
    int** instructions;

    // Memory management
    int* programsAddressSpace;
    Frame* framesArray; 

} Scheduler;

// Scheduler Operations
Scheduler* createScheduler(int** instructions, int* programsAddressSpace, int nPrograms, int nInstructionsProgram);

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
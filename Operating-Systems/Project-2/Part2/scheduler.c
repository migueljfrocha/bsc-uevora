#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>

#include "scheduler.h"

/**
 * Create a new scheduler with the given programs (matrix of instructions).
 * The scheduler is initialized with 0 processes.
 * 
 * @param instructions The matrix of instructions for the programs.
 * @param programsAddressSpace The array of address spaces for each program.
 * @param nPrograms The number of programs.
 * @param nInstructionsProgram The number of instructions per program.
 * 
 * @return A pointer to the newly created scheduler.
 */
Scheduler* createScheduler(int** instructions, int* programsAddressSpace, int nPrograms, int nInstructionsProgram) {
    Scheduler* scheduler = (Scheduler *) malloc(sizeof(Scheduler));

    scheduler->new = createList();
    scheduler->ready = createQueue();
    scheduler->runningProcess = NULL;
    scheduler->blocked = createList();
    scheduler->exitingProcess = createList();

    scheduler->instantTime = 1;
    scheduler->nActiveProcesses = 0; 
    scheduler->numProcesses = 0;
    scheduler->nInstructionsProgram = nInstructionsProgram;
    scheduler->nPrograms = nPrograms;

    scheduler->processes = (Process**) malloc(MAX_PROCESSES * sizeof(Process*));
    if (scheduler->processes == NULL) {
        fprintf(stderr, "Memory allocation failed for processes array.\n");
        exit(EXIT_FAILURE);
    }
    scheduler->instructions = instructions;


    scheduler->programsAddressSpace = programsAddressSpace;
    scheduler->framesArray = initializeFrames(FRAME_COUNT);

    return scheduler;
}


/**
 * Dispatch the next process from the ready queue.
 * If there are no processes in the ready queue, it does nothing.
 * The dispatched process is set to RUN state and its time is set to RUN_QUANTUM.
 * 
 * @param scheduler The scheduler from which to dispatch the process.
 */
void schedulerDispatch(Scheduler* scheduler) {
    if (isEmpty(scheduler->ready)){
        return;
    }
    Process* process = (Process*) dequeue(scheduler->ready);
    process->state = RUN;
    process->time = RUN_QUANTUM;
    scheduler->runningProcess = process;
}


/**
 * Create a new process and add it to the new list.
 * If there are already 20 processes running, it does nothing.
 * If the program number does not exist, it does nothing.
 * 
 * @param scheduler The scheduler to which the new process will be added.
 * @param programNumber The program number from which to create the new process.
 */
void schedulerNew(Scheduler* scheduler, int programNumber) {
    if(scheduler->numProcesses + 1 > MAX_PROCESSES){
        return; 
    }
    if (programNumber > scheduler->nPrograms) {
        fprintf(stderr, "Error: Program number %d does not exist. \n", programNumber);
        return;
    }

    Process* process = createProcess(scheduler->numProcesses + 1, scheduler->instructions[programNumber - 1], scheduler->programsAddressSpace[programNumber - 1], scheduler->nInstructionsProgram);
    scheduler->processes[scheduler->numProcesses] = process;
    scheduler->numProcesses++;
    scheduler->nActiveProcesses++;
    process->time = NEW_TIME;
    insert((void*) process, scheduler->new, scheduler->new);
}


/**
 * Admit a process from the new list to the ready queue.
 * The process state is set to READY and it is added to the ready queue.
 * 
 * @param scheduler The scheduler from which to admit the process.
 * @param process The process to be admitted.
 */
void schedulerAdmit(Scheduler* scheduler, Process* process) {
    delete(process, scheduler->new);
    process->state = READY;
    enqueue(scheduler->ready, (void*) process);
}


/**
 * Timeout the currently running process.
 * The process state is set to READY and it is added back to the ready queue.
 * 
 * @param scheduler The scheduler from which to timeout the process.
 */
void schedulerTimeOut(Scheduler* scheduler) {
    Process* process = scheduler->runningProcess;
    process->state = READY;
    enqueue(scheduler->ready, (void*) process);
    scheduler->runningProcess = NULL;
}


/**
 * Block the currently running process for a the given time.
 * The process state is set to BLOCKED and it is added to the blocked list.
 * 
 * @param scheduler The scheduler from which to block the process.
 * @param time The time for which the process will be blocked.
 */
void schedulerBlock(Scheduler* scheduler, int time) {
    Process* process = scheduler->runningProcess;
    process->state = BLOCKED;
    process->time = time;
    insert((void*) process, scheduler->blocked, scheduler->blocked);
    scheduler->runningProcess = NULL;
}


/**
 * Unblock a process from the blocked list and add it back to the ready queue.
 * The process state is set to READY and it is added to the ready queue.
 * 
 * @param scheduler The scheduler from which to unblock the process.
 * @param process The process to be unblocked.
 */
void schedulerUnblock(Scheduler* scheduler, Process* process) {
    delete(process, scheduler->blocked);
    process->state = READY;
    enqueue(scheduler->ready, (void*) process);
}


/**
 * Release the currently running process and add it to the exiting list.
 * The process state is set to EXIT and its time is set to EXIT_TIME.
 * 
 * @param scheduler The scheduler from which to release the process.
 */
void schedulerRelease(Scheduler* scheduler) {
    Process* process = scheduler->runningProcess;
    process->state = EXIT;
    process->time = EXIT_TIME;
    insert((void*) process, scheduler->exitingProcess, scheduler->exitingProcess);
    scheduler->runningProcess = NULL;
}


/**
 * Terminate a process from the exiting list.
 * The process is deleted from the exiting list and its state is set to TERMINATED.
 * Clear its associated frames from the frames array.
 * 
 * @param scheduler The scheduler from which to terminate the process.
 * @param process The process to be terminated.
 */
void schedulerTerminate(Scheduler* scheduler, Process* process) {
    delete(process, scheduler->exitingProcess);
    clearProcessFrames(process, scheduler->framesArray);
    scheduler->nActiveProcesses--;
    process->state = TERMINATED;
}


/**
 * Decrement the time of all the processes in the blocked list.
 * If the time of a process is less than 0, it is unblocked to the ready queue.
 * 
 * @param scheduler The scheduler from which to decrement the blocked processes' time.
 */
void timeDecrementBlocked(Scheduler* scheduler) {
   Position P = firstList(scheduler->blocked);
   while(P != NULL){
        Process* process = (Process*)retrieveElementList(P);
        Position next = advanceList(P);
        process->time--;
        if (process->time < 0){
            schedulerUnblock(scheduler, process);
        }
        P = next;
    }
}


/**
 * Decrement the time of all the processes in the new list.
 * If the time of a process is 0, it is admitted to the ready queue.
 * 
 * @param scheduler The scheduler from which to decrement the new processes' time.
 */
void timeDecrementNew(Scheduler* scheduler) {
    Position P = firstList(scheduler->new);
    while(P!= NULL){
        Process* process = (Process*) retrieveElementList(P);
        Position next = advanceList(P);
        process->time--;
        if (process->time == 0){
            schedulerAdmit(scheduler, process);
        }
        P = next;
    }
}


/**
 * Decrement the time of the currently running process.
 * If the process is in an error state (SIG_ILL, SIG_SEGV, SIG_EOF), it is released.
 * If the time of the process is 0, it is timed out and added back to the ready queue.
 * 
 * @param scheduler The scheduler from which to decrement the running process' time.
 */
void timeDecrementRunning(Scheduler* scheduler) {
    if (scheduler->runningProcess != NULL){
        scheduler->runningProcess->time--;

        if (scheduler->runningProcess->state >= SIG_ILL) {
            // If the process is in an error state, it is released
            schedulerRelease(scheduler);

        } else if(scheduler->runningProcess->time == 0){
            schedulerTimeOut(scheduler);
        }
    }
}


/**
 * Decrement the time of all the processes in the exiting list.
 * If the time of a process is less than 0, it is terminated.
 * 
 * @param scheduler The scheduler from which to decrement the exiting processes' time.
 */
void timeDecrementExiting(Scheduler* scheduler) {
    Position P = firstList(scheduler->exitingProcess);
    while(P!= NULL){
        Process* process = (Process*) retrieveElementList(P);
        Position next = advanceList(P);
        process->time--;
        if (process->time < 0){
            schedulerTerminate(scheduler, process);
        }
        P = next;
    }
}


/**
 * Decrement the time of all the processes in the scheduler.
 * This includes the blocked, new, running, and exiting processes.
 * 
 * @param scheduler The scheduler from which to decrement the processes' time.
 */
void timeDecrement(Scheduler* scheduler) {
    timeDecrementBlocked(scheduler);
    timeDecrementNew(scheduler);
    timeDecrementRunning(scheduler);
    timeDecrementExiting(scheduler);
}


/**
 * Destroy the scheduler and free all its resources.
 * This includes the new list, ready queue, blocked list, exiting list, and all processes.
 * 
 * @param scheduler The scheduler to be destroyed.
 */
void destroyScheduler(Scheduler* scheduler) {
    
    // Free new list
    deleteList(scheduler->new);
    free(scheduler->new);

    // Free ready queue
    deleteQueue(scheduler->ready);

    // Free blocked list
    deleteList(scheduler->blocked);
    free(scheduler->blocked);

    // Free exiting list
    deleteList(scheduler->exitingProcess);
    free(scheduler->exitingProcess);

    // Free processes
    for (int i = 0; i < scheduler->numProcesses; i++) {
        destroyProcess(scheduler->processes[i]);
    }
    free(scheduler->processes);
    
    // Free programs address space
    free(scheduler->programsAddressSpace);

    // Free frames array
    free(scheduler->framesArray);

    //clear running
    scheduler->runningProcess = NULL;

    // Free scheduler
    free(scheduler);
}
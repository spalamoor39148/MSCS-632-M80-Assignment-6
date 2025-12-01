# Data Processing System — Java & Go

This project implements a multi-threaded **Data Processing System** in both **Java** and **Go**.  
It demonstrates concurrency, synchronization, shared resource management, worker thread processing, and error handling.

The system:
- Uses a shared queue of tasks  
- Spawns multiple worker threads (Java) or goroutines (Go)  
- Processes each task with simulated delays  
- Writes results to output files  
- Logs worker activity and error events  
- Ensures safe shutdown and avoids deadlocks  

---
### Java Implementation

Run

java -cp out Main

Then run:

java -cp out com.example.dataproc.Main

Output File

java-results.txt

### Go Implementation

Run:

go run dataproc.go

Output File:

go-results.txt

## Concurrency Techniques
### Java

ReentrantLock + Condition to guard shared queue

synchronized block for result writes

ExecutorService to manage worker threads

Safe queue closing to avoid deadlocks

try-catch for InterruptedException, IOException, and processing errors

### Go

Goroutines + channels for workers and tasks

Channels used as concurrency-safe queues

sync.WaitGroup for coordinated shutdown

Buffered results channel

Explicit error checking

defer for safe resource cleanup


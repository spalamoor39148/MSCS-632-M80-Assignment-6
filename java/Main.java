package com.example.dataproc;


import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
private static final Logger logger = Logger.getLogger("com.example.dataproc");


public static void main(String[] args) {
// Setup logging
logger.setLevel(Level.INFO);
ConsoleHandler ch = new ConsoleHandler();
ch.setLevel(Level.INFO);
logger.addHandler(ch);


int numWorkers = 4;
SharedQueue<String> queue = new SharedQueue<>();
ResultWriter writer = new ResultWriter();


// Start workers
ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
for (int i = 0; i < numWorkers; i++) {
exec.submit(new Worker(i + 1, queue, writer));
}


// Add tasks
for (int i = 1; i <= 20; i++) {
queue.addTask("task-" + i);
}


// Close queue to indicate no more tasks
queue.close();


// Shut down executor
exec.shutdown();
try {
if (!exec.awaitTermination(30, TimeUnit.SECONDS)) {
exec.shutdownNow();
}
} catch (InterruptedException e) {
exec.shutdownNow();
Thread.currentThread().interrupt();
}


// Write results to file
try {
writer.writeToFile("java-results.txt");
logger.info("Results written to java-results.txt");
} catch (IOException ioe) {
logger.log(Level.SEVERE, "Failed to write results", ioe);
}


logger.info("Main finished");
}
}
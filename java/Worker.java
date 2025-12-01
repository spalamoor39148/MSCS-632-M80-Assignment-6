package com.example.dataproc;


import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Worker implements Runnable {
private static final Logger logger = Logger.getLogger(Worker.class.getName());
private final SharedQueue<String> queue;
private final ResultWriter resultWriter;
private final int id;


public Worker(int id, SharedQueue<String> queue, ResultWriter resultWriter) {
this.id = id;
this.queue = queue;
this.resultWriter = resultWriter;
}


@Override
public void run() {
logger.info(() -> "Worker " + id + " starting");
try {
while (true) {
String task = queue.getTask();
if (task == null) break; // queue closed and empty


// simulate processing
try {
logger.info(() -> "Worker " + id + " processing: " + task);
Thread.sleep(100 + (int)(Math.random() * 400));
String result = "Worker " + id + " processed: " + task + " -> " + task.length();
resultWriter.addResult(result);
} catch (InterruptedException ie) {
logger.log(Level.WARNING, "Worker " + id + " interrupted", ie);
Thread.currentThread().interrupt();
break;
} catch (Exception e) {
// Log and continue with next task
logger.log(Level.SEVERE, "Worker " + id + " error processing task " + task, e);
}
}
} catch (InterruptedException e) {
logger.log(Level.WARNING, "Worker " + id + " interrupted while waiting", e);
Thread.currentThread().interrupt();
}
logger.info(() -> "Worker " + id + " finished");
}
}
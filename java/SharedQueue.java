package com.example.dataproc;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SharedQueue<T> {
private final Queue<T> queue = new LinkedList<>();
private final Lock lock = new ReentrantLock();
private final Condition notEmpty = lock.newCondition();
private boolean closed = false;


public void addTask(T task) {
lock.lock();
try {
if (closed) throw new IllegalStateException("Queue is closed");
queue.add(task);
notEmpty.signal();
} finally {
lock.unlock();
}
}


public T getTask() throws InterruptedException {
lock.lock();
try {
while (queue.isEmpty()) {
if (closed) return null; // no more tasks
notEmpty.await();
}
return queue.poll();
} finally {
lock.unlock();
}
}


public void close() {
lock.lock();
try {
closed = true;
notEmpty.signalAll();
} finally {
lock.unlock();
}
}
}
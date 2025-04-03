package com.shikhar03stark.patterns.producerconsumer;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Consumer implements Runnable{

    private final Queue<Integer> queue;
    private boolean isRunning = true;
    private final Semaphore eventArrivedSemaphore;
    private final Semaphore queueSemaphore;

    public Consumer(Queue<Integer> queue, Semaphore eventArrivedSemaphore, Semaphore queueSemaphore) {
        this.queue = queue;
        this.eventArrivedSemaphore = eventArrivedSemaphore;
        this.queueSemaphore = queueSemaphore;
    }

    private void processEvent(int value) {
        // Simulate processing the event (e.g., consuming the value)
        System.out.println("Thread" + Thread.currentThread().getId() + " Consumed: " + value);
        try {
            Thread.sleep(1000); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            // wait until an event arrived
            try {
                
                eventArrivedSemaphore.acquire();

                queueSemaphore.acquire();
                if (!queue.isEmpty()) {
                    int value = queue.poll(); // Remove and get the value from the queue
                    queueSemaphore.release(); // Release the queue semaphore after consuming the value
                    processEvent(value); // Process the event
                } else {
                    queueSemaphore.release(); // Release the queue semaphore if the queue is empty
                }

            } catch (InterruptedException e) {
                System.out.println("Consumer interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }
        }
    }

    public void stop() {
        isRunning = false;
    }

}

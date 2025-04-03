package com.shikhar03stark.patterns.producerconsumer;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer implements Runnable {

    private final Queue<Integer> queue;
    private final Random random = new Random();
    private boolean isRunning = true;

    private final Semaphore eventArrivedSemaphore;
    private final Semaphore queueSemaphore;

    public Producer(Queue<Integer> queue, Semaphore eventArrivedSemaphore, Semaphore queueSemaphore) {
        this.queue = queue;
        this.eventArrivedSemaphore = eventArrivedSemaphore;
        this.queueSemaphore = queueSemaphore;
    }

    private int waitForEvent() {
        // Simulate waiting for an event (e.g., a user action or a timer)
        try {
            Thread.sleep(400); // Simulate waiting time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        int value = random.nextInt(100); // Generate a random number as an event
        System.out.println("Thread" + Thread.currentThread().getId() + " Produced: " + value);
        return value;
    }

    @Override
    public void run() {
        while (isRunning) {
            int value = waitForEvent(); // Wait for an event and get the value
            // Add the value to the queue
            try {
                queueSemaphore.acquire();

                queue.add(value);

                queueSemaphore.release();
                eventArrivedSemaphore.release();

            } catch (InterruptedException e) {
                System.out.println("Producer interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }

        }
    }

    public void stop() {
        isRunning = false;
    }

}

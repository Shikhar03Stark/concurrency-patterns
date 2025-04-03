package com.shikhar03stark.patterns.producerconsumer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;

public class ProducerConsumerTests {

    private final int NUM_PRODUCERS = 3;
    private final int NUM_CONSUMERS = 6;

    @Test
    public void ProducerConsumerTest() {

        // Create a queue to hold items
        final Queue<Integer> queue = new LinkedList<>();

        // Create semaphores for eventArrival and mutex of queue poll and add
        final Semaphore eventArrivedSemaphore = new Semaphore(0);
        final Semaphore queueSemaphore = new Semaphore(1);

        final ExecutorService executor = Executors.newFixedThreadPool(8);

        final List<Producer> producers = new ArrayList<>();
        final List<Consumer> consumers = new ArrayList<>();

        for(int i = 0; i < NUM_PRODUCERS; i++) {
            producers.add(new Producer(queue, eventArrivedSemaphore, queueSemaphore));
        }

        for(int i = 0; i < NUM_CONSUMERS; i++) {
            consumers.add(new Consumer(queue, eventArrivedSemaphore, queueSemaphore));
        }

        for(Runnable producer : producers) {
            executor.execute(producer);
        }
        for(Runnable consumer : consumers) {
            executor.execute(consumer);
        }

        // Wait for a while to let the producers and consumers run
        try {
            Thread.sleep(10000); // Adjust the sleep time as needed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        // Stop the producers and consumers
        for(Producer producer : producers) {
            producer.stop();
        }

        for(Consumer consumer : consumers) {
            consumer.stop();
        }

        executor.shutdown(); // Shutdown the executor service
    }
}

import java.util.concurrent.atomic.AtomicInteger;

class Counter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.getAndIncrement();  // Atomically increment the count
        System.out.println("Incremented: " + count.get());
    }


    public void decrement() {
        count.getAndDecrement();  // Atomically decrement the count
        System.out.println("Decremented: " + count.get());
    }

    public int getCount() {
        return count.get();
    }
}

public class AtomicVariableExample {
    public static void main(String[] args) {
        Counter counter = new Counter();  // Create a shared counter object

        // Create a thread to increment the counter
        Thread incrementThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.increment();
                try {
                    Thread.sleep(100);  // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Create a thread to decrement the counter
        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.decrement();
                try {
                    Thread.sleep(150);  // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Start both threads
        incrementThread.start();
        decrementThread.start();

        // Wait for both threads to finish
        try {
            incrementThread.join();
            decrementThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Display the final value of the counter
        System.out.println("Final Counter Value: " + counter.getCount());
    }
}

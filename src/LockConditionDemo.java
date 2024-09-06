import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SharedBuffer {
    private int item = -1;
    private boolean isEmpty = true;  // Buffer status flag
    private final Lock lock = new ReentrantLock();  // Lock for synchronization
    private final Condition notEmpty = lock.newCondition();  // Condition for buffer not empty
    private final Condition notFull = lock.newCondition();   // Condition for buffer not full

    // Producer method
    public void produce(int newItem) throws InterruptedException {
        lock.lock();  // Acquire the lock
        try {
            while (!isEmpty) {  // Wait if the buffer is full
                notFull.await();  // Wait for the consumer to consume the item
            }
            item = newItem;  // Produce the item
            System.out.println("Produced: " + item);
            isEmpty = false;  // Mark buffer as full
            notEmpty.signal();  // Signal the consumer that the buffer is not empty
        } finally {
            lock.unlock();  // Release the lock
        }
    }

    // Consumer method
    public int consume() throws InterruptedException {
        lock.lock();  // Acquire the lock
        try {
            while (isEmpty) {  // Wait if the buffer is empty
                notEmpty.await();  // Wait for the producer to produce an item
            }
            System.out.println("Consumed: " + item);
            isEmpty = true;  // Mark buffer as empty
            notFull.signal();  // Signal the producer that the buffer is not full
            return item;
        } finally {
            lock.unlock();  // Release the lock
        }
    }
}

public class LockConditionDemo {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();  // Create the shared buffer

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.produce(i);  // Produce items 1 to 5
                    Thread.sleep(500);  // Simulate production time
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.consume();  // Consume items
                    Thread.sleep(800);  // Simulate consumption time
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start both threads
        producer.start();
        consumer.start();
    }
}

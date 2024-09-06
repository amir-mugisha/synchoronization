class Resource1 {
    private final String name;

    public Resource1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public synchronized void lock() {
        System.out.println(Thread.currentThread().getName() + " locked " + name);
    }
}

public class DeadlockResolvedExample {
    public static void main(String[] args) {
        Resource1 resource1 = new Resource1("Resource-1");
        Resource1 resource2 = new Resource1("Resource-2");

        Thread t1 = new Thread(() -> {
            lockResourcesInOrder(resource1, resource2);
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            lockResourcesInOrder(resource1, resource2);  // Locks in the same order
        }, "Thread-2");

        t1.start();
        t2.start();
    }

    // Acquires locks in a consistent order
    public static void lockResourcesInOrder(Resource1 r1, Resource1 r2) {
        synchronized (r1){
            System.out.println(Thread.currentThread().getName() + " locked " + r1.getName());
            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            synchronized (r2) {
                System.out.println(Thread.currentThread().getName() + " locked " + r2.getName());
            }
        }
    }
}

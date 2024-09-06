public class DeadlockExample {
    public static void main(String[] args) {
        Resource1 resource1 = new Resource1("Resource-1");
        Resource1 resource2 = new Resource1("Resource-2");

        Thread t1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println(Thread.currentThread().getName() + " locked " + resource1.getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                synchronized (resource2) {
                    System.out.println(Thread.currentThread().getName() + " locked " + resource2.getName());
                }
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println(Thread.currentThread().getName() + " locked " + resource2.getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                synchronized (resource1) {
                    System.out.println(Thread.currentThread().getName() + " locked " + resource1.getName());
                }
            }
        }, "Thread-2");

        t1.start();
        t2.start();
    }
}

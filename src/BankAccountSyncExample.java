class BankAccount {
    private int balance = 0;

    // Synchronized method for deposit
    public synchronized void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " deposited " + amount + ". New balance: " + balance);
        }
    }

    // Synchronized block for withdraw
    public void withdraw(int amount) {
        synchronized (this) {
            if (amount > 0 && balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " withdrew " + amount + ". New balance: " + balance);
            } else {
                System.out.println(Thread.currentThread().getName() + " attempted to withdraw " + amount + " but failed. Current balance: " + balance);
            }
        }
    }

    public int getBalance() {
        return balance;
    }
}

public class BankAccountSyncExample {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();

        // Create two threads for depositing and withdrawing money
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                account.deposit(100);
                account.withdraw(50);
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                account.deposit(200);
                account.withdraw(150);
            }
        }, "Thread-2");

        // Start both threads
        t1.start();
        t2.start();

        // Wait for both threads to finish
        t1.join();
        t2.join();

        // Final balance after all transactions
        System.out.println("Final Balance: " + account.getBalance());
    }
}

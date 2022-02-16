package question3;

import java.util.concurrent.Semaphore;

public class CheckingAccountSemaphor {

    private int balance;
    private final Semaphore semaphore;

    public CheckingAccountSemaphor(int initialBalance, Semaphore semaphore) {

        balance = initialBalance;
        this.semaphore = semaphore;
    }

    public boolean withdraw(int amount) {
        if (amount > balance)
            return false;

        acquire();
        if (amount <= balance) {
            try {
                Thread.sleep((int) (Math.random() * 200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            balance -= amount;
            release();
            return true;
        }
        release();
        return false;
    }

    private void release() {
        semaphore.release();
    }

    private void acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final CheckingAccountSemaphor ca = new CheckingAccountSemaphor(100, new Semaphore(1));
        Runnable r = new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                for (int i = 0; i < 10; i++)
                    System.out.println(name + " withdraws $10: " +
                            ca.withdraw(10));
            }
        };


        Thread thdHusband = new Thread(r);
        thdHusband.setName("Husband");
        Thread thdWife = new Thread(r);
        thdWife.setName("Wife");
        thdHusband.start();
        thdWife.start();

    }
}

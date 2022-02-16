package question5;

public class DeadLock {

    private Integer num1 = 10;
    private Integer num2 = 20;

    public static void main(String[] args) throws InterruptedException {
        DeadLock deadLock = new DeadLock();
        Thread thread1 = new Thread(() -> deadLock.add(20), "Thread 1");
        Thread thread2 = new Thread(() -> deadLock.reduce(5), "Thread 2");
        thread1.start();
        thread2.start();

    }

    void add(int i) {
        synchronized (num1) {
            print(Thread.currentThread(), " : Holding lock 1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            print(Thread.currentThread(), " : Waiting for lock 1");

            synchronized (num2) {
            print(Thread.currentThread(), " : Holding lock 2");
                num1 += i;
                num2 += i;
            }
        }
    }

    void reduce(int i) {
        synchronized (num2) {
            print(Thread.currentThread(), " : Holding lock 2");
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
            print(Thread.currentThread(), " : Waiting for lock 1");

            synchronized (num1) {
            print(Thread.currentThread(), " : Holding lock 1");
                num1 -= i;
                num2 -= i;
            }
        }
    }

    void print(Thread thread, String message) {
        System.out.println(thread.getName() + message);
    }
}

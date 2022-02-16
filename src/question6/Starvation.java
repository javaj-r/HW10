package question6;

/**
 * Starvation describes a situation where a thread is unable to gain regular access to shared resources
 * and is unable to make progress.
 * This happens when shared resources are made unavailable for long periods by "greedy" threads.
 * For example, suppose an object provides a synchronized method that often takes a long time to return.
 * If one thread invokes this method frequently, other threads that also need frequent synchronized access
 * to the same object will often be blocked.
 */
public class Starvation {

    public static void main(String[] args) {

        final Starvation starvation = new Starvation();

        Runnable runnable = () -> {
            for (; ; ) {
                try {
                    starvation.lock(Thread.currentThread().getName());
                } catch (InterruptedException ignored) {
                }
            }
        };

        Thread thread1 = new Thread(runnable, "Thread 1");
        Thread thread2 = new Thread(runnable, "Thread 2");
        Thread thread3 = new Thread(runnable, "Thread 3");

        thread1.start();
        thread2.start();
        thread3.start();
    }

    public void lock(String name) throws InterruptedException {
        synchronized (this) {
            System.out.println(name + " : is lucking.");
            Thread.sleep(1000);
        }
    }
}

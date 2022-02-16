package question1;

public class Main {

    public static void main(String[] args) {

        Thread greetingThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    System.out.println("Hello");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                    Thread.currentThread().interrupt();
                }
            }
        });

        greetingThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        greetingThread.interrupt();
    }

}

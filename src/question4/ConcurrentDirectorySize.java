package question4;


import java.io.File;
import java.util.InputMismatchException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentDirectorySize {

    private static Scanner scanner = new Scanner(System.in);
    private final Queue<File> fileQueue = new ConcurrentLinkedQueue<>();
    private AtomicLong size = new AtomicLong(0);
    private AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        File file = getFile();
        ConcurrentDirectorySize directorySize = new ConcurrentDirectorySize();

        if (file.isDirectory()) {
            int threadNumber = getThreadNumber();
            directorySize.fileQueue.add(file);
            Runnable runnable = getRunnable(directorySize);
            long startTime = System.nanoTime();
            for (int i = 0; i < threadNumber; i++) {
                new Thread(runnable).start();
            }
            waitForThreads(directorySize, threadNumber);

            System.out.println("Time : " + (System.nanoTime() - startTime));
        } else if (file.isFile()) {
            directorySize.size.addAndGet(file.length());
        }

        printSize(directorySize.size.get());
    }

    private static void waitForThreads(ConcurrentDirectorySize directorySize, int threadNumber) {
        while (directorySize.counter.get() < threadNumber) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printSize(long size) {
        System.out.println("Size: " + size + "L B");
        System.out.println("\t " + (double) size / 1024 + " KB");
        System.out.println("\t " + +(double) size / (1024 * 1024) + " MB");
        System.out.println("\t " + +(double) size / (1024 * 1024 * 1024) + " GB");
    }

    private static File getFile() {
        while (true) {
            System.out.print("Folder path: ");
            String stringPath = scanner.nextLine().trim();
            File file = new File(stringPath);
            if (file.exists()) {
                return file;
            }
            System.out.println("Failed to find " + stringPath);
        }
    }

    public static int getThreadNumber() {
        System.out.print("Thread number: ");
        while (true) {
            try {
                int i = scanner.nextInt();
                scanner.nextLine();
                return i;
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Invalid number");
            }
            scanner.nextLine();
        }
    }

    private static Runnable getRunnable(ConcurrentDirectorySize directorySize) {
        return new CalculateDirectorySize(directorySize);
    }

    private record CalculateDirectorySize(ConcurrentDirectorySize directorySize) implements Runnable {
        @Override
        public void run() {
            while (true) {
                File folder = directorySize.fileQueue.poll();
                if (folder != null) {
                    File[] files = folder.listFiles();
                    int count = files == null ? 0 : files.length;

                    for (int i = 0; i < count; i++) {
                        if (files[i].isFile()) {
                            long length = files[i].length();
                            directorySize.size.addAndGet(length);
                        } else {
                            directorySize.fileQueue.add(files[i]);
                        }
                    }
                } else {
                    break;
                }
            }
            directorySize.counter.incrementAndGet();
        }
    }

}

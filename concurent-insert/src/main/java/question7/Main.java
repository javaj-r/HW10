package question7;

import question7.database.DatabaseConnection;
import question7.model.Entity;
import question7.repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Main {


    public static void main(String[] args) {

        final Set<Entity> dataSet = new HashSet<>();

        long timeStamp = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            dataSet.add(new Entity("Content " + i + " \tTime: " + timeStamp));
        }

        Repository repository = new Repository(new DatabaseConnection(4));

        executeTask(dataSet, repository);

        System.out.println("Don!");
    }


    private static class Task implements Runnable {

        final Repository repository;
        final CountDownLatch latch;
        final Entity entity;


        public Task(Repository repository, CountDownLatch latch, Entity entity) {
            this.repository = repository;
            this.latch = latch;
            this.entity = entity;
        }

        @Override
        public void run() {
            repository.save(entity);
            latch.countDown();
        }
    }


    private static void executeTask(Set<Entity> dataSet, Repository repository) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        try {
            CountDownLatch latch = new CountDownLatch(dataSet.size());

            dataSet.forEach(entity -> service.submit(new Task(repository, latch, entity)));

            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
    }


}

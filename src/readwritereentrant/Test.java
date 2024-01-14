package readwritereentrant;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {
    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("readWrite.txt");

            Thread t1 = new Thread(new FileWriterReader(readWriteLock, fileWriter));
            Thread t2 = new Thread(new FileWriterReader(readWriteLock, fileWriter));
            Thread t3 = new Thread(new FileWriterWriter(readWriteLock, fileWriter));
            Thread t4 = new Thread(new FileWriterWriter(readWriteLock, fileWriter));

            t1.start();
            t2.start();
            t3.start();
            t4.start();

            t1.join();
            t2.join();
            t3.join();
            t4.join();

            fileWriter.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class FileWriterReader implements Runnable {
        private final ReadWriteLock readWriteLock;
        private final FileWriter fileWriter;
        public FileWriterReader(ReadWriteLock readWriteLock, FileWriter fileWriter) {
            this.readWriteLock = readWriteLock;
            this.fileWriter = fileWriter;
        }

        @Override
        public void run() {
            readWriteLock.readLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + " has acquired the read lock.");

                // Reading from the file
                System.out.println(Thread.currentThread().getName() + " is reading from the file.");
            } finally {
                // Releasing the read lock in a final block to ensure it gets released
                readWriteLock.readLock().unlock();
                System.out.println(Thread.currentThread().getName() + " is releasing the read lock.");
            }
        }
    }

    private static class FileWriterWriter implements Runnable {
        private final ReadWriteLock readWriteLock;
        private final FileWriter fileWriter;
        public FileWriterWriter(ReadWriteLock readWriteLock, FileWriter fileWriter) {
            this.readWriteLock = readWriteLock;
            this.fileWriter = fileWriter;
        }

        @Override
        public void run() {
            readWriteLock.writeLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + " has acquired the write lock");

                fileWriter.write(Thread.currentThread().getName() + " is writing to the file\n");
                Thread.sleep(2000);

                System.out.println(Thread.currentThread().getName() + " is releasing the write lock");
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                readWriteLock.writeLock().unlock();

            }
        }
    }
}

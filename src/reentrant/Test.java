package reentrant;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    public static void main(String[] args) {
//        Lock lock = new ReentrantLock(false);
        Lock lock = new ReentrantLock();
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter("output.txt");
            Thread t1 = new Thread(new FileWriterWorker(lock, fileWriter));
            Thread t2 = new Thread(new FileWriterWorker(lock, fileWriter));
            t1.start();
            t2.start();

            t1.join();
            t2.join();


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

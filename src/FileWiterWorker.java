import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class FileWiterWorker implements Runnable {
    private final Lock lock;
    private final FileWriter fileWriter;
    public FileWiterWorker(Lock lock, FileWriter fileWriter) {
        this.lock = lock;
        this.fileWriter = fileWriter;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            fileWriter.write(Thread.currentThread().getName()+ ", is writing to the file..\n");
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }
}

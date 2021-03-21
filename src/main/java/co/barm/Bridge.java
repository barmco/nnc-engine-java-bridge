package co.barm;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class Bridge {
    // These fields refer from nnc-engine

    private static final Semaphore sem = new Semaphore(1);
    private static final ArrayList<EngineBoundQueueItem> queue = new ArrayList<>();

    // Term indicates to the engine should stop polling and exit gracefully.
    private static boolean term = false;

    // Own indicates semaphore is owned by java side currently.
    private static boolean own = false;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> term = true));
    }

    static public class EngineBoundQueueItem {
        public String subject;
        public Object[] arguments;
    }

    public static void main(String[] args) throws InterruptedException {
        acquire();
        enqueue("test", 123, 1.3, "string", new Date());
        release();
        markTerm();
    }

    public static void enqueue(EngineBoundQueueItem item) {
        if(!own) {
            throw new IllegalStateException("enqueue: you can't enqueue until grab the semaphore");
        }
        queue.add(item);
    }

    public static void enqueue(String subject, Object ...arguments) {
        EngineBoundQueueItem item = new EngineBoundQueueItem();
        item.subject = subject;
        item.arguments = arguments;
        enqueue(item);
    }

    public static void acquire() throws InterruptedException {
        sem.acquire();
        own = true;
    }

    public static void release() {
        own = false;
        sem.release();
    }

    public static void markTerm() {
        try {
            sem.acquire();
            term = true;
            sem.release();

        } catch(InterruptedException ignored) {}
    }
}

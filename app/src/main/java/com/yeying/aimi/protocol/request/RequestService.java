/**
 *
 */
package com.yeying.aimi.protocol.request;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * @author sparrow
 */
public class RequestService implements Runnable {
    private static final String TAG = RequestService.class.getSimpleName();
    private static final Object obj = new Object();
    private static RequestService instance = null;
    private final ExecutorService pool;
    private final BlockingQueue<Runnable> queue;
    private final Queue<Runnable> currentExecute = new ConcurrentLinkedQueue<Runnable>();

    private RequestService(int poolSize) {
        queue = new LinkedBlockingQueue<Runnable>(1000);
        pool = Executors.newFixedThreadPool(poolSize);
        /* Thread t=new Thread(this);
		 t.run();*/
    }

    public static RequestService getInstance() {
        synchronized (obj) {
            if (instance == null) {
                instance = new RequestService(1);
                synchronized (instance) {
                    Thread t = new Thread(instance);
                    t.start();
                }
            }
        }
        return instance;

    }

    //	 public static long task_num=0;
    public synchronized void addTask(Runnable r) {
        try {
            queue.put(r);
//				task_num++;
//				Tools.e("queue size: " + queue.size());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // private final Object obj=new Object();
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            for (; ; ) {
                //queue.t
                Runnable r = queue.take();
                // Future f=pool.submit(r);
                // if(f.get)
                pool.execute(r);
                //  Thread.sleep(5);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
//		     pool.shutdown();
        }
    }

    public void shutdownAndAwaitTermination() {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    Log.d(this.getClass().getName(), "Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


}

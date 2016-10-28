import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

import java.text.SimpleDateFormat;
import java.util.concurrent.*;

/**
 * Created by 1115 on 2016/10/28.
 */
public class TestMain {
    static volatile RateLimiter rateLimiter = RateLimiter.create(3.0);
    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 6, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        for(int i=0;i<10;i++){
            rateLimiter.acquire();
             pool.submit(new ThreadMain(i+""));
        }
    }
    static class ThreadMain implements Runnable{
        //userkey-service limiter
        private static final ConcurrentMap<String, RateLimiter> userresourceLimiterMap = Maps.newConcurrentMap();
        private String threadName ;
        ThreadMain(String threadName){
            this.threadName = threadName;

        }
        @Override
        public void run() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+threadName+". start:"+dateString);
            //rateLimiter.acquire();
            dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+threadName+". end:"+dateString);
            System.out.println();
        }
    }
}

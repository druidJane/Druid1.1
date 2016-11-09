import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by 1115 on 2016/10/28.
 */
public class TestThread implements Runnable{
    private String threadName ;
    TestThread(String threadName){
        this.threadName = threadName;
    }
    volatile RateLimiter rateLimiter = RateLimiter.create(5);;
    @Override
    public void run() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+threadName+". start:"+dateString);
            rateLimiter.acquire();
            dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+threadName+". end:"+dateString);
        System.out.println();
    }
}

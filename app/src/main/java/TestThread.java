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
    RateLimiter rateLimiter ;
    @Override
    public void run() {
        rateLimiter = RateLimiter.create(5);
        //while(true){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+threadName+". start:"+dateString);
            //rateLimiter.acquire(5);
            dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+threadName+". end:"+dateString);

           /* rateLimiter.acquire(4);
        dateString = formatter.format(System.currentTimeMillis());
        System.out.println("Thread:"+threadName+". acquire 4:"+dateString);*/
        System.out.println();
        //}
    }
}

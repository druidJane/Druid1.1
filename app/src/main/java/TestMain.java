import java.text.SimpleDateFormat;

/**
 * Created by 1115 on 2016/10/28.
 */
public class TestMain {
    static volatile RateLimiter rateLimiter = RateLimiter.create(20);
    public static void main(String[] args) throws InterruptedException {
        /*ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 16, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());*/
        //TestThread test = new TestThread("3");
        for(int i=1;;i++){
            if(i%50==0){
                long sleep = 9000;
                System.out.println("sleep start!"+sleep);
                Thread.sleep(sleep);
                System.out.println("sleep end!");
            }
            rateLimiter.acquire();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+i+". start:"+dateString);
            dateString = formatter.format(System.currentTimeMillis());
            System.out.println("Thread:"+i+". end:"+dateString);
            //pool.submit(test);
            System.out.println("");
        }
    }
}

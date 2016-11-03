import com.cn.common.core.model.entity.Player;
import com.cn.server.Server;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by 1115 on 2016/10/31.
 */
public class TestRedis {
    private static RedisTemplate<String,Integer> bean;
    static volatile AtomicInteger count;
    static RateLimiter rateLimiter = RateLimiter.create(50, 1, TimeUnit.SECONDS);
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 25, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        bean = applicationContext.getBean(RedisTemplate.class);
        Server server = applicationContext.getBean(Server.class);
        /*for(int i=0;i<20;i++){
            pool.submit(new TestRedisThread(bean,i+""));
        }*/
        for(int i=0;i<20;i++){
            Thread thread = new TestRedisThread(applicationContext,bean, i + "");
            thread.start();
            //thread.join();
        }
        System.out.println(bean.boundValueOps("count").get());
    }
}

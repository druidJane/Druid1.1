import com.cn.server.TestRedisServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by 1115 on 2016/10/31.
 */
public class TestRedisThread  extends Thread {
    private ApplicationContext ctx;
    private static RedisTemplate<String,Integer> bean;
    volatile Integer count2 = 0;
    Integer count;
    TestRedisThread(ApplicationContext ctx,RedisTemplate<String,Integer> bean,String name){
        this.ctx = ctx;
        this.bean = bean;
        this.name=name;

    }
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    Lock lock = new ReentrantLock();// 锁
    private String name;
    @Override
    public void run() {
        TestRedisServiceImpl impl = ctx.getBean(TestRedisServiceImpl.class);
        impl.readAndWrite(bean);
    }
}

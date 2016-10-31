import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by 1115 on 2016/10/31.
 */
public class TestRedisThread  extends Thread {
    private static RedisTemplate<String,Integer> bean;
    volatile Integer count2 = 0;
    Integer count;
    TestRedisThread(RedisTemplate<String,Integer> bean,String name){
        this.bean = bean;
        this.name=name;

    }
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    Lock lock = new ReentrantLock();// 锁
    private String name;
    @Override
    public void run() {
            try {
                lock.lock();
                //rwlock.writeLock().lock();
               // rwlock.readLock().lock();
                count = bean.boundValueOps("count").get();
                System.out.println(this.name+"in count="+count);
                bean.boundValueOps("count").set(++count);
                count = bean.boundValueOps("count").get();
                System.out.println(this.name+"out count="+count);
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
                //rwlock.writeLock().unlock();
                //rwlock.readLock().unlock();

            }
    }
}

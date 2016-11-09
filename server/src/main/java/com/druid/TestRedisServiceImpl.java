package com.druid;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service("service")
public class TestRedisServiceImpl {
    RateLimiter rateLimiter = RateLimiter.create(5, 1, TimeUnit.SECONDS);
    Lock lock = new ReentrantLock();// 锁
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    public void readAndWrite(RedisTemplate<String,Integer> bean){
        //rateLimiter.acquire();
        //rwLock.writeLock().lock();
        Integer count = bean.boundValueOps("count").get();
        System.out.println(Thread.currentThread().getName()+":in:"+count);
        count++;
        bean.boundValueOps("count").set(count);
        System.out.println(Thread.currentThread().getName()+":out:"+count);
        //rwLock.writeLock().unlock();
    }
}

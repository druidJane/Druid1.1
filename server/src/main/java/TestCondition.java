import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 1115 on 2016/11/3.
 */
public class TestCondition {
    private static ReentrantLock lock = new ReentrantLock();
    volatile private static int nextPrintWho = 1;
    final private static Condition conditionA = lock.newCondition();
    final private static Condition conditionB = lock.newCondition();
    final private static Condition conditionC = lock.newCondition();
    public static void main(String[] args) {
        Thread threadA = new Thread(){
            @Override
            public void run() {
                try {
                    lock.lock();
                    while(nextPrintWho!=1){
                        conditionA.await();
                    }
                    for(int i=0;i<3;i++){
                        System.out.println("Thread A :"+i);
                    }
                    nextPrintWho=2;
                    conditionB.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread threadB = new Thread(){
            @Override
            public void run() {
                try {
                    lock.lock();
                    while(nextPrintWho!=2){
                        conditionB.await();
                    }
                    for(int i=0;i<3;i++){
                        System.out.println("Thread B :"+i);
                    }
                    nextPrintWho=3;
                    conditionC.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread threadC = new Thread(){
            @Override
            public void run() {
                try {
                    lock.lock();
                    while(nextPrintWho!=3){
                        conditionC.await();
                    }
                    for(int i=0;i<3;i++){
                        System.out.println("Thread C :"+i);
                    }
                    nextPrintWho=1;
                    conditionA.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread[] a=new Thread[5];
        Thread[] b=new Thread[5];
        Thread[] c=new Thread[5];
        for(int i=0;i<5;i++){
            a[i]=new Thread(threadA);
            b[i]=new Thread(threadB);
            c[i]=new Thread(threadC);
            a[i].start();
            b[i].start();
            c[i].start();
        }
    }
}

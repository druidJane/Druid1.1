import java.text.SimpleDateFormat;

/**
 * Created by 1115 on 2016/11/3.
 * 有序打印
 */
public class MyThread extends Thread{
    volatile private static int addNum =0;
    private String showChar;
    private Object lock;
    private int numPostion;
    public MyThread(Object lock, String showChar, int numPostion){
        this.showChar=showChar;
        this.lock=lock;
        this.numPostion= numPostion;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                while(true){
                    if(addNum%3==numPostion){
                        System.out.println("Thread:"+showChar);
                        addNum++;
                        lock.notifyAll();
                    }else{
                        lock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }
}

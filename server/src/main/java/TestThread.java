import org.omg.CORBA.ObjectHelper;

/**
 * Created by 1115 on 2016/11/3.
 */
public class TestThread {
    public static void main(String[] args) {
        Object lock = new Object();
        MyThread threadA = new MyThread(lock,"A",0);
        MyThread threadB = new MyThread(lock,"B",1);
        MyThread threadC = new MyThread(lock,"C",2);
        threadA.start();
        threadB.start();
        threadC.start();
    }
}

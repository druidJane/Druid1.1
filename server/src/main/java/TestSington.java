/**
 * Created by 1115 on 2016/11/3.
 */
public class TestSington {
    private volatile static TestSington testSington;
    private TestSington(){
    }
    //使用双检测机制来解决问题，既保证了不需要同步代码的异步执行性
    //又保证了单例的效果

    public static TestSington getInstance(){
        try {
            if(testSington != null){

            }else{
                //模拟在创建对象之前做一些准备性的工作
                Thread.sleep(3000);
                synchronized (TestSington.class){
                    if(testSington==null){
                        testSington=new TestSington();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return testSington;
    }
}

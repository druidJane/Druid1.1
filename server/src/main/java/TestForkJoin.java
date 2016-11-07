import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by 1115 on 2016/11/7.
 */
public class TestForkJoin extends RecursiveTask<Long>{
    private static final int THREADSHOLD = 10000;
    private long start;
    private long end;
    TestForkJoin(long start,long end){
        this.start = start;
        this.end = end;
    }
    @Override
    protected Long compute() {
        long sum = 0;
        boolean canCompute = (end-start)<THREADSHOLD;

        if(canCompute){
            for(long i =start;i<=end;i++){
                sum+=i;
            }
        }else{
            long step = (end+start)/100;
            ArrayList<TestForkJoin> subTasks = new ArrayList<TestForkJoin>();
            long pos = start;
            for(int i=0;i<100;i++){
                long lastOne = pos+step;
                TestForkJoin subTask = new TestForkJoin(pos, lastOne);
                pos += step+1;
                subTasks.add(subTask);
                subTask.fork();
            }
            for(TestForkJoin task:subTasks){
                sum += task.join();
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        TestForkJoin task = new TestForkJoin(0, 200000L);
        ForkJoinTask<Long> result = forkJoinPool.submit(task);
        try {
            Long sum = result.get();
            System.out.println("sum="+sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

//javac ThreadAddition.java
//java -cp "." ThreadAddition 2 10000

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class ThreadAddFunction3 extends Thread {
    int begin;
    int end;
    public static long Total = 0;
    private static Lock Mutex = new ReentrantLock();


	public ThreadAddFunction3(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}
 
    @Override
    public void run() {
        long locsum=0;
        
        System.out.println("[" + Thread.currentThread().getId() + "] Begin Thread " + begin + "-" + end + ".");
        for (int n = begin + 1; n <= end; n++){
            locsum += n;
        }

        Mutex.lock();
        Total = Total + locsum;
        Mutex.unlock();
        
        System.out.println("[" + Thread.currentThread().getId() + "] End Thread.");
    }
}
 
class ThreadAdditionSynchronized3 {
	static int N = 2;
	static int M = 10;

    public static void main(String[] args) {
        int begin;
        int end;
        ArrayList<ThreadAddFunction3> threadList = new ArrayList<>();
        
        if (args.length != 2)
        {
            System.out.println("ThreadAddition [NumThreads] [MaxNumber].\n");
            System.exit(1);
        }

        N = Integer.parseInt(args[0]);
        M = Integer.parseInt(args[1]);
		
		final long startTime = System.nanoTime();
        
        try {
            for (int h = 0; h < N; h++) { /* Thread h */
                begin = h * (M/N);
                end = begin + M/N;
                if (h==(N-1)) 	
                	end = M;        
                threadList.add(new ThreadAddFunction3(begin, end));
                threadList.get(h).start();
            }
            
            for (int h = 0; h < N; h++) {
                threadList.get(h).join();
            }
        } catch (Exception e) {}
		final long endTime = System.nanoTime();
        System.out.println("Addition Result 1 to " + ThreadAddFunction3.Total + ".");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        System.out.println("Total execution time: " + df.format((endTime - startTime)/1000000000.0) + "s" );
    }
}

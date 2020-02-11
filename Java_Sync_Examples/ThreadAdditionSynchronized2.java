//javac ThreadAddition.java
//java -cp "." ThreadAddition 2 10000

import java.util.ArrayList;
import java.text.DecimalFormat;

class ThreadAddFunction2 extends Thread {
    int begin;
    int end;
    public static long Total = 0;
    public static Object lock = new Object();

	public ThreadAddFunction2(int begin, int end) {
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

        synchronized(lock) {
            Total = Total + locsum;
        }
        System.out.println("[" + Thread.currentThread().getId() + "] End Thread.");
    }
}
 
class ThreadAdditionSynchronized2 {
	static int N = 2;
	static int M = 10;

    public static void main(String[] args) {
        int begin;
        int end;
        ArrayList<ThreadAddFunction2> threadList = new ArrayList<>();
        
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
                threadList.add(new ThreadAddFunction2(begin, end));
                threadList.get(h).start();
            }
            
            for (int h = 0; h < N; h++) {
                threadList.get(h).join();
            }
        } catch (Exception e) {}
		final long endTime = System.nanoTime();
        System.out.println("Addition Result 1 to " + ThreadAddFunction2.Total + ".");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        System.out.println("Total execution time: " + df.format((endTime - startTime)/1000000000.0) + "s" );
    }
}

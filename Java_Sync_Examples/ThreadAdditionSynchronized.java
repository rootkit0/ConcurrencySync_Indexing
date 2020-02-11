//javac ThreadAddition.java
//java -cp "." ThreadAddition 2 10000

import java.util.ArrayList;
import java.text.DecimalFormat;

class ThreadAddFunction extends Thread {
    long begin;
    long end;
    public static long Total = 0;

	public ThreadAddFunction(long begin, long end) {
		this.begin = begin;
		this.end = end;
	}
 
    public static synchronized void add(long n) {
		Total +=n;
	}
	 
    @Override
    public void run() {
        
        System.out.println("[" + Thread.currentThread().getId() + "] Begin Thread " + begin + "-" + end + ".");
        for (long n = begin + 1; n <= end; n++){
            add(n);
        }
        System.out.println("[" + Thread.currentThread().getId() + "] End Thread.");
    }
}
 
class ThreadAdditionSynchronized {
	static long N = 4;
	static long M = 100000;

    public static void main(String[] args) {
        long begin;
        long end;
        ArrayList<ThreadAddFunction> threadList = new ArrayList<>();
        
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
                if (h==(N-1)) 	end = M; 
                
                threadList.add(new ThreadAddFunction(begin, end));
                threadList.get(h).start();
            }
            
            for (int h = 0; h < N; h++) {
                threadList.get(h).join();
            }
        } catch (Exception e) {}
		final long endTime = System.nanoTime();
        System.out.println("Addition Result 1 to " + ThreadAddFunction.Total + ".");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        System.out.println("Total execution time: " + df.format((endTime - startTime)/1000000000.0) + "s" );
    }
}

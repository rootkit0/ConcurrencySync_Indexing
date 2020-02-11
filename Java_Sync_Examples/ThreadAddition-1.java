//javac ThreadAddition.java
//java -cp "." ThreadAddition 2 10000

import java.util.ArrayList;
import java.text.DecimalFormat;


class ThreadAddFunction extends Thread {
    int begin;
    int end;
    public static long Total = 0;

	public ThreadAddFunction(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}
 
    @Override
    public void run() {
        
        System.out.println("[" + Thread.currentThread().getId() + "] Begin Thread " + begin + "-" + end + ".");
        for (int n = begin + 1; n <= end; n++){
            Total = Total + n;
        }
        System.out.println("[" + Thread.currentThread().getId() + "] End Thread.");
    }
}
 
class ThreadAddition {
	static int N = 2;
	static int M = 10;

    public static void main(String[] args) {
        int begin;
        int end;
        int pendiente;
        ArrayList<ThreadAddFunction> threadList = new ArrayList<>();
        
        if (args.length != 2)
        {
            System.out.println("ThreadAddition [NumThreads] [MaxNumber].\n");
            System.exit(1);
        }

        N = Integer.parseInt(args[0]);
        M = Integer.parseInt(args[1]);
		
		final long startTime = System.nanoTime();
        
        pendiente = M;
        try {
            for (int h = 0; h < N; h++) { /* Thread h */
                begin = h * (pendiente/(N-h));
                end = begin + (pendiente/(N-h));
                
                threadList.add(new ThreadAddFunction(begin, end));
                threadList.get(h).start();
                pendiente -= (pendiente/(N-h));
            }
            
            for (int h = 0; h < N; h++) {
                threadList.get(h).join();
            }
        } catch (Exception e) {}
		final long endTime = System.nanoTime();
        System.out.println("Addition Result 1 to " + threadList.get(0).Total + ".");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        System.out.println("Total execution time: " + df.format((endTime - startTime)/1000000000.0) + "s" );
    }
}

//javac ThreadAddition.java
//java -cp "." ThreadAdditionVarCond 2 10000 150

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.text.DecimalFormat;


class ThreadAddFunction extends Thread {
    int begin;
    int end;
    int nThreads;
    int partialStatidistics;
    public static long Total = 0;
    public static int PartialPendingThreads;
    public static int BarrierThreads1;
    public static int BarrierThreads2;
    public static ReentrantLock Lock1 = new ReentrantLock();
    public static Condition Barrier1 = Lock1.newCondition();
    public static ReentrantLock Lock2 = new ReentrantLock();
    public static Condition Barrier2 = Lock2.newCondition();


	public ThreadAddFunction(int begin, int end, int N, int P) {
		this.begin = begin;
		this.end = end;
        this.nThreads = N;
        this.partialStatidistics = P;
	}

    public static synchronized void add(long n) {
        Total +=n;
    }
     
    @Override
    public void run()
    {
        int P = this.partialStatidistics;
        long localsum=0;

        System.out.println("[" + Thread.currentThread().getId() + "] Begin Thread " + begin + "-" + end + ".");
        for (long n = begin + 1; n <= end; n++, P--)
        {
            localsum += n;
            if (P==0 || n==end) 
            {
                add(localsum);
                localsum = 0;
                P = this.partialStatidistics;
                // N a N Conditional Synchronization.
                synchronized(getClass())
                {
                    PartialPendingThreads--;
                    getClass().notifyAll();
                    try {
                        while (PartialPendingThreads>0)
                            getClass().wait();
                    }
                    catch (java.lang.InterruptedException e) {}
                    if (begin==0)
                        System.out.println("[" + Thread.currentThread().getId() + "] Partial Result: "+Total);
                }
                Lock2.lock();
                BarrierThreads2 = nThreads;
                Lock2.unlock();

                // NBarrier 1st Stage.
                Lock1.lock();
                BarrierThreads1--;
                Barrier1.signalAll();
                try {
                    while(BarrierThreads1>0)
                        Barrier1.await();
                }
                catch (java.lang.InterruptedException e) {}
                Lock1.unlock();
                synchronized(getClass())
                {
                    PartialPendingThreads = nThreads;
                }
 
                // NBarrier 2nd Stage.
                Lock2.lock();
                BarrierThreads2--;
                Barrier2.signalAll();
                try {
                    while(BarrierThreads2>0)
                        Barrier2.await();
                }
                catch (java.lang.InterruptedException e) {}
                Lock2.unlock();
                Lock1.lock();
                BarrierThreads1 = nThreads;
                Lock1.unlock();
            }
        }
    }
}

 
class ThreadAdditionVarCond 
{

    public static void main(String[] args) 
    {
        int N = 2, M = 100, P = 10;
        int begin, end, pendiente;
        ArrayList<ThreadAddFunction> threadList = new ArrayList<>();
        
        if (args.length != 3)
        {
            System.out.println("ThreadAddition [NumThreads] [MaxNumber] [PartialStatidistic].\n");
            System.exit(1);
        }

        N = Integer.parseInt(args[0]);
        M = Integer.parseInt(args[1]);
        P = Integer.parseInt(args[2]);
		
		final long startTime = System.nanoTime();
        
        ThreadAddFunction.PartialPendingThreads = N;
        ThreadAddFunction.BarrierThreads1 = N;
        ThreadAddFunction.BarrierThreads2 = N;
        pendiente = M;
        try {
            for (int h = 0; h < N; h++) 
            { 
                /* Thread h */
                begin = h * (pendiente/(N-h));
                end = begin + (pendiente/(N-h));
                
                threadList.add(new ThreadAddFunction(begin, end, N, P));
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

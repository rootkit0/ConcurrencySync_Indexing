
public class PingPong extends Thread
{
	private String word;
	public PingPong(String s) {word=s;}

	public void run()
	{
		for (int i=0;i<100;i++)
		{
			System.out.print(word);
			System.out.flush();
			try {
				this.sleep(100);
			} catch(InterruptedException ex) {
 				   this.interrupt();
			}
		}
		System.out.println();
   }

   public static void main(String[] args)
   {
		SynchronizedPingPong tP=new SynchronizedPingPong("P");
		SynchronizedPingPong tp=new SynchronizedPingPong("p");
		tp.start();
		tP.start();
		try {
			tp.join();
			tP.join();
		} catch(InterruptedException ex) {}		
	}
}

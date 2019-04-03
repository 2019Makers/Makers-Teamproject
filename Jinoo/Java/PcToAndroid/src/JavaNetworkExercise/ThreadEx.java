package JavaNetworkExercise;

public class ThreadEx {
	public static void main(String args[]) {
		ThreadEx test = new ThreadEx();
		Tmp t = test.new Tmp();
		t.start();
	}
	class Tmp extends Thread{
		public void run() {
			System.out.println("스레드 실행...");
		}
	}
}
